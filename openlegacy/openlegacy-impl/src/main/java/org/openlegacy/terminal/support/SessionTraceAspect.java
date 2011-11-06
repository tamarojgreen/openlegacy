package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.ScreenDisplayUtils;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SessionTraceAspect {

	private final static Log logger = LogFactory.getLog(SessionTraceAspect.class);

	@Before("execution(* org.openlegacy.terminal.TerminalConnection.doAction(..))  && target(terminalConnection) && args(terminalSendAction)")
	public void logBefore(JoinPoint joinPoint, TerminalConnection terminalConnection, TerminalSendAction terminalSendAction) {
		if (logger.isTraceEnabled()) {
			TerminalScreen snapshot = terminalConnection.getSnapshot();
			logger.trace("\n\n******* Screen before\n(* abc * indicates an modified field, [ abc ] indicates a input field): ******* \n\n"
					+ ScreenDisplayUtils.toString(snapshot, terminalSendAction, true));
		}

	}

	@After("execution(* org.openlegacy.terminal.TerminalConnection.doAction(..)) && target(terminalConnection)")
	public void logAfter(JoinPoint joinPoint, TerminalConnection terminalConnection) {
		if (logger.isTraceEnabled()) {
			try {
				logger.trace("\n\n******* Screen after ([ abc ] indicates a input field): ******* \n\n"
						+ terminalConnection.getSnapshot());
			} catch (SessionEndedException e) {
				// ignore
			}
		}

	}
}
