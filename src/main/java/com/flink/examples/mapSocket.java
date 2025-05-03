package com.flink.examples;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class mapSocket {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String host = "localhost";
        int port = 2222;

        // Read text from socket
        DataStream<String> text = env.socketTextStream(host, port);

        // Convert String to Integer and double it
        DataStream<Integer> numbers = text.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String value) {
                return Integer.parseInt(value.trim()) * 2;
            }
        });

        // Print results
        numbers.print();

        env.execute("Socket Map Example");
    }
}
