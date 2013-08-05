/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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

	public static String fileWithoutExtension(String filename) {
		if (!filename.contains(".")) {
			return filename;
		}
		filename = new File(filename).getName();
		return filename.substring(0, filename.lastIndexOf("."));
	}

	public static String fileWithoutAnyExtension(String filename) {
		if (!filename.contains(".")) {
			return filename;
		}
		filename = new File(filename).getName();
		return filename.substring(0, filename.indexOf("."));
	}
	
	public static String fileExtension(String filename) {
		if (!filename.contains(".")) {
			return "";
		}
		filename = new File(filename).getName();
		return filename.substring(filename.lastIndexOf("."), filename.length());
	}

	public static File extractToTempDir(URL resource, String fileName) throws IOException {
		File tempDir = SystemUtils.getJavaIoTmpDir();
		File file = new File(tempDir, fileName);
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			IOUtils.copy(resource.openStream(), output);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return file;
	}

	/**
	 * Deletes the file if it's empty
	 * 
	 * @param file
	 */
	public static void deleteEmptyFile(File file) {
		if (file == null) {
			return;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] bytes = IOUtils.toByteArray(fis);
			if (bytes.length == 0) {
				org.apache.commons.io.FileUtils.deleteQuietly(file);
			}
		} catch (IOException e) {
			// do nothing
		}
	}
}
