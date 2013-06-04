package org.openlegacy.designtime;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.designtime.analyzer.SnapshotsSorterTest;
import org.openlegacy.designtime.rpc.generators.RpcEntityAjGeneratorTest;
import org.openlegacy.designtime.terminal.analyzer.support.AnalyzerSuite;
import org.openlegacy.designtime.terminal.generators.DefaultTerminalSnapshotsSorterTest;
import org.openlegacy.designtime.terminal.generators.HelpGeneratorTest;
import org.openlegacy.designtime.terminal.generators.ScreenEntityAjGeneratorTest;
import org.openlegacy.designtime.terminal.generators.ScreenEntityMvcGeneratorTest;
import org.openlegacy.designtime.terminal.generators.TrailJunitGeneratorTest;

@RunWith(Suite.class)
@SuiteClasses({ AnalyzerSuite.class, ScreenEntityAjGeneratorTest.class, RpcEntityAjGeneratorTest.class,
		DefaultTerminalSnapshotsSorterTest.class, SnapshotsSorterTest.class, TrailJunitGeneratorTest.class,
		ScreenEntityMvcGeneratorTest.class, HelpGeneratorTest.class })
public class OpenLegacyDesigntimeSuite {

}
