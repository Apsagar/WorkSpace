package com.flink.examples;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ReadFile {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Specify your file path, ensure the file path is correct
        FileSource<String> fileSource = FileSource.forRecordStreamFormat(
                new TextLineInputFormat(), 
                new Path("file:///Users/abburupremsagar/Downloads/sample-1.txt")  // Update the file path
        ).build();

        // Reading data from the file source
        DataStream<String> text = env.fromSource(
                fileSource,
                WatermarkStrategy.noWatermarks(),
                "file-input"
        );

        // Print the content of the file
        text.print();

        // Execute the Flink job
        env.execute("Flink FileSource Example");
    }
}
