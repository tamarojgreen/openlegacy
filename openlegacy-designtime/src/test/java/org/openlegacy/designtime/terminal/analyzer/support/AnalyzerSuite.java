package org.openlegacy.designtime.terminal.analyzer.support;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DemoSessionAnalyzerTest.class, DefaultTerminalSnapshotsAnalyzerTest.class,
		DefaultTerminalSnapshotsAnalyzerBidiTest.class, DefaultTerminalSnapshotsAnalyzerInventoryTest.class,
		DefaultTerminalSnapshotsMainframeTest.class, AS400MenusTest.class })
public class AnalyzerSuite {

}
