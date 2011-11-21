package org.openlegacy.providers.applinx;

import java.io.IOException;

import org.openlegacy.utils.RequestMockUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServer {

	public static void main(String[] args) throws IOException {
		RequestMockUtil.initRequest();
		new ClassPathXmlApplicationContext("classpath*:/test-common-context.xml");

		System.out.println("Press any key stop");
		System.in.read();
	}
}
