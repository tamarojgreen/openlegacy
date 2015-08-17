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
package org.openlegacy.utils;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
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

	public static byte[] compress(byte[] data) {
		byte[] result = null;
		if (data == null) {
			return result;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream zip = new GZIPOutputStream(baos);
			zip.write(data);
			zip.close();
			result = baos.toByteArray();
			baos.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static byte[] decompress(byte[] data, int size) {
		byte[] result = null;
		if (data == null || size == 0) {
			return null;
		}
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			GZIPInputStream zip = new GZIPInputStream(bais);
			result = new byte[size];
			zip.read(result);
			bais.close();
			zip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
