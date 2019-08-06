package com.jason;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * Created by jc6t on 2015/6/13.
 */
public class HelloJedis {
    public static void main(String[] args) {
        /**
         * Jedis 實例不是thread safe，使用jedispool, JedisPool是使用Apache的common pool
         */
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "10.16.198.6");

        /// Jedis implements Closable. Hence, the jedis instance will be auto-closed after the last statement.
        try (Jedis jedis = pool.getResource()) {
            /// ... do stuff here ... for example
            jedis.set("foo", "bar");
            String foobar = jedis.get("foo");
            jedis.zadd("sose", 0, "car");
            jedis.zadd("sose", 0, "bike");
            Set<String> sose = jedis.zrange("sose", 0, -1);
        }
/// ... when closing your application:
        pool.destroy();
    }


}
