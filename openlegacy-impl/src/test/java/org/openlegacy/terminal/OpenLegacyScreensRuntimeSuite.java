package org.openlegacy.terminal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.recognizers.RecognizersSuite;
import org.openlegacy.terminal.layout.DefaultBidiScreenPageBuilderTest;
import org.openlegacy.terminal.layout.DefaultScreenPageBuilderTest;
import org.openlegacy.terminal.mock_session.MockStateMachineTerminalConnection2Test;
import org.openlegacy.terminal.modules.login.DefaultLoginModuleFailedTest;
import org.openlegacy.terminal.modules.login.DefaultLoginModuleTest;
import org.openlegacy.terminal.modules.menu.DefaultTerminalMenuModuleTest;
import org.openlegacy.terminal.modules.messages.DefaultTerminalMessagesModuleTest;
import org.openlegacy.terminal.modules.navigation.DefaultNavigationModuleTest;
import org.openlegacy.terminal.modules.roles.DefaultTerminalRolesModuleTest;
import org.openlegacy.terminal.modules.table.DefaultTableDrilldownPerformerTest;
import org.openlegacy.terminal.modules.table.TableMultiLineTest;
import org.openlegacy.terminal.modules.trail.XmlTrailWriterTest;
import org.openlegacy.terminal.render.TerminalSnapshotImageRendererTest;
import org.openlegacy.terminal.support.DefaultScreenRecordsProviderTest;
import org.openlegacy.terminal.support.DefaultSessionNavigatorTest;
import org.openlegacy.terminal.support.DefaultSessionsManagerTest;
import org.openlegacy.terminal.support.DefaultTerminalSessionKeysTest;
import org.openlegacy.terminal.support.DefaultTerminalSessionTest;
import org.openlegacy.terminal.support.MultyLineFieldTest;
import org.openlegacy.terminal.support.SimpleTerminalSessionPoolTest;
import org.openlegacy.terminal.support.SnapshotUtilsTest;
import org.openlegacy.terminal.support.WindowTest;
import org.openlegacy.terminal.support.binders.BooleanFieldsBinderTest;
import org.openlegacy.terminal.support.binders.ConditionalFieldTest;
import org.openlegacy.terminal.support.binders.CustomBinderTest;
import org.openlegacy.terminal.support.binders.DateFieldsBinderTest;
import org.openlegacy.terminal.support.binders.ListFieldTest;
import org.openlegacy.terminal.support.binders.ScreenEntityFieldAttributeTest;
import org.openlegacy.terminal.support.binders.ScreenEntityTablesBinderTest;
import org.openlegacy.terminal.support.binders.ScreenExpressionFieldsBinderTest;
import org.openlegacy.terminal.support.obfuscator.TrailObfuscatorTest;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeTest;

@RunWith(Suite.class)
@SuiteClasses({ SnapshotUtilsTest.class, RegistryTest.class, TerminalEqualsHashcodeTest.class,
		MockTerminalSessionSystemTest.class, DefaultTerminalSessionKeysTest.class, RecognizersSuite.class,
		XmlTrailWriterTest.class, DefaultLoginModuleTest.class, DefaultLoginModuleFailedTest.class,
		DefaultTerminalMessagesModuleTest.class, DefaultTerminalSessionTest.class, WindowTest.class,
		DefaultSessionNavigatorTest.class, MockTerminalConnectionTest.class, MockTerminalSessionTest.class,
		ScreenEntityTablesBinderTest.class, TableMultiLineTest.class, DateFieldsBinderTest.class, BooleanFieldsBinderTest.class,
		DefaultNavigationModuleTest.class, DefaultTableDrilldownPerformerTest.class, DefaultTerminalMenuModuleTest.class,
		MockStateMachineTerminalConnection2Test.class, DefaultScreenPageBuilderTest.class,
		DefaultBidiScreenPageBuilderTest.class, DefaultScreenRecordsProviderTest.class, TerminalSnapshotImageRendererTest.class,
		CustomBinderTest.class, MultyLineFieldTest.class, DefaultSessionsManagerTest.class, ListFieldTest.class,
		ScreenEntityFieldAttributeTest.class, ConditionalFieldTest.class, ConditionalFieldTest.class,
		SimpleTerminalSessionPoolTest.class, ScreenExpressionFieldsBinderTest.class, TrailObfuscatorTest.class,
		DefaultTerminalRolesModuleTest.class })
public class OpenLegacyScreensRuntimeSuite {

}
