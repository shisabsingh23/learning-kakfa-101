package org.learning.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoStickyPartitioner {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoStickyPartitioner.class.getSimpleName());

    public static void main(String[] args) {
        log.info("ProducerDemoWithCallBack flow started ");

        //create producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:19092"); //connects to localhost
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //set batch to low size to have msg go to different partition
        //or else follows stickyPartitioner
        properties.setProperty("batch.size", "40");

        //create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
clear
        for (int i = 0; i < 30; i++) {
           //create a record/msg
            ProducerRecord<String, String> producerRecord
                    = new ProducerRecord<>("topic-for-ide", "**Demo for stickPartitioner -> " + i);

            //send data
            //use callback
            producer.send(producerRecord, new Callback() {

                @Override
                public void onCompletion(RecordMetadata metadata, Exception exp) {
                    if (exp == null) {
                        log.info("Received metadata \n"
                                + "Topic: " + metadata.topic() + "\n"
                                + "Partition: " + metadata.partition() + "\n"
                                + "Offset: " + metadata.offset() + "\n"
                                + "Timestamp: " + metadata.timestamp());
                    } else {
                        log.error("exception occured - " + exp);
                    }
                }
            });
        }

        producer.flush();
        producer.close();

    }
}
