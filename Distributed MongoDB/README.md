# Distributed MongoDB

This is a fully replicated MongoDB cluster with 3 nodes using a Replication Set.

## Usage

1. Create separate directories for the mongo db config files:


`mongod --replSet rs0 --port 27017 --bind_ip 127.0.0.1 --dbpath /usr/local/var/mongodb/rs0-0 --oplogSize 128`

`mongod --replSet rs0 --port 27018 --bind_ip 127.0.0.1 --dbpath /usr/local/var/mongodb/rs0-1 --oplogSize 128`

`mongod --replSet rs0 --port 27019 --bind_ip 127.0.0.1 --dbpath /usr/local/var/mongodb/rs0-2 --oplogSize 128`


2. Connect to any of the mongodb instances, and use `rs.initiate` to elect a primary node


To compile the application:

```
mvn clean package
```