package com.excilys.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MainApplication implements WebApplicationInitializer {
    
	@Override
    public void onStartup(final ServletContext sc) throws ServletException {
        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        //root.scan("com.excilys");
        root.register(AppConfig.class);
        sc.addListener(new ContextLoaderListener(root));
    }
}