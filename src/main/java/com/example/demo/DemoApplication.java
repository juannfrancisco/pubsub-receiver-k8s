package com.example.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class DemoApplication {

	private static final Log LOGGER = LogFactory.getLog(DemoApplication.class);


	@Bean
	public MessageChannel pubsubInputChannel() {
		return new DirectChannel();
	}


	@Bean
	public PubSubInboundChannelAdapter messageChannelAdapter(
			@Qualifier("pubsubInputChannel") MessageChannel inputChannel,
			PubSubTemplate pubSubTemplate) {

		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "sub-ms-email");
		adapter.setOutputChannel(inputChannel);
		return adapter;
	}


	@ServiceActivator(inputChannel = "pubsubInputChannel")
	public void messageReceiver(String payload) {

		// Invocar a la funcion para enviar correos

		LOGGER.info("Mensaje recibido, el mensaje es : " + payload);
	}




	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
