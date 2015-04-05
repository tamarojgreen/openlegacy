package tests;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.openlegacy.ws.openlegacy.Items;  

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoSessionTest {

	@Inject
	private TerminalSession terminalSession;
	
	@Test
	public void testSession(){

		 Items items = terminalSession.getEntity(Items.class);
				
	}
}
