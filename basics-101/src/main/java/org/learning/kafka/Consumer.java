package org.learning.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am kafka consumer....flow started... ");
        String groupId = "java-application-intellij";
        String topic = "sales_events";
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:19092"); //connects to localhost
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);
        //can have three value
        //none -> if no consumer-grp are set when consuming -> application fails
        //earlies -> read from beginning
        //none -> read latest msg only afer started consuming
        properties.setProperty("auto.offset.reset", "earliest");


        //create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //subscribe - can use collection or pattern of topic
        consumer.subscribe(Arrays.asList(topic));

        while (true)
        {
            log.info("msg polling");

            //if data it will read data write away
            //if no data -> it will wait for time defined inside poll()
            

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));

            for (ConsumerRecord<String, String> record: records)
            {
                log.info("key: " + record.key() + ", value:   " + record.value() + " Partition: " + record.partition() + ", Offset:   " + record.offset());
            }
        }


    }
   
    
}
