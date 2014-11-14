/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.tools;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.ScreensRecognizer;
import org.openlegacy.utils.FileCommand;
import org.openlegacy.utils.FileCommandExecuter;
import org.openlegacy.utils.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTerminalSessionSnapshotsDumper implements TerminalSessionSnapshotsDumper {

	private final static Log logger = LogFactory.getLog(AbstractTerminalSessionSnapshotsDumper.class);
	protected static final String OUTGOING_FILE_INDICATOR = "-out";

	private List<TerminalSnapshotDumper> dumpers = new ArrayList<TerminalSnapshotDumper>();

	@Override
	public void dumpSession(File baseDir, boolean cleanupFolder, String springContext) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springContext);

		ScreensRecognizer screensRecognizer = applicationContext.getBean(ScreensRecognizer.class);
		ScreenEntitiesRegistry screenEntitiesRegistry = applicationContext.getBean(ScreenEntitiesRegistry.class);

		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		terminalSession.getSnapshot();

		if (cleanupFolder) {
			FileCommandExecuter.execute(baseDir, new FileCommand() {

				@Override
				public void doCommand(File file) {
					file.delete();
				}

				@Override
				public boolean accept(File file) {
					String ext = FilenameUtils.getExtension(file.getName());
					for (TerminalSnapshotDumper terminalSnapshotDumper : dumpers) {
						if (ext.equals(terminalSnapshotDumper.getDumpFileExtension())) {
							return true;
						}
					}
					return false;
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

				for (TerminalSnapshotDumper terminalSnapshotDumper : dumpers) {
					byte[] bytes = terminalSnapshotDumper.getDumpContent(terminalSnapshot);
					writeFileContentIfDifferent(baseDir, entityName, count, bytes, terminalSnapshotDumper.getDumpFileExtension());
				}

				terminalSession.doAction(TerminalActions.ENTER(), null);
			}

		} catch (OpenLegacyRuntimeException e) {
			logger.info("Session completed:" + e.getMessage());
			return;
		}

	}

	private static void writeFileContentIfDifferent(File baseDir, String entityName, int count, byte[] bytes,
			String dumpFileExtension) throws IOException {
		String fileNameNoSuffix = entityName != null ? entityName : "screen" + count;

		File file = FileUtils.findNextAndDifferentFreeFile(baseDir, fileNameNoSuffix, dumpFileExtension, bytes);

		if (file != null) {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		}
	}

	public void setDumpers(List<TerminalSnapshotDumper> dumpers) {
		this.dumpers = dumpers;
	}
}
