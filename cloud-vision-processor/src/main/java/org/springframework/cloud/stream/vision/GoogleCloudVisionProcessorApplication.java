package org.springframework.cloud.stream.vision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.schema.avro.AvroSchemaMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MessageConverter;

@SpringBootApplication
@EnableBinding(Sink.class)
public class GoogleCloudVisionProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleCloudVisionProcessorApplication.class, args);
	}

	@Bean
	public MessageConverter avroConverter(){
		return new AvroSchemaMessageConverter();
	}
}
