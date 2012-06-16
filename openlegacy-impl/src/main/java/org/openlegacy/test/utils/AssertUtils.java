package org.openlegacy.test.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

	private static String initTestString(byte[] expectedBytes) {
		return new String(expectedBytes).replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "");
	}

}
