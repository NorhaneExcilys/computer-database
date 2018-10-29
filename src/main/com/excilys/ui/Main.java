package com.excilys.ui;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.excilys.config.SpringCliConfig;

@Component
public class Main {
	
	public static void main(String[] args) {

		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringCliConfig.class)) {
			UserInterface ui = context.getBean(UserInterface.class);
			ui.InitInterface();
		}
	}
}