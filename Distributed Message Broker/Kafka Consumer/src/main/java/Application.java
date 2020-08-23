import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Application {

    private static final String TOPIC = "events";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092, localhost:9093, localhost:9094";

    public static void main(String[] args) {
        String consumerGroup = "defaultConsumerGroup";

        if (args.length == 1) {
            consumerGroup = args[0];
        }
        System.out.println("Consumer is part of consumer group " + consumerGroup);

        Consumer<Long, String> kafkaConsumer = createKafkaProducer(BOOTSTRAP_SERVERS, consumerGroup);
        consumeMessages(TOPIC, kafkaConsumer);
    }

    /**
     * Sets up a Kafka Consumer
     *
     * @param bootstrapServers Kafka bootstrap servers
     * @return Kafka Consumer
     */
    public static Consumer<Long, String> createKafkaProducer(String bootstrapServers, String consumerGroup) {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new KafkaConsumer<>(properties);
    }

    /**
     * Consumes messages from Kafka
     *
     * @param topic         Topic
     * @param kafkaConsumer Kafka Consumer
     */
    public static void consumeMessages(String topic, Consumer<Long, String> kafkaConsumer) {

        // Subscribes consumers to topics
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<Long, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

            if (consumerRecords.isEmpty()) {
                // Do something else
            }

            for (ConsumerRecord<Long, String> record : consumerRecords) {
                System.out.println(String.format("Recieved record (key: %d, value: %s, partition: %d, offset: %d",
                        record.key(),
                        record.value(),
                        record.partition(),
                        record.offset()));
            }
            // Do something with the records

            // Tells Kafka that the consumer successfully consumed the messages
            kafkaConsumer.commitAsync();
        }
    }

}
