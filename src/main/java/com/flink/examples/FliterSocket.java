package com.flink.examples;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FliterSocket {
    public static void main(String[] args) throws Exception
     {
        StreamExecutionEnvironment env= StreamExecutionEnvironment.getExecutionEnvironment();
        String host = "localhost";
        int port = 4444;
        // Read text from socket
        DataStream<String> text=env.socketTextStream(host, port);

        DataStream<String> num=text.filter(new FilterFunction<String>() {
        
                @Override
                public boolean filter(String value) {
                    return !value.trim().equals("0");
                }

            
        });
        num.print();
        env.execute("Socket Filter Example");
    }

}
