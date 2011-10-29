package org.openlegacy.adapter.terminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.ChildScreenDefinition;
import org.openlegacy.terminal.TerminalSession;

import java.lang.reflect.Field;
import java.text.MessageFormat;

public class ScreenBindUtil {

	private final static Log logger = LogFactory.getLog(ScreenBindUtil.class);

	public static Object populateChildScreenField(TerminalSession terminalSession, Class<?> screenEnity, Object target,
			Field declaredField, ChildScreenDefinition childScreenDefinition) throws InstantiationException,
			IllegalAccessException {

		terminalSession.doAction(childScreenDefinition.getStepInto().newInstance(), null);
		Object returnTypeInstance = terminalSession.getEntity(screenEnity);

		logger.info(MessageFormat.format("Collecyed screen to field {0} in class {1}", declaredField.getName(), screenEnity));

		declaredField.set(target, returnTypeInstance);
		return returnTypeInstance;
	}
}
