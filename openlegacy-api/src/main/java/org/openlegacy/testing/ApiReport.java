package org.openlegacy.testing;

import org.openlegacy.testing.TestResult.TestStatus;

import java.util.ArrayList;
import java.util.List;

public class ApiReport {

	private List<TestResult> testsResults = new ArrayList<TestResult>();

	public List<TestResult> getTestsResults() {
		return testsResults;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (TestResult testResult : testsResults) {
			sb.append(testResult);
			sb.append("\n");
		}
		return sb.toString();
	}

	public boolean hasFailures() {
		for (TestResult testResult : testsResults) {
			if (testResult.getTestStatus() == TestStatus.FAIL) {
				return true;
			}
		}
		return false;
	}
}
