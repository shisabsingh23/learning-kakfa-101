package org.kafka.wikimedia.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

public class WikiMediaEventChangeHandlerImpl implements EventHandler{

	private final Logger log = LoggerFactory.getLogger(WikiMediaEventChangeHandlerImpl.class.getSimpleName());
	 private KafkaProducer<String, String> producer;
	 private String topic;
	
	public WikiMediaEventChangeHandlerImpl(KafkaProducer<String, String> producer, String topic) {
		super();
		this.producer = producer;
		this.topic = topic;
	}

	@Override
	public void onOpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClosed() {
		producer.close();
		
	}

	//sends stream received to Kafka topic
	@Override
	public void onMessage(String event, MessageEvent messageEvent) {
		log.info(messageEvent.getData());
		//async 
		producer.send(new ProducerRecord<>(topic,messageEvent.getData()));
		
	}

	@Override
	public void onComment(String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub
		
	}

}
