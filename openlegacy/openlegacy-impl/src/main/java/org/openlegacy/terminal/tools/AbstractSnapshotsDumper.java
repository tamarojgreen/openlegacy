package org.openlegacy.terminal.tools;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.utils.FileCommand;
import org.openlegacy.utils.FileCommandExecuter;
import org.openlegacy.utils.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class AbstractSnapshotsDumper {

	private final static Log logger = LogFactory.getLog(AbstractSnapshotsDumper.class);
	protected static final String OUTGOING_FILE_INDICATOR = "-out";

	public void dumpSession(File baseDir, boolean cleanupFolder, String sprintContext) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(sprintContext);

		ScreensRecognizer screensRecognizer = applicationContext.getBean(ScreensRecognizer.class);
		ScreenEntitiesRegistry screenEntitiesRegistry = applicationContext.getBean(ScreenEntitiesRegistry.class);

		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		terminalSession.getSnapshot();

		if (cleanupFolder) {
			FileCommandExecuter.execute(baseDir, new FileCommand() {

				public void doCommand(File file) {
					if (!file.getName().contains(OUTGOING_FILE_INDICATOR)) {
						file.delete();
					}
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

				TerminalSnapshot terminalSnapshot = terminalSession.getSnapshot();
				Class<?> matchedScreenEntity = screensRecognizer.match(terminalSnapshot);
				String entityName = null;
				if (matchedScreenEntity != null) {
					entityName = screenEntitiesRegistry.getEntityName(matchedScreenEntity);
				}

				byte[] bytes = getDumpContent(terminalSnapshot);

				writeFileContentIfDifferent(baseDir, entityName, count, bytes);

				terminalSession.doAction(TerminalActions.ENTER(), null);
			}

		} catch (OpenLegacyException e) {
			logger.info("Session completed:" + e.getMessage());
			System.exit(0);
		}

	}

	private void writeFileContentIfDifferent(File baseDir, String entityName, int count, byte[] bytes) throws IOException {
		String fileNameNoSuffix = entityName != null ? entityName : "screen" + count;

		File file = FileUtils.findNextAndDifferentFreeFile(baseDir, fileNameNoSuffix, getDumpFileExtension(), bytes);

		if (file != null) {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		}
	}

	protected abstract byte[] getDumpContent(TerminalSnapshot snapshot) throws Exception;

	protected abstract String getDumpFileExtension();

}
