package ru.dgis.casino.plain;

import io.confluent.connect.avro.AvroData;
import io.confluent.connect.hdfs.Format;
import io.confluent.connect.hdfs.HdfsSinkConnectorConfig;
import io.confluent.connect.hdfs.RecordWriter;
import io.confluent.connect.hdfs.RecordWriterProvider;
import io.confluent.connect.hdfs.SchemaFileReader;
import io.confluent.connect.hdfs.hive.HiveMetaStore;
import io.confluent.connect.hdfs.hive.HiveUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.sink.SinkRecord;

import java.io.IOException;

public class PlainTextFormat implements Format {
    @Override
    public RecordWriterProvider getRecordWriterProvider() {
        return new RecordWriterProvider() {
            @Override
            public String getExtension() {
                return ".txt";
            }

            @Override
            public RecordWriter<SinkRecord> getRecordWriter(final Configuration conf, final String fileName, SinkRecord record, AvroData avroData) throws IOException {
                return new RecordWriter<SinkRecord>() {
                    private final Path path = new Path(fileName);
                    private final FSDataOutputStream hadoop = path.getFileSystem(conf).create(path);

                    @Override
                    public void write(SinkRecord sinkRecord) throws IOException {
                        hadoop.write(sinkRecord.value().toString().getBytes());
                        hadoop.write("\n".getBytes());
                    }

                    @Override
                    public void close() throws IOException {
                        hadoop.close();
                    }
                };
            }
        };
    }

    @Override
    public SchemaFileReader getSchemaFileReader(AvroData avroData) {
        return null;
    }

    @Override
    public HiveUtil getHiveUtil(HdfsSinkConnectorConfig config, AvroData avroData, HiveMetaStore hiveMetaStore) {
        return null;
    }
}
