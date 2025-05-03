package com.flink.examples;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerExample {
    public static void main(String[] args) {
        // Kafka configuration
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Send messages
        for (int i = 1; i <= 5; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("orders-events", "order-" + i, "Product " + i);
            producer.send(record);
        }

        // Close producer
        producer.close();
        System.out.println("Messages sent successfully!");
    }
}