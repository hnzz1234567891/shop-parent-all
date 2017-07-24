package com.shsxt.smscenter;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Application {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new FileSystemXmlApplicationContext("classpath:application.xml");
	}
}
