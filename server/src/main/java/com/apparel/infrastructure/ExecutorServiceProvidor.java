package com.apparel.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Joe Deluca on 10/23/2016.
 */
@Configuration
public class ExecutorServiceProvidor {

    @Bean
    public ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(5);
    }
}
