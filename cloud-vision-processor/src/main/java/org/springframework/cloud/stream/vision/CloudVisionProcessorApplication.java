package org.springframework.cloud.stream.vision;

import io.netty.handler.ssl.SslContextBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.schema.avro.AvroSchemaMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.web.client.AsyncRestTemplate;

@SpringBootApplication
@EnableBinding(Sink.class)
public class CloudVisionProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudVisionProcessorApplication.class, args);
	}

	@Bean
	public MessageConverter avroConverter(){
		return new AvroSchemaMessageConverter();
	}

	@Bean
	public AsyncRestTemplate asyncRestTemplate() throws Exception{
		return new AsyncRestTemplate();
	}
}
