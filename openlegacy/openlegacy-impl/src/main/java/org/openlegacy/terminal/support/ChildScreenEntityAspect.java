package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenEntityFieldAccessor;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.TypesUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import javax.inject.Inject;

@Component
// to be synchronize with terminalSession which is also in session scope
@Scope("session")
@Aspect
public class ChildScreenEntityAspect {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private TerminalSession terminalSession;

	private final static Log logger = LogFactory.getLog(ChildScreenEntityAspect.class);

	@Around("execution(* *.get*(..)) && target(org.openlegacy.terminal.ScreenEntity)")
	public Object handleScreenEntityGetters(ProceedingJoinPoint joinPoint) throws Throwable {

		Class<?> returnType = null;

		// method doesn't match
		if (!(joinPoint.getSignature() instanceof MethodSignature)) {
			return joinPoint.proceed();
		}

		returnType = ((MethodSignature)joinPoint.getSignature()).getReturnType();

		// exit if return type is primitive
		if (TypesUtil.isPrimitive(returnType)) {
			return joinPoint.proceed();
		}

		// if return type is in the registry - handle child entity fetching
		ScreenEntityDefinition childScreenEntityDefinition = screenEntitiesRegistry.get(returnType);
		if (childScreenEntityDefinition != null) {
			return handleChildScreenGetter(joinPoint, returnType);
		}

		return joinPoint.proceed();
	}

	private Object handleChildScreenGetter(ProceedingJoinPoint joinPoint, Class<?> childScreenEntityClass) throws Exception,
			NoSuchFieldException, Throwable, IllegalAccessException, InstantiationException {

		String fieldName = PropertyUtil.getPropertyNameIfGetter(joinPoint.getSignature().getName());

		Object target = joinPoint.getTarget();
		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(target);

		if (!fieldAccessor.isExists(fieldName)) {
			return joinPoint.proceed();
		}

		// if value already been set - continue

		if (fieldAccessor.getFieldValue(fieldName) != null) {
			return joinPoint.proceed();
		}

		Object childScreenEntity = terminalSession.getEntity(childScreenEntityClass);
		logger.info(MessageFormat.format("Collected child screen for class {1}", childScreenEntityClass));

		fieldAccessor.setFieldValue(fieldName, childScreenEntity);
		return childScreenEntity;
	}

}
