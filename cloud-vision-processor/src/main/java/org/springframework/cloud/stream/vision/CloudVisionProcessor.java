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

package org.springframework.cloud.stream.vision;

import ai.projectoxford.face.Face;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.cloud.stream.rtmp.ImageData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * @author Vinicius Carvalho
 */
@Component
public class CloudVisionProcessor {

	@Autowired
	private Face faceClient;

	private Logger logger = LoggerFactory.getLogger(CloudVisionProcessor.class);

	@StreamListener(Sink.INPUT)
	public void onImage(ImageData imageData){
		logger.info("Payload received");
		faceClient.detect(false,false,imageData.getData().array(),"age").subscribe(faceResponse -> {
			if(faceResponse != null){
				System.out.println("Found a face");
			}
		});

	}

}
