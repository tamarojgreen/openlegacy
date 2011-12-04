package org.openlegacy.terminal.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.TypesUtil;

import java.text.MessageFormat;

import javax.inject.Inject;

public class ScreenEntityMethodInterceptor implements MethodInterceptor {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private TerminalSession terminalSession;

	private final static Log logger = LogFactory.getLog(ScreenEntityMethodInterceptor.class);

	private Object handleChildScreenGetter(MethodInvocation invocation, Class<?> childScreenEntityClass) throws Exception,
			NoSuchFieldException, Throwable, IllegalAccessException, InstantiationException {

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

		Object childScreenEntity = terminalSession.getEntity(childScreenEntityClass);
		logger.info(MessageFormat.format("Collected child screen for class {1}", childScreenEntityClass));

		fieldAccessor.setFieldValue(fieldName, childScreenEntity);
		return childScreenEntity;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {

		Class<?> returnType = invocation.getMethod().getReturnType();

		// exit if return type is primitive
		if (TypesUtil.isPrimitive(returnType)) {
			return invocation.proceed();
		}

		// if return type is in the registry - handle child entity fetching
		ScreenEntityDefinition childScreenEntityDefinition = screenEntitiesRegistry.get(returnType);
		if (childScreenEntityDefinition != null) {
			return handleChildScreenGetter(invocation, returnType);
		}
		return invocation.proceed();
	}

	public void setTerminalSession(TerminalSession terminalSession) {
		this.terminalSession = terminalSession;
	}
}
