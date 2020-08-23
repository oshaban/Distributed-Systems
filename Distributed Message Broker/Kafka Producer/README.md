# Distributed Message Broker

This is an example of using Kafka as a distributed message broker

## Usage

This assumes that Kafka is installed. It can be installed with homebrew:

```
brew install kafka
```

Once Kafka is installed we need to start a ZooKeeper instance:

```
zookeeper-server-start resources/zookeeper.properties
```

Then we can start 3 Kafka Brokers:

```
kafka-server-start resources/server.properties
kafka-server-start resources/server-1.properties
kafka-server-start resources/server-2.properties
```

To compile the application:

```
mvn clean package
```