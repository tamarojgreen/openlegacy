package org.openlegacy.loaders.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
public class ScreenActionsAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenActionsAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenActions.class;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenEntityDefinition screenEntityDefinition = (ScreenEntityDefinition)entitiesRegistry.get(containingClass);
		ScreenActions screenActions = (ScreenActions)annotation;

		Action[] actions = screenActions.actions();
		if (actions.length > 0) {
			for (Action action : actions) {
				Class<? extends TerminalAction> theAction = action.action();

				TerminalPosition position = null;
				if (action.row() > 0 && action.column() > 0) {
					position = new SimpleTerminalPosition(action.row(), action.column());
				}
				SimpleActionDefinition actionDefinition = null;
				if (action.additionalKey() != AdditionalKey.NONE || position != null) {
					actionDefinition = new SimpleTerminalActionDefinition(TerminalActions.combined(action.additionalKey(),
							theAction), action.additionalKey(), action.displayName(), position);
				} else {
					actionDefinition = new SimpleActionDefinition(ReflectionUtil.newInstance(theAction), action.displayName());
				}

				if (StringUtils.isEmpty(action.alias())) {
					actionDefinition.setAlias(action.displayName());
				} else {
					actionDefinition.setAlias(action.alias());
				}

				screenEntityDefinition.getActions().add(actionDefinition);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Action {0} - \"{1}\" was added to the registry for screen {2}",
							theAction.getSimpleName(), action.displayName(), containingClass));
				}

			}
			logger.info(MessageFormat.format("Screen actions for \"{0}\" was added to the screen registry",
					screenEntityDefinition.getEntityClass()));
		}
	}
}
