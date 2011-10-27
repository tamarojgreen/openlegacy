package org.openlegacy.adapter.terminal.trail;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.trail.TerminalIncomingTrailStage;
import org.openlegacy.terminal.trail.TerminalOutgoingTrailStage;
import org.openlegacy.trail.SessionTrail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SessionTrailAspect {

	@Autowired
	private SessionTrail sessionTrail;

	@Before("execution(* org.openlegacy.terminal.TerminalConnection.doAction(..))")
	public void logBefore(JoinPoint joinPoint) {
		TerminalConnection terminalConnection = (TerminalConnection)joinPoint.getTarget();

		Object[] args = joinPoint.getArgs();

		sessionTrail.appendStage(new TerminalOutgoingTrailStage(terminalConnection.getSnapshot(), (TerminalSendAction)args[0]));

	}

	@After("execution(* org.openlegacy.terminal.TerminalConnection.doAction(..))")
	public void logAfter(JoinPoint joinPoint) {
		TerminalConnection terminalConnection = (TerminalConnection)joinPoint.getTarget();
		sessionTrail.appendStage(new TerminalIncomingTrailStage(terminalConnection.getSnapshot()));

	}

}
