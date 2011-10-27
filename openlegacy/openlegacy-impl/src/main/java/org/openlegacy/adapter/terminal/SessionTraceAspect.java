package org.openlegacy.adapter.terminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.openlegacy.terminal.TerminalConnection;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SessionTraceAspect {

	private final static Log logger = LogFactory.getLog(SessionTraceAspect.class);

	@Before("execution(* org.openlegacy.terminal.TerminalConnection.doAction(..))")
	public void logBefore(JoinPoint joinPoint) {
		TerminalConnection terminalConnection = (TerminalConnection)joinPoint.getTarget();

		if (logger.isTraceEnabled()) {
			logger.trace("\n\n******* Screen before: ******* \n\n" + terminalConnection.getSnapshot());
		}

	}

	@After("execution(* org.openlegacy.terminal.TerminalConnection.doAction(..))")
	public void logAfter(JoinPoint joinPoint) {
		TerminalConnection terminalConnection = (TerminalConnection)joinPoint.getTarget();
		if (logger.isTraceEnabled()) {
			logger.trace("\n\n******* Screen after: ******* \n\n" + terminalConnection.getSnapshot());
		}

	}

}
