package com.jason;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jc6t on 2015/4/14.
 */
public class HelloLog4j2 {
    private static final Logger test = LogManager.getLogger(HelloLog4j2.class);
    private static final Logger testReLoad = LogManager.getLogger("testReLoad");

    public static void main(String[] args) {

        Thread t = new Thread() {
            public void run() {
                while (true) {
                    test.debug("test debug");
                    test.info("test info");
                    test.warn("test warn");
                    test.error("test error");
                    testReLoad.debug("testReload debug");
                    testReLoad.info("testReload info");
                    testReLoad.warn("testReload warn");
                    testReLoad.error("testReload error");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }

            }
        };
        t.start();
    }
}
