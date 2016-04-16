package com.jomik.apparel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Mick on 25/03/2016.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class ApparelServerMainApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApparelServerMainApplication.class, args);
    }
}
