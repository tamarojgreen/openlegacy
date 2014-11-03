package org.openlegacy.terminal.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("DefaultTerminalSessionPoolTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleTerminalSessionPoolTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testPool() {
		final SimpleTerminalSessionPoolFactory terminalSessionPool = applicationContext.getBean(SimpleTerminalSessionPoolFactory.class);

		Assert.assertEquals(0, terminalSessionPool.getActives().size());
		final TerminalSession terminalSession1 = terminalSessionPool.getSession();
		terminalSession1.getSnapshot();
		Assert.assertEquals(1, terminalSessionPool.getActives().size());
		@SuppressWarnings("unused")
		TerminalSession terminalSession2 = terminalSessionPool.getSession();
		Assert.assertEquals(2, terminalSessionPool.getActives().size());

		Thread thread = new Thread() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw (new RuntimeException(e));
				}
				terminalSessionPool.returnSession(terminalSession1);
			};
		};
		thread.start();

		long before = System.currentTimeMillis();
		// should wait for run to end
		@SuppressWarnings("unused")
		TerminalSession terminalSession3 = terminalSessionPool.getSession();
		long after = System.currentTimeMillis();

		Assert.assertTrue((after - before) >= 1000);
	}

	@Test
	public void testKeepAlive() throws InterruptedException {
		SimpleTerminalSessionPoolFactory terminalSessionPool = applicationContext.getBean(SimpleTerminalSessionPoolFactory.class);
		TerminalSession session = terminalSessionPool.getSession();
		session.getSnapshot();
		Assert.assertFalse(CleanupDummyAction.isCalled());
		terminalSessionPool.returnSession(session);
		Thread.sleep(4000);
		Assert.assertTrue(CleanupDummyAction.isCalled());

	}

	public static class CleanupDummyAction implements TerminalAction {

		private static boolean called = false;

		@Override
		public void perform(TerminalSession session, Object entity, Object... keys) {
			called = true;
		}

		public static boolean isCalled() {
			return called;
		}

		@Override
		public boolean isMacro() {
			return false;
		}

	}
}
