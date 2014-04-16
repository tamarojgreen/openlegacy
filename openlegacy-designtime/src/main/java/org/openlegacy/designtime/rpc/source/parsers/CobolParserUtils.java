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
package org.openlegacy.designtime.rpc.source.parsers;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Map;

public class CobolParserUtils {

	private static String LINE_START = "      ";
	private static int LINE_MIN_LENGTH = 6;
	private static String newLine = System.getProperty("line.separator");
	private static String lineSeperator = "\\r?\\n"; // covers both \r\n and \n

	public static String createTmpDir(String initial) {
		boolean needToFind = true;
		String sysTempDir = FileUtils.getTempDirectoryPath();
		File tempDir;
		String randomDirName = null;
		while (needToFind) {
			randomDirName = MessageFormat.format("{0}{1}{2}{3}", sysTempDir, File.separator, initial,
					((Double)Math.random()).toString());
			tempDir = new File(randomDirName);
			if (!tempDir.exists()) {
				needToFind = false;
				tempDir.mkdir();
				tempDir.deleteOnExit();

			}
		}
		return randomDirName + File.separator;
	}

	public static void replaceStringInFile(String oldstring, String newstring, String path, String fileName) throws IOException {

		String fullPathName = path + fileName;
		File orig = new File(fullPathName);
		File fp = new File(fullPathName + ".bk");
		orig.renameTo(fp);
		File out = new File(fullPathName);
		BufferedReader reader = new BufferedReader(new FileReader(fp));
		PrintWriter writer = new PrintWriter(new FileWriter(out));
		String line = null;
		while ((line = reader.readLine()) != null) {
			writer.println(line.replaceAll(oldstring, newstring));
		}
		reader.close();
		writer.close();
	}

	private static boolean isComment(String line) {

		for (int i = 6; i < line.length(); i++) {
			if (line.charAt(i) != ' ') {
				if (line.charAt(i) == '*') {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public static String preProcessLine(String line) {
		if (line.length() > LINE_MIN_LENGTH) {
			line = LINE_START + line.substring(6);
			if (isComment(line) == false) {
				return line + newLine;

			}
		}
		return null;
	}

	public static String removeCommentsAndLabels(String source) {
		String result = "";
		String[] lines = source.split(lineSeperator);
		for (String line : lines) {
			if ((line = preProcessLine(line)) != null) {
				result = result + line;
			}
		}

		return result;
	}

	public static void copyStreamsToFile(String path, Map<String, InputStream> streamMap) throws IOException {

		for (String copyBookName : streamMap.keySet()) {
			File copyBookFile = new File(path + copyBookName);
			OutputStream copyBookStream = new FileOutputStream(copyBookFile);
			OutputStreamWriter copyBookStreamWriter = new OutputStreamWriter(copyBookStream);

			copyBookFile.deleteOnExit();

			BufferedReader input = new BufferedReader(new InputStreamReader(streamMap.get(copyBookName)));
			String line;
			while ((line = input.readLine()) != null) {
				if ((line = preProcessLine(line)) != null) {
					copyBookStreamWriter.write(line);
				}

			}
			// IOUtils.copy(streamMap.get(copyBookName), copyBookStream);
			copyBookStreamWriter.close();
			copyBookStream.close();
		}
	}

	public static String writeToTempFile(String source, String extension) throws IOException {

		File tempFile = File.createTempFile("temp" + System.currentTimeMillis(), extension);

		tempFile.deleteOnExit();

		BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
		try {
			out.write(source);
		} finally {
			out.close();

		}
		return tempFile.getPath();
	}
}
