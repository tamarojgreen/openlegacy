package org.openlegacy.designtime.rpc.source.parsers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

public class CobolParserUtils {

	public static String createTmpDir(String initial) {
		boolean needToFind = true;
		String sysTempDir = FileUtils.getTempDirectoryPath();
		File tempDir;
		String randomDirName = null;
		while (needToFind) {
			randomDirName = sysTempDir + initial + ((Double)Math.random()).toString();
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

	public static void copyStreamsToFile(String path, Map<String, InputStream> streamMap) throws IOException {
		for (String copyBookName : streamMap.keySet()) {
			File copyBookFile = new File(path + copyBookName);
			OutputStream copyBookStream = new FileOutputStream(copyBookFile);

			copyBookFile.deleteOnExit();

			IOUtils.copy(streamMap.get(copyBookName), copyBookStream);
			copyBookStream.close();
		}
	}
}
