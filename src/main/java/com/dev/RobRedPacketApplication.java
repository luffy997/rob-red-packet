package com.dev;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan(value="com.dev.mapper")
@SpringBootApplication
public class RobRedPacketApplication {

    public static void main(String[] args) {
        SpringApplication.run(RobRedPacketApplication.class, args);
    }

}
