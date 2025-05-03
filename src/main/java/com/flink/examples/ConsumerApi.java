package com.flink.examples;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;  // Required for object creation
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;  // Required for object creation

public class ConsumerApi {
    public static void main(String[] args) {
        // Step 1: Set properties
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "order-group");

        // Step 2: Create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Step 3: Subscribe to topic
        consumer.subscribe(Collections.singletonList("orders"));

        // Step 4: Poll for data
        System.out.println("Waiting for messages...");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Key: " + record.key() + ", Value: " + record.value());
            }
        }
    }
}
