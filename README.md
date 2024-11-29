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
### Connects to conduktor.io

 ### Setup the $PATH environment variable
*  In order to easily access the Kafka binaries, you can edit your PATH variable by adding the following line (edit the content to your system) to your system run commands (for example ~/.zshrc if you use zshrc):

```python PATH="$PATH:/Users/stephanemaarek/kafka_2.13-3.0.0/bin" ```

* Start Kafka in docker
curl -L https://releases.conduktor.io/quick-start -o docker-compose.yml && docker compose up -d --wait && echo "Conduktor started on http://localhost:8080"


* Create Topic	
```python
kafka-topics.sh --bootstrap-server localhost:19092 --topic consumer-grp-topic-23 --create --partitions 3 --replication-factor 1
```

* Produce msg in round robin	
```python
kafka-console-producer.sh --bootstrap-server localhost:19092 --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner --topic topic-to-test-roundRobbin
```

* Produce msg with key separated by “ : “
```python
kafka-console-producer.sh --bootstrap-server localhost:19092 --topic first_topic --property parse.key=true --property key.separator=:
```

* Consumer simple cmd with no metadata (partition name, key, value…etc)	
```python
kafka-console-consumer.sh --bootstrap-server localhost:19092 --topic truck-gps
```

* Consumer from beginning with metadata.	
```python
kafka-console-consumer.sh --bootstrap-server localhost:19092 --topic topic-to-test-roundRobbin --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true --from-beginning
```

* Consume msg from that group and be part of that group or join (if group does not exist, new one is created)	
```python
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --group my-first-application 
```

* Consumer group describe in detail
```python
kafka-consumer-groups.sh --bootstrap-server localhost:19092 --group consumer-grp-for-23 --describe
```

* Reset offset dry run(not executed yet-shows how output would be if executed)	
```python
kafka-consumer-groups.sh --bootstrap-server localhost:19092 --group grp-v3 --reset-offsets --to-earliest --topic tpc-consmr-grp --dry-run
```

* Actually run the reset offset
```python
kafka-consumer-groups.sh --bootstrap-server localhost:19092 --group grp-v3 --reset-offsets --to-earliest --topic tpc-consmr-grp --execute
```

*Note: There are multiple way to reset offset-> specific timestamp, from beginnin, etc*
	
----    
### Run kafka without zookeeper on local(KRAFT MODE)

* The first step is to generate a new ID for your cluster
```python
~/kafka_2.13-3.8.1/bin/kafka-storage.sh random-uuid
```
This returns a UUID, for example 76BLQI7sT_ql1mBfKsOk9Q

* Next, format your storage directory (replace <uuid> by your UUID obtained above)
```python
~/kafka_2.13-3.8.1/bin/kafka-storage.sh format -t <UUID> -c ~/kafka_2.13-3.8.1/config/kraft/server.properties
```
This will format the directory that is in the log.dirs in the config/kraft/server.properties file (by default /tmp/kraft-combined-logs)


* Launch the broker itself in daemon mode by running this command
```python
~/kafka_2.13-3.8.1/bin/kafka-server-start.sh ~/kafka_2.13-3.8.1/config/kraft/server.properties
```
* List all available topics
```python
kafka-topics.sh --bootstrap-server localhost:9092  --list
```

## Questions
1) Why flush() and close() method are important?
* without those methods, producer might send not send msg and exit the program.

*   producer.send() method does not send the msg right away to kafka, puts the msg in queue as producer has buffer where it tries to bundled msg together and send in batches.
*   flush(): we say hey send all msg that is in buffer immediately to kafka and blocks the program until done.
*   close(): cleanup - free resources, etct. internally calls flush(), we did mention in code to explain flush() usage, flush() is optional.
