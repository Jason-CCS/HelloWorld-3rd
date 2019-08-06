package com.jason;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by jc6t on 2015/8/10.
 */
public class KafkaProducer implements Runnable {
    private final ProducerConfig config;
    private int threadID;
    private static Schema schema;

    public KafkaProducer(int threadID) {
        // 卡夫卡吃java native properties，所以可以去查詢kafka properties有哪些可以設定，producer的configuration
        Properties props = new Properties();
        props.put("metadata.broker.list", "10.16.198.7:9092, 10.16.205.106:9092");
        props.put("serializer.class", "com.jason.app.AvroEncoder");
        props.put("request.required.acks", "1"); //需要多少次的acks
        config = new ProducerConfig(props);
        this.threadID = threadID;

    }

    public void run() {
        Producer<String, GenericRecord> producer = new Producer<String, GenericRecord>(config);
        List<KeyedMessage<String, GenericRecord>> messages = new ArrayList<KeyedMessage<String, GenericRecord>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = AvroUtil.getOneSampleData(i);
            GenericRecord gr = AvroUtil.genGenericRecord(schema, map);
            KeyedMessage<String, GenericRecord> data = new KeyedMessage<String, GenericRecord>("test3", gr);
            messages.add(data);
        }
        producer.send(messages);
        producer.close();
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            schema = AvroUtil.getSchema(KafkaProducer.class.getClassLoader().getResource("schema.avsc").getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int threadNum = 1;
        ExecutorService es = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            es.submit(new KafkaProducer(i + 1));
        }
        es.shutdown();
        es.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("send complete!");
    }
}
