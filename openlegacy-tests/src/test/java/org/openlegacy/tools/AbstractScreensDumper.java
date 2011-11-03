package org.openlegacy.tools;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.utils.FileCommand;
import org.openlegacy.utils.FileCommandExecuter;
import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.RequestMockUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public abstract class AbstractScreensDumper {

	private final static Log logger = LogFactory.getLog(AbstractScreensDumper.class);

	public void dumpSession(File baseDir, boolean cleanupFolder) throws Exception {
		RequestMockUtil.initRequest();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/test-common-context.xml");

		ScreensRecognizer screensRecognizer = applicationContext.getBean(ScreensRecognizer.class);
		ScreenEntitiesRegistry screenEntitiesRegistry = applicationContext.getBean(ScreenEntitiesRegistry.class);

		TerminalSession hostSession = applicationContext.getBean(TerminalSession.class);
		hostSession.getSnapshot();

		if (cleanupFolder) {
			FileCommandExecuter.execute(baseDir, new FileCommand() {

				public void doCommand(File file) {
					file.delete();
				}

				public boolean accept(File file) {
					String ext = FilenameUtils.getExtension(file.getName());
					return ext.equals(getDumpFileExtension());
				}
			});
		}

		int count = 0;
		try {
			while (true) {
				count++;

				TerminalScreen terminalscreen = hostSession.getSnapshot();
				Class<?> matchedScreenEntity = screensRecognizer.match(terminalscreen);
				String entityName = null;
				if (matchedScreenEntity != null) {
					entityName = screenEntitiesRegistry.getEntityName(matchedScreenEntity);
				}

				File file = getOutputFile(baseDir, entityName, count);

				String s = getDumpContent(terminalscreen);

				Writer writer = new FileWriter(file);
				writer.write(s);
				writer.close();

				hostSession.doAction(SendKeyActions.ENTER, null);
			}

		} catch (OpenLegacyException e) {
			logger.info("Session completed:" + e.getMessage());
			System.exit(0);
		}

	}

	private File getOutputFile(File baseDir, String entityName, int count) {
		String fileNameNoSuffix = entityName != null ? entityName : "screen" + count;

		File file = FileUtils.findFreeFileName(baseDir, fileNameNoSuffix, getDumpFileExtension());
		return file;
	}

	protected abstract String getDumpContent(TerminalScreen snapshot) throws Exception;

	protected abstract String getDumpFileExtension();

}
