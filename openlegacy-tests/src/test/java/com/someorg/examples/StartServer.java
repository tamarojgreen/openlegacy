package com.someorg.examples;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServer {

	public static void main(String[] args) throws IOException {
		RequestMockUtil.initRequest();
		new ClassPathXmlApplicationContext("classpath*:/test-common-context.xml");

		System.out.println("Press any key stop");
		System.in.read();
	}
}
