package org.openlegacy.utils;

import java.io.File;
import java.text.MessageFormat;

public class FileUtils {

	public static File findFreeFileName(File baseDir, String fileNameNoSuffix, String fileExtension) {
		File file = null;
		String fileSuffix = "";
		int filesCount = 1;
		do {
			String fileName = MessageFormat.format("{0}{1}.{2}", fileNameNoSuffix, fileSuffix, fileExtension);
			file = new File(baseDir, fileName);
			if (file.exists()) {
				fileSuffix = "_" + filesCount++;
			}
		} while (file.exists());

		file.getParentFile().mkdirs();
		return file;
	}
}
