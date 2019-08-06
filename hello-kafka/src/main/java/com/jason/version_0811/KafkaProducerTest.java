package com.jason.version_0811;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jc6t on 2015/6/17.
 */
public class KafkaProducerTest {
    private static AtomicInteger ai = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put("metadata.broker.list", "10.16.198.7:9092, 10.16.205.106:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1"); //需要多少次的acks
        ProducerConfig config = new ProducerConfig(props);
        Producer producer = new Producer(config);

        int threadNum = 1;
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < threadNum; i++) {
            es.submit(new MessagePusher(config, 1));
        }

        /*for (int i = 0; i < threadNum; i++) {
            es.submit(new MessagePusherUsingTheSameProducer(producer,1000));
        }*/

        es.shutdown();
        while (true) {
            if (es.awaitTermination(2, TimeUnit.SECONDS)) {
                break;
            }
        }
        System.out.println("Tasks finished!!!");
    }

    static class MessagePusher implements Runnable {
        private final ProducerConfig config;
        private int msgNum;
        private int threadID;

        public MessagePusher(ProducerConfig config, int msgNum) {
            this.config = config;
            this.msgNum = msgNum;
            this.threadID = ai.getAndAdd(1);
        }

        public void run() {
            System.out.println("=====MessagePusher start run()=====");
            Producer<String, String> producer = new Producer<String, String>(config);
            List<KeyedMessage<String, String>> messages = new ArrayList<KeyedMessage<String, String>>();
            for (int i = 1; i <= this.msgNum; i++) {
                KeyedMessage<String, String> data = new KeyedMessage<String, String>("test", "MessagePusher threadID is " + threadID + ", msg # is " + i);
                System.out.println(data.message());
                messages.add(data);
//                if (i % 100 == 0) {
                    producer.send(messages);
                    messages.clear();
                    /*try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
//                }
            }
            System.out.println("=====MessagePusher finish run()=====");
            producer.close();
        }
    }

    static class MessagePusherUsingTheSameProducer implements Runnable {
        private final Producer producer;
        private int msgNum;
        private int threadID;


        public MessagePusherUsingTheSameProducer(Producer producer, int msgNum) {
            this.producer = producer;
            this.msgNum = msgNum;
            this.threadID = ai.getAndAdd(1);
        }

        public void run() {
            System.out.println("=====MessagePusherUsingTheSameProducer start run()=====");
            List<KeyedMessage<String, String>> messages = new ArrayList<KeyedMessage<String, String>>();
            for (int i = 1; i <= this.msgNum; i++) {
                KeyedMessage<String, String> data = new KeyedMessage<String, String>("test", String.valueOf(this.threadID),"MessagePusherUsingTheSameProducer threadID is " + threadID + ", msg # is " + i);
                System.out.println(data.message());
                messages.add(data);
                if (i % 100 == 0) {
                    producer.send(messages);
                    messages.clear();
                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("=====MessagePusher finish run()=====");
        }
    }
}

