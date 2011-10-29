package org.openlegacy.adapter.terminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.openlegacy.terminal.ChildScreenDefinition;
import org.openlegacy.terminal.ChildScreensDefinitionProvider;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.util.ProxyUtil;
import org.openlegacy.util.TypesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
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

	@Around("execution(* *.fetch*(..))")
	public Object captureChildScreens(ProceedingJoinPoint joinPoint) throws Throwable {

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

		// exit if return type is not in the registry
		if (screenEntitiesRegistry.get(returnType) == null) {
			return joinPoint.proceed();
		}

		String fieldName = joinPoint.getSignature().getName().substring(5); // 5- fetch
		fieldName = StringUtils.uncapitalize(fieldName);

		Object target = ProxyUtil.getTargetObject(joinPoint.getTarget(), returnType);

		Class<?> targetClass = ProxyUtil.getRealClass(target);

		Field declaredField = targetClass.getDeclaredField(fieldName);
		if (declaredField == null) {
			return joinPoint.proceed();
		}

		declaredField.setAccessible(true);

		// if value already been set - continue
		if (declaredField.get(target) != null) {
			return joinPoint.proceed();
		}

		ChildScreenDefinition childScreenDefinition = childScreenDefinitionProvider.getChildScreenDefinitions(targetClass,
				fieldName);

		if (childScreenDefinition == null) {
			logger.warn(MessageFormat.format("Child entity definitions not found for {0} in class {1}", returnType, targetClass));
			return joinPoint.proceed();
		}

		Object returnTypeInstance = ScreenBindUtil.populateChildScreenField(terminalSession, returnType, target, declaredField,
				childScreenDefinition);
		return returnTypeInstance;
	}

}
