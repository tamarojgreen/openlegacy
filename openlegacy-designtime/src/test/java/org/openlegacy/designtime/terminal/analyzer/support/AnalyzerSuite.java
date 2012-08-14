package org.openlegacy.designtime.terminal.analyzer.support;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DefaultTerminalSnapshotsAnalyzerTest.class, DefaultTerminalSnapshotsAnalyzerBidiTest.class,
		DefaultTerminalSnapshotsAnalyzerInventoryTest.class, DefaultTerminalSnapshotsMainframeTest.class })
public class AnalyzerSuite {

}
