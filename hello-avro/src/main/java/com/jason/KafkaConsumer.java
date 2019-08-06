package com.jason;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by jc6t on 2015/8/10.
 */
public class KafkaConsumer implements Runnable {
    private static org.apache.avro.Schema schema;
    public static void main(String[] args) {
        try {
            schema = AvroUtil.getSchema(KafkaProducer.class.getClassLoader().getResource("schema.avsc").getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // setting consumerConnector
        Properties props = new Properties();
        props.put("zookeeper.connect", "10.16.205.106:2181,10.16.198.7:2181");// got msg from zookeeper
        props.put("group.id", "g2");// different group could consume the same topic
        props.put("zookeeper.session.timeout.ms", "6000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
//        props.put("auto.commit.enable","false");
        props.put("auto.offset.reset", "largest");
        ConsumerConnector consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));


        /**
         * create consumerMap, 塞入一個topic有幾個thread在消費的Map，
         * 一個topic 對應一個List<>，這個topic有幾個thread，list size就是幾個
         */
        int numThreads = 1;
        String topic = "test3";
        System.out.println("a_numThreads = " + numThreads);
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, numThreads); // 這個topic有幾個thread在消費
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);

        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        System.out.println("streams.size() = " + streams.size());
        // now launch all the threads
        //
        ExecutorService es = Executors.newFixedThreadPool(numThreads);

        // now create an object to consume the messages
        //
        int threadNumber = 1;
        for (KafkaStream<byte[], byte[]> stream : streams) {
            System.out.println("threadNumber: " + threadNumber);
            es.submit(new KafkaConsumer(stream, threadNumber));
            threadNumber++;
        }

        try {
            es.shutdown();
            es.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {

        }
    }

    private KafkaStream<byte[], byte[]> m_stream;
    private int m_threadNumber;
    private GenericRecord genericRecord;
    private DatumReader<GenericRecord> reader;

    public KafkaConsumer(KafkaStream<byte[], byte[]> a_stream, int a_threadNumber) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
        reader = new GenericDatumReader<GenericRecord>(schema);
        System.out.println("==========finish consumer constructor=========");
    }

    public void run() {
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
        System.out.println("============start consumer run()============");
        while (it.hasNext()) {
            System.out.println("=========message start==========");
            /**
             * Here is the key point:
             * MessageAndMetadata is one of msg from kafka.
             * And notice that this is an obj with metadata.
             */
            MessageAndMetadata<byte[], byte[]> m = it.next();
            byte[] key = new byte[0];

            if (m.key() != null) {
                System.out.println("m.key()!=null");
                key = m.key();
            } else {
                System.out.println("m.key()=null");
            }

            byte[] message = m.message();

            long offset = m.offset();
//                int partition = m.partition();
//                String topic = m.topic();
//                System.out.println("Thread # is " + m_threadNumber);
//                System.out.println("message = " + new String(message));
//                System.out.println("offset = " + offset);
//                System.out.println("partition = " + partition);
//                System.out.println("topic = " + topic);
//                System.out.println(", key = " + new String(key));
//                System.out.println("m_stream = " + m);


            // deserialize by avro
            InputStream input = new ByteArrayInputStream(message);
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(input, null);
            try {
                genericRecord = reader.read(null, decoder);
            } catch (IOException e) {
            }

            System.out.println("genericRecord = " + genericRecord);
            System.out.println("==========message end===========");
        }

        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
}
