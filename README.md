## Steps to create producer
* Create Producer properties
* Create Producer
* Send Data
* Flush and close the producer

## Producer 
#### Sticky Partitioner(//NOT RECOMMENDED IN PROD as we can to have it follow stickyPartition - IMPROVES NETWORK EFFICIENCY)
* Since Kafka 2.4, the Sticky Partitioner became the default for the Java Kafka client.

* When sending msg if multiple msg are send quickly, producer will accumulate msg as much as possible and send in batch when its full and then switch to new batch, leading to sending to all msg to same partition rather than doing ***RoundRobin**(each msg to different partition)*

* Improves network efficiency as less travel is done.

## Kafka CLI Cmd
#### Connects to conduktor.io


* Start Kafka in docker
```
curl -L https://releases.conduktor.io/quick-start -o docker-compose.yml && docker compose up -d --wait && echo "Conduktor started on http://localhost:8080"
```

* Create Topic	
```
kafka-topics.sh --bootstrap-server localhost:19092 --topic consumer-grp-topic-23 --create --partitions 3 --replication-factor 1
```

* Produce msg in round robin	
```
kafka-console-producer.sh --bootstrap-server localhost:19092 --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner --topic topic-to-test-roundRobbin
```

* Produce msg with key separated by “ : “
```
kafka-console-producer.sh --bootstrap-server localhost:19092 --topic first_topic --property parse.key=true --property key.separator=:
```

* Consumer simple cmd with no metadata (partition name, key, value…etc)	
```
kafka-console-consumer.sh --bootstrap-server localhost:19092 --topic truck-gps
```

* Consumer from beginning with metadata.	
```
kafka-console-consumer.sh --bootstrap-server localhost:19092 --topic topic-to-test-roundRobbin --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true --from-beginning
```

* Consume msg from that group and be part of that group or join (if group does not exist, new one is created)	
```
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --group my-first-application 
```

* Consumer group describe in detail
```
kafka-consumer-groups.sh --bootstrap-server localhost:19092 --group consumer-grp-for-23 --describe
```

* Reset offset dry run(not executed yet-shows how output would be if executed)	
```
kafka-consumer-groups.sh --bootstrap-server localhost:19092 --group grp-v3 --reset-offsets --to-earliest --topic tpc-consmr-grp --dry-run
```

* Actually run the reset offset
```
kafka-consumer-groups.sh --bootstrap-server localhost:19092 --group grp-v3 --reset-offsets --to-earliest --topic tpc-consmr-grp --execute
```

*Note: There are multiple way to reset offset-> specific timestamp, from beginnin, etc*
	

## Questions
1) Why flush() and close() method are important?
* without those methods, producer might send not send msg and exit the program.

*   producer.send() method does not send the msg right away to kafka, puts the msg in queue as producer has buffer where it tries to bundled msg together and send in batches.
*   flush(): we say hey send all msg that is in buffer immediately to kafka and blocks the program until done.
*   close(): cleanup - free resources, etct. internally calls flush(), we did mention in code to explain flush() usage, flush() is optional.