package com.jason;

import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by jc6t on 2015/5/8.
 */
public class HelloLog4j {
    private static final Logger log=Logger.getLogger(HelloLog4j.class);

    public static void main(String[] args) throws InterruptedException {
        while(true){
            System.out.println("-0--------------");
            log.trace("this is trace");
            log.debug("this is debug");
            System.out.println("and so on...");

            TimeUnit.SECONDS.sleep(1);
        }
    }

}
