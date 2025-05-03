package com.flink.examples;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import java.util.Properties;

public class KafkaSourceSinkExample {
    public static void main(String[] args) throws Exception {
        // Set up the execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Kafka configuration properties
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "flink-consumer");

        // Create Kafka source (reads from "input-events")
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
            "input-events",
            new SimpleStringSchema(),
            props
        );

        // Create Kafka sink (writes to "output-events")
        FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<>(
            "output-events",
            new SimpleStringSchema(),
            props
        );

        // If you also want to send to Kafka
        env.addSource(consumer)
           .map(value -> "Processed: " + value)
           .addSink(producer)
           .name("KafkaSink");

        // Execute the job
        env.execute("Kafka Source and Sink Example");
    }
}
