package org.openlegacy.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class FileUtils {

	/**
	 * @param content
	 * @return a file name which doesn't exists. null if file content was matched by one of the scanned files
	 * @throws IOException
	 */
	public static File findNextAndDifferentFreeFile(File baseDir, String fileNameNoSuffix, String fileExtension, byte[] content)
			throws IOException {
		File file = null;
		String fileSuffix = "";
		int filesCount = 1;
		do {
			String fileName = MessageFormat.format("{0}{1}.{2}", fileNameNoSuffix, fileSuffix, fileExtension);
			file = new File(baseDir, fileName);

			if (file.exists()) {
				byte[] fileContent = org.apache.commons.io.FileUtils.readFileToByteArray(file);
				if (Arrays.equals(fileContent, content)) {
					return null;
				}

				fileSuffix = "_" + filesCount++;
			}
		} while (file.exists());

		file.getParentFile().mkdirs();
		return file;
	}

	public static void copyAndReplace(InputStream inputStream, OutputStream outputStream, Map<String, String> keysValues)
			throws IOException {
		String content = IOUtils.toString(inputStream);

		if (keysValues != null) {
			Set<String> keys = keysValues.keySet();
			for (String key : keys) {
				content = content.replaceAll(key, keysValues.get(key));
			}
		}
		outputStream.write(content.getBytes());
	}

	public static String fileWithoutExtenstion(String filename) {
		filename = new File(filename).getName();
		return filename.substring(0, filename.lastIndexOf("."));
	}
}
