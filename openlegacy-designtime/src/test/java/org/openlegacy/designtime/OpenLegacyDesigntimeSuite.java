package org.openlegacy.designtime;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.designtime.analyzer.SnapshotsSorterTest;
import org.openlegacy.designtime.generators.DefaultTerminalSnapshotsSorterTest;
import org.openlegacy.designtime.generators.HelpGeneratorTest;
import org.openlegacy.designtime.generators.ScreenEntityAjGeneratorTest;
import org.openlegacy.designtime.generators.ScreenEntityMvcGeneratorTest;
import org.openlegacy.designtime.generators.TrailJunitGeneratorTest;
import org.openlegacy.designtime.terminal.analyzer.support.AnalyzerSuite;

@RunWith(Suite.class)
@SuiteClasses({ ScreenEntityAjGeneratorTest.class, DefaultTerminalSnapshotsSorterTest.class, SnapshotsSorterTest.class,
		TrailJunitGeneratorTest.class, AnalyzerSuite.class, ScreenEntityMvcGeneratorTest.class, HelpGeneratorTest.class })
public class OpenLegacyDesigntimeSuite {

}
