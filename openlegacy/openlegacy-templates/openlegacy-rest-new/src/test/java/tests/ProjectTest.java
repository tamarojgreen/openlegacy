package tests;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.support.trail.AbstractSessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailUtil;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.testing.ApiReport;
import org.openlegacy.testing.ApiTester;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;

import javax.inject.Inject;

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectTest {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private TrailUtil trailUtil;

	@Inject
	private ApiTester<TerminalSession> apiTester;

	@Ignore
	@Test
	public void testProject() throws RegistryException, LoginException, FileNotFoundException {
		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		AbstractSessionTrail<?> sessionTrail = (AbstractSessionTrail<?>)terminalSession.getModule(Trail.class).getSessionTrail();
		sessionTrail.setHistoryCount(null);

		ApiReport testReport = null;
		try {

			terminalSession.getModule(Login.class).login("user", "pwd");

			testReport = apiTester.test(terminalSession);

			// example of how to navigate to certain entity
			// Items items = terminalSession.getEntity(Items.class);

			// example of how to navigate to certain entity with key
			// ItemDetails itemDetails = terminalSession.getEntity(ItemDetails.class,123);

			// example of how to update field
			// itemDetails.setDescription("ABC");

			// example of how to perform an action on an entity with modified field
			// terminalSession.doAction(TerminalActions.ENTER(), (ScreenEntity)items);

		} finally {
			trailUtil.saveTestTrail(terminalSession);
			try {
				terminalSession.disconnect();
			} catch (Exception e) {
			}
			if (testReport.hasFailures()) {
				Assert.fail(testReport.toString());
			}
			System.out.println(testReport.toString());
		}

	}
}
