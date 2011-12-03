package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ScreenActionsAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenActionsAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenActions.class;
	}

	public void load(EntitiesRegistry<?, ?> screenEntitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenEntityDefinition screenEntityDefinition = (ScreenEntityDefinition)screenEntitiesRegistry.get(containingClass);
		ScreenActions screenActions = (ScreenActions)annotation;

		Action[] actions = screenActions.actions();
		if (actions.length > 0) {
			for (Action action : actions) {
				@SuppressWarnings("unchecked")
				Class<? extends SessionAction<Session>> theAction = (Class<? extends SessionAction<Session>>)action.action();
				TerminalPosition position = null;
				if (action.row() > 0 && action.column() > 0) {
					position = new SimpleTerminalPosition(action.row(), action.column());
				}
				SimpleActionDefinition actionDefinition = new SimpleActionDefinition(theAction, position, action.displayName());

				screenEntityDefinition.getActions().add(actionDefinition);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Action {0} - \"{1}\" was added to the registry for screen {2}",
							theAction.getSimpleName(), action.displayName(), containingClass));
				}

			}
			logger.info(MessageFormat.format("Screen identifications for \"{0}\" was added to the screen registry",
					screenEntityDefinition.getEntityClass()));
		}
	}
}
