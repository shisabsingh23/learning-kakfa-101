package org.kafka.wikimedia.producer;


import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaKafkaProducer
{
    public static void main( String[] args )
    {

        final String bootstrapServers = "localhost:9092";
        final String topic = "wikimedia-recent-changes";

        //create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); //connects to localhost
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //Interface to handle event coming from wikimedia stream and push to Kafka Topic
        EventHandler eventHandler = new WikiMediaEventChangeHandlerImpl(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        //starts the producer another new THREAD
        eventSource.start();

        //block the main thread for 10 minutes
        //as eventSource.start() creates its own thread
        //we don't want main to exit/stop,
        // code below makes main thread wait for 10min
        try {
			TimeUnit.MINUTES.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }
}
