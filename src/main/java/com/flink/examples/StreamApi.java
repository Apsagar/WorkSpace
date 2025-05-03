package com.flink.examples;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Arrays;
import java.util.Properties;

public class StreamApi {
    public static void main(String[] args) {
        // Step 1: Set properties
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Step 2: Define processing logic
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream("input-topic");

        stream.flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")))
              .groupBy((key, word) -> word)
              .count()
              .toStream()
              .foreach((word, count) -> System.out.println(word + ": " + count));

        // Step 3: Start stream
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}
