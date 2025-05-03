package com.flink.examples;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class KeyByExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 🔌 Read from socket (host: localhost, port: 9999)
        DataStream<String> input = env.socketTextStream("localhost", 9999);

        // 🛠️ Map each line to SensorReading object
        DataStream<SensorReading> mapped = input.map(line -> {
            String[] parts = line.split(",");
            return new SensorReading(parts[0], Integer.parseInt(parts[1]));
        });

        // 🧠 Apply keyBy and sum
        DataStream<SensorReading> result = mapped
            .keyBy(sensor -> sensor.id)
            .sum("temperature");

        result.print();

        env.execute();
    }

    // SensorReading POJO
    public static class SensorReading {
        public String id;
        public int temperature;

        public SensorReading() {}

        public SensorReading(String id, int temperature) {
            this.id = id;
            this.temperature = temperature;
        }

        public String toString() {
            return "(" + id + ", " + temperature + ")";
        }
    }
}
