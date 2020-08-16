# Leader Election Algorithm

This is an implementation of the Leader Election Algorithm using Apache Zookeeper

## Usage

To start the Apache Zookeeper docker instance:

```
docker-compose up
```

Or create your container manually:

```
docker run --name zoo --restart always -d zookeeper:3.5.8
```

To connect to Apache Zookeeper CLI:
```
 docker run --net resources_default -it --rm --link resources_zoo1_1:zookeeper zookeeper zkCli.sh -server zookeeper
```

To compile the application:

```
mvn clean package
```