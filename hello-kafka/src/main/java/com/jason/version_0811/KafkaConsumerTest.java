package com.jason.version_0811;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jc6t on 2015/6/17.
 */
public class KafkaConsumerTest {
    private static final Logger log = LogManager.getLogger(KafkaConsumerTest.class);

    public static void main(String[] args) {
        /**
         * config
         */
        String zooKeeper = "10.16.205.106:2181,10.16.198.7:2181"; // got msg from zookeeper
        String groupId = "g1";// different group could consume the same topic
        String topic = "test";
        int threadNum = 3;

        /**
         * consumer config
         */
        Properties props = new Properties();
        props.put("zookeeper.connect", zooKeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset","smallest");
        props.put("auto.commit.enable","true");

        ConsumerConnector connector = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));


        Map<String, Integer> topicThreadNumMap = new HashMap<String, Integer>();
        topicThreadNumMap.put(topic, new Integer(threadNum)); // 這個topic有幾個thread在消費

        /**
         * create consumerMap, 塞入一個topic有幾個thread在消費的Map，
         * 一個topic 對應一個List<>，這個topic有幾個thread，list size就是幾個
         */
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicThreadNumMap);
        connector.commitOffsets();

        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        System.out.println("streams.size() = " + streams.size());

        /**
         * submit tasks to es
         */
        ExecutorService es = Executors.newCachedThreadPool();

        // now create an object to consume the messages
        //
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            es.submit(new MessagePuller(stream));
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        es.shutdown();
    }


    static class MessagePuller implements Runnable {
        private static AtomicInteger ai = new AtomicInteger(0);
        private KafkaStream<byte[], byte[]> m_stream;
        private int threadID;

        public MessagePuller(KafkaStream<byte[], byte[]> a_stream) {
            this.m_stream = a_stream;
            this.threadID =ai.getAndAdd(1);
                    System.out.println("==========finish MessagePuller constructor=========");
        }

        public void run() {
            ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
            System.out.println("============start MessagePuller run()============");
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
                int partition = m.partition();
                String topic = m.topic();
                System.out.println("MessagePuller threadID is " + threadID);
                System.out.println("message = " + new String(message));
                System.out.println("offset = " + offset);
                System.out.println("partition = " + partition);
                System.out.println("topic = " + topic);
                System.out.println(", key = " + new String(key));
                System.out.println("m_stream = " + m);
                System.out.println("==========message end===========");
            }
        }
    }
}

