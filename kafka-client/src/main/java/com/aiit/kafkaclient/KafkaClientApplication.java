package com.aiit.kafkaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
public class KafkaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaClientApplication.class, args);
    }

}
