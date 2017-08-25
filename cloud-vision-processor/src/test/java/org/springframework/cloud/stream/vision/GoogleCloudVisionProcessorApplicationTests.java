package org.springframework.cloud.stream.vision;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class GoogleCloudVisionProcessorApplicationTests {

	@Test
	public void contextLoads() throws Exception{
		ImageAnnotatorClient vision = ImageAnnotatorClient.create();
		String fileName = "/Users/vcarvalho/Desktop/barack.jpg";

		// Reads the image file into memory
		Path path = Paths.get(fileName);
		byte[] data = Files.readAllBytes(path);
		ByteString imgBytes = ByteString.copyFrom(data);

		// Builds the image annotation request
		List<AnnotateImageRequest> requests = new ArrayList<>();
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
				.addFeatures(feat)
				.setImage(img)
				.build();
		requests.add(request);

		// Performs label detection on the image file
		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
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
		}
	}
}



