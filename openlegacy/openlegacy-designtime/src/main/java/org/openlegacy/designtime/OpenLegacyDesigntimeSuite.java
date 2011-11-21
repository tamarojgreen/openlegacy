package org.openlegacy.designtime;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.designtime.generators.DefaultTerminalSnapshotsSorterTest;
import org.openlegacy.designtime.generators.ScreenEntityAjGeneratorTest;

@RunWith(Suite.class)
@SuiteClasses({ ScreenEntityAjGeneratorTest.class, DefaultTerminalSnapshotsSorterTest.class })
public class OpenLegacyDesigntimeSuite {

}
