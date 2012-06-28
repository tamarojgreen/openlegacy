package org.openlegacy.test.utils;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

public class AssertUtils {

	private final static Log logger = LogFactory.getLog(AssertUtils.class);

	public static void assertContent(byte[] expectedContent, byte[] resultContent) {
		if (!initTestString(expectedContent).equals(initTestString(resultContent))) {
			logger.info("Expected content:\n" + new String(expectedContent));
			logger.info("\nResult content:");
			logger.info("************\n" + new String(resultContent) + "\n************");
			logger.info("*** NOTE: If the result is correct copy the above result block into expected result file so test can pass next time\n");
			throw new RuntimeException("Generated content don't match. See log");

		}
	}

	public static void assertImageContent(byte[] expectedContent, byte[] resultContent) throws IOException {
		if (!Arrays.equals(expectedContent, resultContent)) {

			writeTempImage(expectedContent, "ol_tests/expected.jpg");
			writeTempImage(resultContent, "ol_tests/result.jpg");

			File tempDir = SystemUtils.getJavaIoTmpDir();
			logger.info(MessageFormat.format("*** Result screen doesn''t match expected screen. see: {0}/ol_tests", tempDir));

			throw new RuntimeException("Generated content don't match. See log");

		}
	}

	private static void writeTempImage(byte[] content, String fileName) throws FileNotFoundException, IOException {
		File tempDir = SystemUtils.getJavaIoTmpDir();

		File file = new File(tempDir, fileName);
		file.getParentFile().mkdirs();
		FileOutputStream image = new FileOutputStream(file);
		image.write(content);
		image.close();
	}

	private static String initTestString(byte[] expectedBytes) {
		return new String(expectedBytes).replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "");
	}

}
