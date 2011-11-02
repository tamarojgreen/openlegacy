package org.openlegacy.tools;

import org.openlegacy.utils.RequestMockUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StartServer {

	public static void main(String[] args) throws IOException {
		RequestMockUtil.initRequest();
		new ClassPathXmlApplicationContext("classpath*:/test-common-context.xml");

		System.out.println("Press any key stop");
		System.in.read();
	}
}
