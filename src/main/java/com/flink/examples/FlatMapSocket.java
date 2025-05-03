package com.flink.examples;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class FlatMapSocket {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String host = "localhost";
        int port = 3333;

        DataStream<String> text = env.socketTextStream(host, port);

        DataStream<String> dataStream = text.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) {
                for (String word : value.split(" ")) {
                    out.collect(word);
                }
            }
        });

        dataStream.print();

        env.execute("Socket FlatMap Example");
    }
}
