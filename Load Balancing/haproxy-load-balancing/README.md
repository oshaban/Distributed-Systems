# HA Proxy Load Balancing

This is an example of using HAProxy to Load Balance

## Usage

To compile the web-servers:

```
mvn clean install
```

To run the web-apps:
```
java -jar webapp/target/haproxy-example-0.0.1-SNAPSHOT.jar --server.port=9001 --name="Server 1"
java -jar webapp/target/haproxy-example-0.0.1-SNAPSHOT.jar --server.port=9002 --name="Server 2"
java -jar webapp/target/haproxy-example-0.0.1-SNAPSHOT.jar --server.port=9003 --name="Server 3"
```

To start the HAProxy Server:

```
haproxy -f haproxy/haproxy.cfg
```

To access the HAProxy Load Balancer/Gateway:
- Go to your web-browser: localhost:80