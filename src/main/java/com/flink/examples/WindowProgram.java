package com.flink.examples;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.datastream.WindowedStream;

public class WindowProgram {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        

        // Sample string data
        DataStream<String> words = env.fromElements("apple", "banana", "apple", "apple", "banana", "banana");

        // Key by word
        KeyedStream<String, String> keyedStream = words.keyBy(value -> value);

        // Count window of 2 for each key
        WindowedStream<String, String, GlobalWindow> windowedStream = keyedStream.countWindow(2);
       
        // Reduce to concatenate strings
        DataStream<String> result = windowedStream.reduce(new ReduceFunction<String>() {
            @Override
            public String reduce(String value1, String value2) {
                return value1 + "+" + value2;
            }
        });
        String outputPath = "file:///Users/abburupremsagar/output_flink.txt";
    
        result.writeAsText(outputPath).setParallelism(1);


        result.print();

        env.execute("String Count Window Example");
    }
}
