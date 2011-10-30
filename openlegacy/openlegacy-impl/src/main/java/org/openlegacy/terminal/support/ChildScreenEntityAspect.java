package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.openlegacy.terminal.ChildScreensDefinitionProvider;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ChildScreenDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.ScreenBindUtil;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.TypesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

@Component
// to be synchronize with terminalSession which is also in session scope
@Scope("session")
@Aspect
public class ChildScreenEntityAspect {

	@Autowired
	private ChildScreensDefinitionProvider childScreenDefinitionProvider;

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Autowired
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
		if (screenEntitiesRegistry.get(returnType) != null) {
			return handleChildScreenGetter(joinPoint, returnType);
		}

		return joinPoint.proceed();
	}

	private Object handleChildScreenGetter(ProceedingJoinPoint joinPoint, Class<?> returnType) throws Exception,
			NoSuchFieldException, Throwable, IllegalAccessException, InstantiationException {

		String fieldName = joinPoint.getSignature().getName().substring(PropertyUtil.GET.length());
		fieldName = StringUtils.uncapitalize(fieldName);

		Object target = joinPoint.getTarget();
		ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(target);

		if (!fieldAccessor.isReadableProperty(fieldName)) {
			return joinPoint.proceed();
		}

		// if value already been set - continue

		if (fieldAccessor.getFieldValue(fieldName) != null) {
			return joinPoint.proceed();
		}

		Class<? extends Object> targetClass = target.getClass();
		ChildScreenDefinition childScreenDefinition = childScreenDefinitionProvider.getChildScreenDefinitions(targetClass,
				fieldName);

		if (childScreenDefinition == null) {
			logger.warn(MessageFormat.format("Child entity definitions not found for {0} in class {1}", returnType, targetClass));
			return joinPoint.proceed();
		}

		Object returnTypeInstance = ScreenBindUtil.getChildScreen(terminalSession, returnType, childScreenDefinition);
		fieldAccessor.setFieldValue(fieldName, returnTypeInstance);
		return returnTypeInstance;
	}

}
