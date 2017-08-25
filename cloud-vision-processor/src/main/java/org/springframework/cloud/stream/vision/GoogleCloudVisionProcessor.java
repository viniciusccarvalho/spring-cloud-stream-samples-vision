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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.cloud.stream.rtmp.ImageData;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author Vinicius Carvalho
 */
@Component
public class GoogleCloudVisionProcessor {

	private ImageAnnotatorClient vision;

	private Logger logger = LoggerFactory.getLogger(GoogleCloudVisionProcessor.class);

	public GoogleCloudVisionProcessor(){
		try {
			this.vision = ImageAnnotatorClient.create();
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	String face = "  __               \n" +
			" / _|              \n" +
			"| |_ __ _  ___ ___ \n" +
			"|  _/ _` |/ __/ _ \\\n" +
			"| || (_| | (_|  __/\n" +
			"|_| \\__,_|\\___\\___|";

	@StreamListener(Sink.INPUT)
	public void onImage(ImageData imageData){
		logger.info("Payload received");

		ByteString imgBytes = ByteString.copyFrom(imageData.getData());
		Image img = Image.newBuilder().setContent(imgBytes).build();
		List<AnnotateImageRequest> requests = new ArrayList<>();
		Feature feature = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
				.addFeatures(feature)
				.setImage(img)
				.build();
		requests.add(request);
		logger.info("Call prepared");
		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
		logger.info("Response received");
		List<AnnotateImageResponse> responses = response.getResponsesList();

		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
				return;
			}
			for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
				annotation.getAllFields().forEach((k, v)->
						System.out.printf("%s : %s\n", k, v.toString()));
			}

			long facefound = res.getLabelAnnotationsList().stream().filter(entityAnnotation -> {return entityAnnotation.getDescription().equals("face");}).count();
			if(facefound > 0){
				System.out.println(face);
			}
		}
	}

}
