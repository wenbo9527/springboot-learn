package com.example.loglearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoglearnApplication {

    private static final Logger log = LoggerFactory.getLogger(LoglearnApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LoglearnApplication.class, args);

        log.error("第一次输出错误日志");

    }

}
