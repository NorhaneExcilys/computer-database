package com.excilys.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages= {"com.excilys.config", "com.excilys.mapper", "com.excilys.persistance", "com.excilys.service", "com.excilys.servlet", "com.excilys.ui", "com.excilys.validator"})
@EnableWebMvc
public class AppConfig {
	
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