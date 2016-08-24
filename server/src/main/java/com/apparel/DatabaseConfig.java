package com.apparel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class DatabaseConfig
{
    Logger logger = Logger.getLogger(DatabaseConfig.class.getSimpleName());

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSourceProd() {
        String databaseUrl = env.getProperty("database.url");
        String databaseUsername = env.getProperty("database.username");
        String databasePassword = env.getProperty("database.password");

        if(databaseUrl == null || databaseUsername == null || databasePassword == null) {
            logger.log(Level.ALL, "Using local H2 database");
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        }

        logger.log(Level.ALL, "Using database " + databaseUrl);
        return DataSourceBuilder.create()
                .url(databaseUrl)
                .username(databaseUsername)
                .password(databasePassword)
                .build();

    }

}