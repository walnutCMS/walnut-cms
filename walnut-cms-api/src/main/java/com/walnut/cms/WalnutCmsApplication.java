package com.walnut.cms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.walnut.cms.mapper")
public class WalnutCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalnutCmsApplication.class, args);
    }
} 