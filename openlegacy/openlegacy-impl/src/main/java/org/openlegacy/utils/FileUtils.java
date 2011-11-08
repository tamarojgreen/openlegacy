package org.openlegacy.utils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

public class FileUtils {

	/**
	 * @param content
	 * @return a file name which doesn't exists. null if file content was matched by one of the scanned files
	 * @throws IOException
	 */
	public static File findNextAndDifferentFreeFile(File baseDir, String fileNameNoSuffix, String fileExtension, String content)
			throws IOException {
		File file = null;
		String fileSuffix = "";
		int filesCount = 1;
		do {
			String fileName = MessageFormat.format("{0}{1}.{2}", fileNameNoSuffix, fileSuffix, fileExtension);
			file = new File(baseDir, fileName);

			if (file.exists()) {
				String fileContent = org.apache.commons.io.FileUtils.readFileToString(file);
				if (fileContent.equals(content)) {
					return null;
				}

				fileSuffix = "_" + filesCount++;
			}
		} while (file.exists());

		file.getParentFile().mkdirs();
		return file;
	}
}
