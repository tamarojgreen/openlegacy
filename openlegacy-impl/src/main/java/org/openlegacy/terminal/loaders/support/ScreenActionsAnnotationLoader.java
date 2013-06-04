/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.loaders.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
		Assert.notNull(
				screenEntityDefinition,
				MessageFormat.format(
						"Screen entity definition for class {0} not found. Verify @ScreenActions is defined along @ScreenEntity annotation",
						containingClass.getName()));

		ScreenActions screenActions = (ScreenActions)annotation;

		Action[] actions = screenActions.actions();
		if (actions.length > 0) {
			for (Action action : actions) {
				Class<? extends TerminalAction> theAction = action.action();

				TerminalPosition position = null;
				if (action.row() > 0 && action.column() > 0) {
					position = new SimpleTerminalPosition(action.row(), action.column());
				}
				SimpleTerminalActionDefinition actionDefinition = null;
				String displayName = action.displayName().length() > 0 ? action.displayName()
						: StringUtil.toDisplayName(action.action().getSimpleName());
				if (action.additionalKey() != AdditionalKey.NONE || position != null) {
					actionDefinition = new SimpleTerminalActionDefinition(TerminalActions.combined(action.additionalKey(),
							theAction), action.additionalKey(), displayName, position);
				} else {
					actionDefinition = new SimpleTerminalActionDefinition(ReflectionUtil.newInstance(theAction),
							action.additionalKey(), displayName, null);
				}

				if (StringUtils.isEmpty(action.alias())) {
					actionDefinition.setAlias(displayName);
				} else {
					actionDefinition.setAlias(action.alias());
				}

				actionDefinition.setGlobal(action.global());

				if (action.focusField().length() > 0) {
					actionDefinition.setFocusField(action.focusField());
				}

				screenEntityDefinition.getActions().add(actionDefinition);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Action {0} - \"{1}\" was added to the registry for screen {2}",
							theAction.getSimpleName(), displayName, containingClass));
				}

			}
			logger.info(MessageFormat.format("Screen actions for \"{0}\" was added to the screen registry",
					screenEntityDefinition.getEntityClass()));
		}
	}
}
