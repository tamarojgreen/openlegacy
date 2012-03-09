package org.openlegacy.terminal.support.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.TypesUtil;

import java.util.List;

public class ScreenEntityMethodInterceptor implements MethodInterceptor {

	private TerminalSession terminalSession;

	private List<ScreenEntityProxyHandler> proxyHandlers;

	public Object invoke(MethodInvocation invocation) throws Throwable {

		Class<?> returnType = invocation.getMethod().getReturnType();

		// exit if return type is primitive
		if (TypesUtil.isPrimitive(returnType)) {
			return invocation.proceed();
		}

		String fieldName = PropertyUtil.getPropertyNameIfGetter(invocation.getMethod().getName());

		Object target = invocation.getThis();
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(target);

		if (!fieldAccessor.isExists(fieldName)) {
			return invocation.proceed();
		}

		// if value already been set - continue

		if (fieldAccessor.getFieldValue(fieldName) != null) {
			return invocation.proceed();
		}

		for (ScreenEntityProxyHandler screenEntityProxyHandler : proxyHandlers) {
			Object result = screenEntityProxyHandler.invoke(terminalSession, invocation);
			if (result != null) {
				fieldAccessor.setFieldValue(fieldName, result);
				break;
			}
		}

		return invocation.proceed();
	}

	public void setTerminalSession(TerminalSession terminalSession) {
		this.terminalSession = terminalSession;
	}

	public void setProxyHandlers(List<ScreenEntityProxyHandler> proxyHandlers) {
		this.proxyHandlers = proxyHandlers;
	}
}
