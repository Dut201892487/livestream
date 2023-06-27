package com.ydh.livestream;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ydh.livestream.mapper")
public class LivestreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(LivestreamApplication.class, args);
    }

}
