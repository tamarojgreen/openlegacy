package org.openlegacy.testing;

import java.text.MessageFormat;

public class TestResult {

	public enum TestStatus {
		PASS,
		WARNING,
		FAIL
	}

	private String message;
	private TestStatus testStatus;

	public TestResult(TestStatus testStatus, String message) {
		this.testStatus = testStatus;
		this.message = message;
	}

	public TestStatus getTestStatus() {
		return testStatus;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0} - {1}", testStatus, message);
	}
}
