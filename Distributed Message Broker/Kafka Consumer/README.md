# Kafka Consumer in Distributed Message Broker

This is an implementation of a Kafka Consumer in a Distributed Message Broker System.

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