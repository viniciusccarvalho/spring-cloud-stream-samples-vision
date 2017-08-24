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

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.event.IVideoPictureEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.cloud.stream.rtmp.ImageData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Vinicius Carvalho
 */
@Component
public class ImageEmitter {

	@Autowired
	private Source source;

	@Autowired
	private RTMPSourceProperties properties;

	private Logger logger = LoggerFactory.getLogger(ImageEmitter.class);

	@Async
	public void writeImageData(IVideoPictureEvent event) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(event.getImage(),properties.getImageFormat(),baos);
			logger.info("Writing image of type {} and size {}, on timestamp: {}",properties.getImageFormat(),baos.size(),event.getTimeStamp());
			ImageData imageData = new ImageData();
			imageData.setExtension(properties.getImageFormat());
			imageData.setTimestamp(event.getTimeStamp());
			imageData.setData(ByteBuffer.wrap(baos.toByteArray()));

			this.source.output().send(MessageBuilder.withPayload(imageData).setHeader(MessageHeaders.CONTENT_TYPE,"image/"+properties.getImageFormat()).build());
		}catch (Exception e){

		}
	}
}
