package org.springframework.cloud.stream.rtmp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.schema.avro.AvroSchemaMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(RtmpSourceProperties.class)
@EnableAsync
@EnableBinding(Source.class)
public class RtmpScreenCaptureSourceApplication {

	@Autowired
	private RtmpSourceProperties properties;

	public static void main(String[] args) {
		SpringApplication.run(RtmpScreenCaptureSourceApplication.class, args);
	}

	@Bean
	public JavaCvFrameCapturer frameCapturer(ImageEmitter emitter) {
		JavaCvFrameCapturer frameCapturer = new JavaCvFrameCapturer();
		frameCapturer.setProperties(properties);
		frameCapturer.setImageEmitter(emitter);
		return frameCapturer;
	}

	@Bean
	public MessageConverter avroConverter(){
		return new AvroSchemaMessageConverter();
	}
}
