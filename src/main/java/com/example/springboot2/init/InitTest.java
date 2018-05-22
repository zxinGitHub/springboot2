package com.example.springboot2.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统初始化接口
 * Created by zx9035 on 2018/5/18.
 */
@Component
public class InitTest implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(InitTest.class);

    @Override
    public void run(String... strings) throws Exception {
        while (true) {
            logger.info("当前时间"+System.currentTimeMillis());
            logger.error("当前时间"+System.currentTimeMillis());
            logger.warn("当前时间"+System.currentTimeMillis());
            Thread.sleep(10000);
        }
    }
}
