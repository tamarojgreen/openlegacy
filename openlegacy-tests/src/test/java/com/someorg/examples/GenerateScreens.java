package com.someorg.examples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;

import org.openlegacy.adapter.terminal.SendKeyActions;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GenerateScreens {

	public static void main(String[] args) throws IOException {
		RequestMockUtil.initRequest();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/test-common-context.xml");

		TerminalSession hostSession = applicationContext
				.getBean(TerminalSession.class);

		int count = 0;
		while (count < 100) {
			try {
				count++;
				String outDirPath = GenerateScreens.class.getResource("/")
						.getFile();
				File projectDir = new File(outDirPath).getParentFile()
						.getParentFile();
				File file = new File(projectDir + "/screens", MessageFormat
						.format("screen{0}.txt", count));
				file.getParentFile().mkdirs();
				Writer writer = new FileWriter(file);
				writer.write(hostSession.getSnapshot().toString());
				writer.close();
				hostSession.doAction(SendKeyActions.ENTER);
			} catch (OpenLegacyProviderException e) {
				return;
			}
		}
	}
}
