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
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

import com.commit451.youtubeextractor.YouTubeExtractor;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.cloud.stream.rtmp.ImageData;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Vinicius Carvalho
 */
public class JavaCvFrameCapturer implements SmartLifecycle, InitializingBean{

	private volatile boolean autoStartup = true;

	private volatile int phase = 0;

	private volatile boolean running;

	private ThreadPoolExecutor pool;

	private Map<String, VideoCapturer> instances = new ConcurrentHashMap<>();

	private Logger logger = LoggerFactory.getLogger(getClass());

	private RtmpSourceProperties properties;

	private ImageEmitter imageEmitter;

	public ImageEmitter getImageEmitter() {
		return imageEmitter;
	}

	YouTubeExtractor extractor = YouTubeExtractor.create();

	public void setImageEmitter(ImageEmitter imageEmitter) {
		this.imageEmitter = imageEmitter;
	}

	protected final ReentrantLock lifecycleLock = new ReentrantLock();


	public void capture(String url){
		if(this.pool.getActiveCount() >= this.properties.getPoolSize()){
			throw new IllegalStateException("Can not accept any more frame grabbers");
		}
		boolean live = true;
		if(url.contains("youtube")){
			live = false;
			UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).build();
			String video = uriComponents.getQueryParams().get("v").get(0);
			URI uri = extractor.extract(video).blockingGet().getSd360VideoUri();
			url = uri.toString();
		}
		VideoCapturer videoCapturer = new VideoCapturer(url,live);
		instances.put(url,videoCapturer);
		pool.submit(videoCapturer);

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(this.properties.getPoolSize());
	}

	@Override
	public boolean isAutoStartup() {
		return this.autoStartup;
	}

	@Override
	public void stop(Runnable runnable) {
		stop();
		runnable.run();
	}

	@Override
	public void start() {
		this.lifecycleLock.lock();
		try {
			if (!this.running) {
				this.running = true;
				if (logger.isInfoEnabled()) {
					logger.info("started " + this);
				}
			}
		}
		finally {
			this.lifecycleLock.unlock();
		}
	}

	@Override
	public void stop() {
		this.lifecycleLock.lock();
		try {
			if (this.running) {
				instances.values().forEach(videoCapturer -> videoCapturer.running = false);
				this.pool.shutdown();
				this.running = false;
				if (logger.isInfoEnabled()) {
					logger.info("stopped " + this);
				}
			}
		}
		finally {
			this.lifecycleLock.unlock();
		}
	}

	@Override
	public boolean isRunning() {
		this.lifecycleLock.lock();
		try {
			return this.running;
		}
		finally {
			this.lifecycleLock.unlock();
		}
	}

	@Override
	public int getPhase() {
		return this.phase;
	}

	public RtmpSourceProperties getProperties() {
		return properties;
	}

	public void setProperties(RtmpSourceProperties properties) {
		this.properties = properties;
	}

	public class VideoCapturer implements Runnable{

		private String url;
		private volatile boolean running = false;
		private final String uuid = UUID.randomUUID().toString().substring(0,8);
		private boolean live;

		public VideoCapturer(String url, boolean live) {
			this.url = url;
			this.running = true;
			this.live = live;
		}

		@Override
		public void run() {
			FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(this.url);
			Java2DFrameConverter converter = new Java2DFrameConverter();
			try {
				frameGrabber.start();
				double fps = frameGrabber.getFrameRate();
				int currentFrame = 0;
				int maxFrames = live ? Integer.MAX_VALUE : frameGrabber.getLengthInFrames();
				while(running && currentFrame <= maxFrames){

					BufferedImage image = converter.getBufferedImage(frameGrabber.grabFrame());
					if(image != null){
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(image, properties.getImageFormat(), baos);
						baos.flush();
						ImageData imageData = new ImageData();
						imageData.setData(ByteBuffer.wrap(baos.toByteArray()));
						imageData.setTimestamp(System.currentTimeMillis());
						imageData.setExtension(properties.getImageFormat());
						imageData.setId(uuid);
						imageEmitter.writeImageData(imageData);
						if(!live){
							currentFrame+=fps*(properties.getIntervalBetweenFrames()/1000);
							frameGrabber.setFrameNumber(currentFrame);

						}
					}
					Thread.sleep(properties.getIntervalBetweenFrames());
				}
			}
			catch (Exception e) {
				running = false;
				logger.error("Error opening url",e);
			}finally {
				try {
					frameGrabber.stop();
				}
				catch (FrameGrabber.Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
