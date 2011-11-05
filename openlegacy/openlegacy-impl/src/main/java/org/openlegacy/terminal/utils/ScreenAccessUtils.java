package org.openlegacy.terminal.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ChildScreenDefinition;

import java.text.MessageFormat;

public class ScreenAccessUtils {

	private final static Log logger = LogFactory.getLog(ScreenAccessUtils.class);

	public static Object getChildScreen(TerminalSession terminalSession, Class<?> screenEnity,
			ChildScreenDefinition childScreenDefinition) {

		try {
			terminalSession.doAction(childScreenDefinition.getStepInto().newInstance(), null);
		} catch (InstantiationException e) {
			throw (new IllegalStateException(e));
		} catch (IllegalAccessException e) {
			throw (new IllegalStateException(e));
		}

		Object childEntity = terminalSession.getEntity(screenEnity);

		logger.info(MessageFormat.format("Collected child screen for class {1}", childScreenDefinition.getFieldName(),
				screenEnity));

		return childEntity;
	}
}
