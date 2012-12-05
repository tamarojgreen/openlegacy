package tests;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.as400.menus.ChangePassword;
import org.openlegacy.as400.menus.ChangeProfilechgprf;
import org.openlegacy.as400.menus.CopyFromPcDocumentcpyfrmpcd;
import org.openlegacy.as400.menus.DecisionSupport;
import org.openlegacy.as400.menus.DisplayJobStatusAttributes;
import org.openlegacy.as400.menus.DisplayMessages;
import org.openlegacy.as400.menus.EditLibraryList;
import org.openlegacy.as400.menus.IbmIAccessTasks;
import org.openlegacy.as400.menus.IbmIMainMenu;
import org.openlegacy.as400.menus.InteractiveDataDefinitionUtility;
import org.openlegacy.as400.menus.OfficeTasks;
import org.openlegacy.as400.menus.ProgrammerMenu;
import org.openlegacy.as400.menus.Programming;
import org.openlegacy.as400.menus.ProgrammingDevelopmentManagerpdm;
import org.openlegacy.as400.menus.SelectDefinitionType;
import org.openlegacy.as400.menus.SendMessagesndmsg;
import org.openlegacy.as400.menus.SignOn;
import org.openlegacy.as400.menus.SpecifyLibrariesToWorkWith;
import org.openlegacy.as400.menus.SubmitJobsbmjob;
import org.openlegacy.as400.menus.UserTasks;
import org.openlegacy.as400.menus.WorkWithJob;
import org.openlegacy.as400.menus.WorkWithMessages;
import org.openlegacy.as400.menus.WorkWithPrinterOutput;
import org.openlegacy.as400.menus.WorkWithSubmittedJobs;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AS400_menusTest {

	@Inject
	private TerminalSession terminalSession;
	
	@Test
	public void testSession(){

				
	}
}
