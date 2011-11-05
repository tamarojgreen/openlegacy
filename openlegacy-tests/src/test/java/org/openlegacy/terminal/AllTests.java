package org.openlegacy.terminal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.applinx.ApxSuite;
import org.openlegacy.recognizers.RecognizersSuite;
import org.openlegacy.terminal.mock.MockTerminalConnectionTest;
import org.openlegacy.terminal.modules.login.DefaultLoginModuleFailedTest;
import org.openlegacy.terminal.modules.login.DefaultLoginModuleTest;
import org.openlegacy.terminal.modules.trail.XmlTrailWriterTest;
import org.openlegacy.terminal.support.DefaultSessionNavigatorTest;
import org.openlegacy.terminal.support.DefaultTerminalSessionTest;

@RunWith(Suite.class)
@SuiteClasses({ SimpleScreenEntityBinderTest.class, RecognizersSuite.class, ApxSuite.class, XmlTrailWriterTest.class,
		DefaultLoginModuleTest.class, DefaultLoginModuleFailedTest.class, DefaultTerminalSessionTest.class,
		DefaultSessionNavigatorTest.class, MockTerminalConnectionTest.class })
public class AllTests {

}
