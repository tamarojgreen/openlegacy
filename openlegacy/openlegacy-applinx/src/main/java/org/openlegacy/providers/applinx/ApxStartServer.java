package org.openlegacy.providers.applinx;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ApxStartServer {

	public static void main(String[] args) throws IOException {
		new ClassPathXmlApplicationContext("classpath*:/test-apx-context.xml");

		System.out.println("Press any key stop");
		System.in.read();
	}
}
