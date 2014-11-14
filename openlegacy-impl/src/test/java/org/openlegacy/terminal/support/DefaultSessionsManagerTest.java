package org.openlegacy.terminal.support;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.SessionProperties;
import org.openlegacy.SessionPropertiesProvider;
import org.openlegacy.SessionsManager;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.support.SimpleSessionProperties;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("DefaultSessionsManagerTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultSessionsManagerTest {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private SessionsManager<TerminalSession> sessionsManager;

	@Ignore("Fails on Hudson only - not clear why")
	@Test
	public void testSessionManager() {

		Assert.assertEquals(0, sessionsManager.getSessionsProperties().size());

		TerminalSession session1 = applicationContext.getBean(TerminalSession.class);
		// verify connection opened
		session1.getSnapshot();

		Assert.assertEquals(1, sessionsManager.getSessionsProperties().size());

		TerminalSession session2 = applicationContext.getBean(TerminalSession.class);
		session2.getSnapshot();
		Assert.assertEquals(2, sessionsManager.getSessionsProperties().size());

		session1.disconnect();
		Assert.assertEquals(1, sessionsManager.getSessionsProperties().size());
		session2.disconnect();
		Assert.assertEquals(0, sessionsManager.getSessionsProperties().size());
	}

	@Test
	public void testSessionProperties() {

		DefaultTerminalSession session1 = (DefaultTerminalSession)applicationContext.getBean(TerminalSession.class);
		session1.getSnapshot();
		Date beforeEnter = session1.getProperties().getLastActivity();
		session1.doAction(TerminalActions.ENTER());
		Date afterEnter = session1.getProperties().getLastActivity();

		Assert.assertTrue(session1.getProperties().getId().startsWith("some-session-id"));
		Assert.assertNotNull(beforeEnter);
		Assert.assertNotNull(afterEnter);
		Assert.assertTrue(afterEnter.compareTo(beforeEnter) > 0);
	}

	@Test
	public void testSessionTrail() {

		DefaultTerminalSession session1 = (DefaultTerminalSession)applicationContext.getBean(TerminalSession.class);

		session1.doAction(TerminalActions.ENTER());

		SessionTrail<Snapshot> trail = sessionsManager.getTrail(session1.getProperties().getId());

		Assert.assertEquals(3, trail.getSnapshots().size());
	}

	public static class TestSessionPropertiesProvider implements SessionPropertiesProvider {

		@Override
		public SessionProperties getSessionProperties() {
			SimpleSessionProperties properties = new SimpleSessionProperties();
			properties.setId("some-session-id" + System.currentTimeMillis());
			return properties;
		}

	}
}
