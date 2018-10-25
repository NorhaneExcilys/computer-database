package com.excilys.ui;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.excilys.config.SpringCliConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;

@Component
public class Main {
	
	//private static org.slf4j.Logger logger = LoggerFactory.getLogger("Main");
	
	public static void main(String[] args) {
		//((Logger)LogManager.getLogger(HikariPool.class)).setLevel(Level.DEBUG);
		//((Logger)LogManager.getLogger(PoolElf.class)).setLevel(Level.INFO);
		//((Logger)LogManager.getLogger(HikariConfig.class)).setLevel(Level.DEBUG);

		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringCliConfig.class)) {
			UserInterface ui = context.getBean(UserInterface.class);
			ui.InitInterface();
		}
	}
}