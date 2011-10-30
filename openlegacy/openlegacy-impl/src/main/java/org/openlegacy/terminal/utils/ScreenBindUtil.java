package org.openlegacy.terminal.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ChildScreenDefinition;

import java.text.MessageFormat;

public class ScreenBindUtil {

	private final static Log logger = LogFactory.getLog(ScreenBindUtil.class);

	public static Object getChildScreen(TerminalSession terminalSession, Class<?> screenEnity,
			ChildScreenDefinition childScreenDefinition) throws InstantiationException, IllegalAccessException {

		terminalSession.doAction(childScreenDefinition.getStepInto().newInstance(), null);
		Object returnTypeInstance = terminalSession.getEntity(screenEnity);

		logger.info(MessageFormat.format("Collected child screen for class {1}", childScreenDefinition.getFieldName(),
				screenEnity));

		return returnTypeInstance;
	}
}
