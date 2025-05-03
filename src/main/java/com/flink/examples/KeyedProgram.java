package com.flink.examples;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class KeyedProgram {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Create a stream of numbers
        DataStream<Integer> num = env.fromElements(1, 2, 3, 4, 5);

        // Key all elements by the same key (e.g., key "1")
        KeyedStream<Integer, Integer> keyedStream = num.keyBy(value -> 1);

        // Apply reduce to sum the values
        DataStream<Integer> summed = keyedStream.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer value1, Integer value2) {
                return value1 + value2;
            }
        });

        summed.print();

        env.execute("Keyed Reduce Example");
    }
}
