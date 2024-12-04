package org.learning.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerGracefulShutdown {
    
    private static final Logger log = LoggerFactory.getLogger(ConsumerGracefulShutdown.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am kafka consumer....following proper way to shutdown ... ");
        String groupId = "java-application-intellij";
        String topic = "sales_events";
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092"); //connects to localhost
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);
        
        properties.setProperty("auto.offset.reset", "earliest");


        //create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);


        //Step 1: get reference to main thread
        final Thread mainThread = Thread.currentThread();
        log.info("thread name: {}", mainThread.getName());

        //Step 2: add shutdownHook(note: pass new Thread here)
        //Step 3: run() add the code you want to execute while current thread has been shut

        /*
        this method analogy-> when ctrl + c is detected or shutdown is detected
        it gets trigger
        new thread is created*/

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                log.info("Detected shutdown, let's exit by calling consumer.wakeup()");
                //Step 4: wakeup()
                consumer.wakeup();
                try {
                   /* Step 5: join the main thread back to continue flow
                    this log gets printed before the WakeupException error msg
                    as shutdown hook is running line by line*/
                    log.info("@@@@@@@@ joining the main thread.....");

                   /* after the log gets printed, SHUTDOWN HOOK join the main thread back
                    main thread was stopped at the while loop , poll() method (blocks thread and waits for 1sec)
                    therefore, it goes straight back to wakeupException and prints log inside wakeupException
                    and continues the flow....*/

                    mainThread.join(); //this means wait for mainThread which was blocked in the poll() to complete
                    // and then continue the SHUTDOWN THREAD FLOW - prints the log below after all code flows are done


                    log.info("thread name: {}", mainThread.getName());
                } catch (InterruptedException e) {
                    log.info("InterruptedException ");
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            //subscribe - can use collection or pattern of topic
            consumer.subscribe(Arrays.asList(topic));
            while(true)
            {
                log.info("msg polling");
                log.info("############polling.....");
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));

                for (ConsumerRecord<String, String> record: records) {
                    log.info("key: " + record.key() + ", value:   " + record.value() +
                            " Partition: " + record.partition() + ", " +
                            "Offset:   " + record.offset());
                }
            }
        }
        catch (WakeupException wakeupException) {
            log.info("@@@@@@ Consumer is starting to shutdown....");
        }
        catch (Exception exception) {
            log.error("Unexpected error occured in the consumer ", exception);
        }
        finally {
            //Step : close the consumer ( will commit to offsets)
            consumer.close();
            log.info("Consumer is gracefully shutdown....");
        }

        log.info("******************************************");
       log.info("***********************");
        System.out.println("***********END END END OF LINE*************");
    }
}
