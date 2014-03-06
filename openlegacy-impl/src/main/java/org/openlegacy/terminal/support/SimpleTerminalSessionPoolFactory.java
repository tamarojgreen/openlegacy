/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.support.AbstractSessionPoolFactory;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.context.ApplicationContext;

import java.text.MessageFormat;

import javax.inject.Inject;

public class SimpleTerminalSessionPoolFactory extends AbstractSessionPoolFactory<TerminalSession, TerminalAction> {

	private static final Log logger = LogFactory.getLog(SimpleTerminalSessionPoolFactory.class);

	@Inject
	private ApplicationContext applicationContext;

	@Override
	protected void init() {

		if (keepAliveAction != null) {
			keepAliveThread = new Thread() {

				@Override
				public void run() {
					while (!stopKeepAlive) {
						try {
							logger.debug("Keep alive is sleeping");
							sleep(keepAliveInterval);
						} catch (InterruptedException e) {
							throw (new RuntimeException(e));
						}
						TerminalAction keepAliveAction1 = ReflectionUtil.newInstance(keepAliveAction);

						TerminalSession[] sessions = blockingQueue.toArray(new TerminalSession[blockingQueue.size()]);

						logger.debug(sessions.length + " sessions found in queue during keep alive");
						for (TerminalSession session : sessions) {
							if (!actives.contains(session)) {
								try {
									session.doAction(keepAliveAction1);
									logger.debug("Keep alive action " + keepAliveAction.getSimpleName() + " performed for "
											+ session);
								} catch (Exception e) {
									logger.fatal(e, e);
								}
							}
						}
					}
				}

			};
			keepAliveThread.start();
		}
	}

	@Override
	protected void initSession() {
		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		logger.debug(MessageFormat.format("New session {0} created for pool", terminalSession));
		if (initAction != null) {
			ReflectionUtil.newInstance(initAction).perform(terminalSession, null);
			logger.debug(MessageFormat.format("New session {0} init action {1} performed", terminalSession,
					initAction.getSimpleName()));
		}
		try {
			blockingQueue.put(terminalSession);
			logger.debug("New session " + terminalSession + " added to blocking queue");
		} catch (InterruptedException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

}
