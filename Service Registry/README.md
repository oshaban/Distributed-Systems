# Service Registry and Discovery

This is an implementation of Service Discovery and Registry

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

To run the application:

- The application is best ran in separate terminal windows to simulate multiple servers connecting to the service discovery

```
java -jar your-jar-file port
``` 
- The port can be passed as a command line argument