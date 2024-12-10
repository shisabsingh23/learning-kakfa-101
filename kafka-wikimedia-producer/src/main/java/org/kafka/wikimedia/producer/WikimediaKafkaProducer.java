package org.kafka.wikimedia.producer;


import com.launchdarkly.eventsource.EventHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class WikimediaKafkaProducer
{
    public static void main( String[] args )
    {

        final String bootstrapServers = "localhost:19092";
        final String topic = "wikimedia-recent-changes";

        //create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); //connects to localhost
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //Interface to handle event coming from wikimedia stream and push to Kafka Topic
        //EventHandler eventHandler = TODD;
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";



    }
}
