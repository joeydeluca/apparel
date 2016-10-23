package com.apparel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ApparelServerApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApparelServerApplication.class, args);
    }
}
