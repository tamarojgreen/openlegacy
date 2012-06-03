package org.openlegacy.terminal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.recognizers.RecognizersSuite;
import org.openlegacy.terminal.layout.DefaultScreenPageBuilderTest;
import org.openlegacy.terminal.mock_session.MockStateMachineTerminalConnectionTest;
import org.openlegacy.terminal.modules.login.DefaultLoginModuleFailedTest;
import org.openlegacy.terminal.modules.login.DefaultLoginModuleTest;
import org.openlegacy.terminal.modules.menu.DefaultTerminalMenuModuleTest;
import org.openlegacy.terminal.modules.messages.DefaultTerminalMessagesModuleTest;
import org.openlegacy.terminal.modules.navigation.DefaultNavigationModuleTest;
import org.openlegacy.terminal.modules.table.DefaultTableDrilldownPerformerTest;
import org.openlegacy.terminal.modules.trail.XmlTrailWriterTest;
import org.openlegacy.terminal.support.DefaultScreenRecordsProviderTest;
import org.openlegacy.terminal.support.DefaultSessionNavigatorTest;
import org.openlegacy.terminal.support.DefaultTerminalSessionTest;
import org.openlegacy.terminal.support.WindowTest;
import org.openlegacy.terminal.support.binders.DateFieldsBinderTest;
import org.openlegacy.terminal.support.binders.ScreenEntityTablesBinderTest;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeTest;

@RunWith(Suite.class)
@SuiteClasses({ RegistryTest.class, TerminalEqualsHashcodeTest.class, MockTerminalSessionSystemTest.class,
		RecognizersSuite.class, XmlTrailWriterTest.class, DefaultLoginModuleTest.class, DefaultLoginModuleFailedTest.class,
		DefaultTerminalMessagesModuleTest.class, DefaultTerminalSessionTest.class, WindowTest.class,
		DefaultSessionNavigatorTest.class, MockTerminalConnectionTest.class, MockTerminalSessionTest.class,
		ScreenEntityTablesBinderTest.class, DateFieldsBinderTest.class, DefaultNavigationModuleTest.class,
		DefaultTableDrilldownPerformerTest.class, DefaultTerminalMenuModuleTest.class,
		MockStateMachineTerminalConnectionTest.class, DefaultScreenPageBuilderTest.class, DefaultScreenRecordsProviderTest.class })
public class OpenLegacyRuntimeSuite {

}
