package org.openlegacy.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

	private static int BUFFER_SIZE = 2048;

	public static void unzip(String zipFileName, String targetPath) throws IOException {

		ZipFile zipFile = new ZipFile(zipFileName);

		Enumeration<? extends ZipEntry> entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();

			if (entry.isDirectory()) {
				(new File(targetPath, entry.getName())).mkdir();
			}

			File outputFile = new File(targetPath, entry.getName());

			File outputParentFile = new File(outputFile.getParent());
			outputParentFile.mkdirs();
			if (!entry.isDirectory()) {
				unzipStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(outputFile)));
			}
		}

		zipFile.close();
	}

	private static final void unzipStream(InputStream in, OutputStream out) throws IOException {
		int len;
		byte[] buffer = new byte[BUFFER_SIZE];

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}
}
