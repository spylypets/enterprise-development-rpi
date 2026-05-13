package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import io.r2dbc.spi.ConnectionFactory;

import javax.sql.DataSource;

//@Configuration
public class TestConfig {

    //@Bean
    //PlatformTransactionManager testTransactionManager(ConnectionFactory connectionFactory) {
    //	return new PlatformTransactionManager(connectionFactory);
    //}
}

