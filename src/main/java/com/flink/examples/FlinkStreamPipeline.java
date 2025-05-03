package com.flink.examples;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkStreamPipeline {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    
        DataStreamSource<String> source = env.fromElements("A", "B", "C", "A", "B", "C");

        // 3. Apply transformations step by step
        SingleOutputStreamOperator<String> result = source.name("source")
            .map((MapFunction<String, String>) value -> value.toLowerCase()).name("map1") // "A" → "a"
            .map((MapFunction<String, String>) value -> "Prefix_" + value).name("map2")   // "a" → "Prefix_a"
            .rebalance() // Redistribute load evenly
            .map((MapFunction<String, String>) value -> value + "_M3").name("map3")       // → "Prefix_a_M3"
            .map((MapFunction<String, String>) value -> value + "_M4").name("map4")       // → "Prefix_a_M3_M4"
            .keyBy(value -> value) // Group by value
            .map((MapFunction<String, String>) value -> value + "_Grouped").name("map5")  // → "Prefix_a_M3_M4_Grouped"
            .map((MapFunction<String, String>) value -> value + "_Final").name("map6");   // → "Prefix_a_M3_M4_Grouped_Final"

            String outputPath= "file:///Users/abburupremsagar/output_2.txt";
            result.writeAsText(outputPath).name("sink");// Write to file

        // 4. Print the final output to console


        // 5. Execute the job
        env.execute("Simple Flink Pipeline Example");
    }
}
