package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.analyzer.TerminalActionAnalyzer;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;

public class DefaultTerminalActionAnalyzer implements TerminalActionAnalyzer {

	private final static Log logger = LogFactory.getLog(DefaultTerminalActionAnalyzer.class);

	@SuppressWarnings("unchecked")
	public TerminalActionDefinition toTerminalActionDefinition(String action, String caption, TerminalPosition position) {
		action = action.trim().toUpperCase();
		AdditionalKey additionalKey = null;

		if (action.startsWith("F")) {
			String keyNumberText = action.substring(1);
			if (StringUtils.isNumeric(keyNumberText)) {
				int keyNumber = Integer.valueOf(keyNumberText);
				if (keyNumber > 12 && keyNumber <= 24) {
					keyNumber = keyNumber - 12;
					additionalKey = AdditionalKey.SHIFT;
					action = "F" + keyNumber;
				}
			}
		}

		Class<? extends TerminalAction> actionClass = null;
		try {
			actionClass = (Class<? extends TerminalAction>)Class.forName(MessageFormat.format("{0}{1}{2}",
					TerminalActions.class.getName(), ClassUtils.INNER_CLASS_SEPARATOR, action));
		} catch (ClassNotFoundException e) {
			logger.warn(MessageFormat.format("Could not found class for Action {0}", action));
		}

		TerminalAction actionInstance = ReflectionUtil.newInstance(actionClass);
		SimpleTerminalActionDefinition actionDefinition = new SimpleTerminalActionDefinition(actionInstance, additionalKey,
				caption, position);
		actionDefinition.setAlias(StringUtil.toJavaMethodName(caption));
		return actionDefinition;
	}
}
