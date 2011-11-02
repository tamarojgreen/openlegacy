package org.openlegacy.tools;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.openlegacy.utils.RequestMockUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public abstract class AbstractScreensDumper {

	public void iterate() throws Exception {
		RequestMockUtil.initRequest();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/test-common-context.xml");

		TerminalSession hostSession = applicationContext.getBean(TerminalSession.class);
		hostSession.getSnapshot();

		int count = 0;
		while (count < 100) {
			count++;
			File file = createOutput(count);

			String s = getContent(hostSession.getSnapshot());

			Writer writer = new FileWriter(file);
			writer.write(s);
			writer.close();

			hostSession.doAction(SendKeyActions.ENTER, null);
		}

	}

	protected abstract String getContent(TerminalScreen snapshot) throws Exception;

	private File createOutput(int count) {
		String outDirPath = ScreensTextDumper.class.getResource("/").getFile();
		File projectDir = new File(outDirPath).getParentFile().getParentFile();
		String relativePath = getRelativeFilePath(count);
		File file = new File(projectDir, relativePath);
		file.getParentFile().mkdirs();
		return file;
	}

	protected abstract String getRelativeFilePath(int count);

}
