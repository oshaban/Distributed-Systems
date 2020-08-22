# HTTP Client

This is an implementation of an HTTP Client.

## Usage

To compile the application:

```
mvn clean package
```

To run the application:

- The application is best ran in separate terminal windows to simulate multiple servers connecting to the service discovery

```
java -jar httpclient-1.0-SNAPSHOT-jar-with-dependencies.jar <port>
``` 
- The port can be passed as a command line argument