package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableTransactionManagement
class DatabaseConfig{
	
	@Bean
	ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
	    return new R2dbcTransactionManager(connectionFactory);
	}
	
	@Bean
	DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
		return DatabaseClient.create(connectionFactory);
	}
}