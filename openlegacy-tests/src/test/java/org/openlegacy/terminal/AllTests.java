package org.openlegacy.terminal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.applinx.ApxSuite;
import org.openlegacy.recognizers.RecognizersSuite;
import org.openlegacy.terminal.modules.login.LoginModuleImplTest;
import org.openlegacy.terminal.modules.trail.UnifiedTerminalTrailTest;
import org.openlegacy.terminal.support.DefaultTerminalSessionTest;

@RunWith(Suite.class)
@SuiteClasses({ SimpleScreenEntityBinderTest.class, RecognizersSuite.class, ApxSuite.class, UnifiedTerminalTrailTest.class,
		LoginModuleImplTest.class, DefaultTerminalSessionTest.class })
public class AllTests {

}
