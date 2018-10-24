package com.excilys.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan("com.excilys.persistance," + " com.excilys.service")
public class SpringRootConfig {
	
	@Bean
    public DataSource getDataSource() {
		HikariConfig hikariConfig = new HikariConfig("/home/elgharbi/eclipse-workspace/ComputerDatabase/datasource.properties");
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        return hikariDataSource;
    }
	
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }
	
}