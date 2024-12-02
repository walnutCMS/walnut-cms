package com.cursor.cms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cursor.cms.mapper")
public class CursorCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CursorCmsApplication.class, args);
    }
} 