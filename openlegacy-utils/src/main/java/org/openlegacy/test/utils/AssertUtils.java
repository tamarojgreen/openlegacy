package org.openlegacy.test.utils;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
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

	public static void assertImageContent(String expectedImageFileName, byte[] expectedContent, byte[] resultContent)
			throws IOException {
		if (!Arrays.equals(expectedContent, resultContent)) {

			writeTempImage(expectedContent, "ol_tests/expected.jpg");
			// copy the file as the file name for comfort copy back to the compared resource
			writeTempImage(resultContent, "ol_tests/" + expectedImageFileName);

			File tempDir = SystemUtils.getJavaIoTmpDir();

			String message = MessageFormat.format(
					"Image doesn''t match expected image. see: {0}/ol_tests. If the resulting image is OK, replace it back as the original resource",
					tempDir.getAbsolutePath());
			logger.info(message);
			throw new RuntimeException(message);

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
		try {
			return new String(expectedBytes, CharEncoding.UTF_8).replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getTestMethodName() {
		StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
		String methodName = null;
		for (StackTraceElement stackTraceElement : stackElements) {
			String clsName = stackTraceElement.getClassName();
			methodName = stackTraceElement.getMethodName();
			try {
				Class<?> cls = Class.forName(clsName);
				Method method = cls.getMethod(methodName);
				Test test = method.getAnnotation(Test.class);
				if (test != null) {
					methodName = method.getName();
					break;
				}
			} catch (Exception ex) {
				// do nothing
			}
		}
		return methodName;
	}

}
