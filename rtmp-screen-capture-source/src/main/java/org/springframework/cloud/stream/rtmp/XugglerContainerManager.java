/*
 *  Copyright 2017 original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.springframework.cloud.stream.rtmp;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.SmartLifecycle;

/**
 * @author Vinicius Carvalho
 *
 * The Xuggler IContainer does not offer a hook for a connection problem to the remote rtmp stream.
 * This class starts a daemon thread to handle the connection and monitor its status, if a frame has not
 * being received after a timeout, it kills the thread and respanws a new container.
 */
public class XugglerContainerManager implements SmartLifecycle {

	private volatile boolean running = false;

	private ExecutorService executorService;

	private RtmpSourceProperties properties;

	private XugglerContainerRunner runner;

	private ImageEmitter imageEmitter;

	private final Object lifecycleMonitor = new Object();

	public void setImageEmitter(ImageEmitter imageEmitter) {
		this.imageEmitter = imageEmitter;
	}

	public XugglerContainerManager(RtmpSourceProperties properties){
		this(properties,Executors.newFixedThreadPool(2));
	}

	public XugglerContainerManager(RtmpSourceProperties properties, ExecutorService executorService) {
		this.executorService = executorService;
		this.properties = properties;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		this.stop();
		callback.run();

	}

	@Override
	public void start() {
		synchronized (this.lifecycleMonitor) {
			this.runner = new XugglerContainerRunner();
			this.executorService.submit(runner);
			this.running = true;
		}
	}

	@Override
	public void stop() {
		synchronized (this.lifecycleMonitor) {
			this.executorService.shutdownNow();
			this.runner.running = false;
			this.running = false;
		}
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public int getPhase() {
		return 42_000;
	}


	class XugglerContainerRunner implements Runnable {

		private IContainer container;

		volatile boolean running = false;

		private Logger logger = LoggerFactory.getLogger(XugglerContainerRunner.class);

		public XugglerContainerRunner() {
			this.container = IContainer.make();
			this.running = true;
		}

		@Override
		public void run() {
			int videoStreamId = 0;
			boolean connected = false;

			while(!connected && running && !Thread.currentThread().isInterrupted()){
				int i = this.container.open(XugglerContainerManager.this.properties.getEndpoint(), IContainer.Type.READ, null, true, false);
				if(i >=0){
					logger.info("Connection to RTMP endpoint succesfull, waiting for frames ");
					connected = true;
				}else{
					logger.warn("Can't open remote URL, retrying in 10 seconds");
					try {
						Thread.sleep(5_000);
					}
					catch (InterruptedException e) {
						logger.warn("Task execution cancelled, probably due inactivity over the stream");
						Thread.currentThread().interrupt();
						return;
					}
				}
			}

			IMediaReader mediaReader = ToolFactory.makeReader(this.container);
			mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
			mediaReader.addListener(new ImageSnapListener());
			//This blocks even if the remote stream does not send any more picture events
			//Even worse, if the stream is closed remotely and resumes later, it can't reprocess it anymore
			//Therefore we check for inactivity and try to reconnect
			while (mediaReader.readPacket() == null && running);
			logger.info("Shutting down Runner");
		}
	}

	class ImageSnapListener extends MediaListenerAdapter {

		private final long MICRO_SECONDS_BETWEEN_FRAMES;

		private int mVideoStreamIndex = -1;

		private long mLastPtsWrite = Global.NO_PTS;

		private Logger logger = LoggerFactory.getLogger(ImageSnapListener.class);

		public ImageSnapListener(){
			this.MICRO_SECONDS_BETWEEN_FRAMES = (long)(Global.DEFAULT_PTS_PER_SECOND * XugglerContainerManager.this.properties.getIntervalBetweenFrames());
		}
		@Override
		public void onVideoPicture(IVideoPictureEvent event) {
			logger.info("Frame received at timestamp: {}",event.getTimeStamp());
			if (event.getStreamIndex() != mVideoStreamIndex) {
				// if the selected video stream id is not yet set, go ahead an
				// select this lucky video stream
				if (mVideoStreamIndex == -1)
					mVideoStreamIndex = event.getStreamIndex();
					// no need to show frames from this video stream
				else
					return;
			}

			if (mLastPtsWrite == Global.NO_PTS) {
				mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
			}
			if (event.getTimeStamp() - mLastPtsWrite >=
					MICRO_SECONDS_BETWEEN_FRAMES) {

				XugglerContainerManager.this.imageEmitter.writeImageData(event);

				mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
			}

		}
	}
}
