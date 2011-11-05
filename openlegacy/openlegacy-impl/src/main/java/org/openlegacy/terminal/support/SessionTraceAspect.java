package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.ScreenDisplayUtils;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SessionTraceAspect {

	private final static Log logger = LogFactory.getLog(SessionTraceAspect.class);

	@Before("execution(* org.openlegacy.terminal.TerminalConnection.doAction(..))  && target(terminalConnection)")
	public void logBefore(JoinPoint joinPoint, TerminalConnection terminalConnection) {
		if (logger.isTraceEnabled()) {
			logger.trace("\n\n******* Screen before ([ abc ] indicates an input field): ******* \n\n"
					+ terminalConnection.getSnapshot());
		}

	}

	@After("execution(* org.openlegacy.terminal.TerminalConnection.doAction(..)) && target(terminalConnection) && args(terminalSendAction)")
	public void logAfter(JoinPoint joinPoint, TerminalConnection terminalConnection, TerminalSendAction terminalSendAction) {
		if (logger.isTraceEnabled()) {
			logger.trace("\n\n******* Screen after (* abc * indicates a modified field): ******* \n\n"
					+ ScreenDisplayUtils.toString(terminalConnection.getSnapshot(), terminalSendAction, true));
		}

	}
}
