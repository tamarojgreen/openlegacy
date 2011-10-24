package org.openlegacy.utils;

import java.io.File;

public class FileCommandExecuter {

	public static void execute(File root, FileCommand command) {
		File[] files = root.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (!command.accept(file)) {
				continue;
			}

			if (file.isDirectory()) {
				execute(file, command);
			} else {
				command.doCommand(file);
			}
		}
	}
}