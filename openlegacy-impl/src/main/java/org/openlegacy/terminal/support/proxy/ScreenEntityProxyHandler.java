package org.openlegacy.terminal.support.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSession;

public interface ScreenEntityProxyHandler {

	Object invoke(TerminalSession terminalSession, MethodInvocation invocation) throws OpenLegacyRuntimeException;
}
