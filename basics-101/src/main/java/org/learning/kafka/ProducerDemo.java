package org.learning.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {
        log.info("hello there");

        //create producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:19092"); //connects to localhost
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //create a record/msg
        ProducerRecord<String, String> producerRecord
                = new ProducerRecord<>("topic-for-ide", "1st msg posted from intellij");

        //send data
        producer.send(producerRecord);

        /**
         * Flush and close are important as producer.send() is async And when we
         * flush and close it makes the program wait q until all msg's are sent
         * succesfully to kakfa
         */
        //flush the producer
        //sync operation -> tell producer to send data and block until done
        producer.flush();

        //close the producer -> this internally calls flush()
        //just done above to show its existance
        producer.close();

    }
}
