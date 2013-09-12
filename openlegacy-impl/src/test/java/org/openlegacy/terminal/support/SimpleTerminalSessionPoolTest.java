package org.openlegacy.terminal.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSession;
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

	@Inject
	private SimpleTerminalSessionPoolFactory terminalSessionPool;

	@Test
	public void testPool() {
		Assert.assertEquals(0, terminalSessionPool.getActives().size());
		final TerminalSession terminalSession1 = applicationContext.getBean(TerminalSession.class);
		Assert.assertEquals(1, terminalSessionPool.getActives().size());
		@SuppressWarnings("unused")
		TerminalSession terminalSession2 = applicationContext.getBean(TerminalSession.class);
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
		TerminalSession terminalSession3 = applicationContext.getBean(TerminalSession.class);
		long after = System.currentTimeMillis();

		Assert.assertTrue((after - before) >= 1000);
	}
}
