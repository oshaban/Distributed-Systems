# Banking System Distributed System

This is an exercise from the Udemy course "Distributed Systems & Cloud Computing with Java"

![Alt text](example.png?raw=true "Title")

----
Banking API Service - Receives credit card transactions from online and physical stores. Each transaction contains the following information

1. User - the user who (allegedly) made the purchase in that particular store

2. Amount - the amount of the purchase transaction in dollars

3. Transaction location - the country in which that purchase has been made

- Users Database - A database where we store each of our bank users' residence location

- User Notification Service - for every transaction this service receives, it sends a notification to the user who allegedly made the purchase with a request to log in the banking web site and confirm or reject the transaction

- Account Manager Service - every transaction that this service receives is a valid transaction. The service authorizes the transaction and transfers the money from the user's account to the store where the purchase was made

- Reporting Service - For every transaction that has been processed by the Banking API Service, the Reporting Service stores the transaction for:

1. Further investigation (if it is a suspicious transaction) or;

2. For the user's monthly statement (if it is a valid transaction)

All the communication inside our Distributed System is done through Kafka Topics