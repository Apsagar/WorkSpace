package com.flink.examples;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WordCountSocketStream {
    public static void main(String[] args) throws Exception {
        // Set up the execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Define the hostname and port for the socket stream
        String hostname = "localhost"; // Replace with your hostname if needed
        int port = 4563;

        // Create a DataStream from the socket stream
        DataStream<String> text = env.socketTextStream(hostname, port);

        // Apply transformations: Split lines into words, count them, and sum the counts
        DataStream<Tuple2<String, Integer>> wordCounts = text
                .flatMap(new Tokenizer()) // Tokenize the input lines into words
                .keyBy(value -> value.f0) // Group by the word (Tuple2 field 0)
                .sum(1); // Sum the counts (Tuple2 field 1)

        // Print the results to the console
        wordCounts.print();

        // Execute the Flink job
        env.execute("Socket WordCount");
    }

    // Tokenizer class to split lines into words and emit (word, 1) pairs
    public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // Normalize and split the line into words
            String[] words = value.toLowerCase().split("\\W+");
            for (String word : words) {
                if (word.length() > 0) {
                    out.collect(new Tuple2<>(word, 1));
                }
            }
        }
    }
}
