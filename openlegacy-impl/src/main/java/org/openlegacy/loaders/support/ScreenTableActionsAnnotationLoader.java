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
package org.openlegacy.loaders.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.table.TerminalDrilldownAction;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
public class ScreenTableActionsAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenTableActionsAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenTableActions.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenTableDefinition screenTableDefinition = screenEntitiesRegistry.getTable(containingClass);
		if (screenTableDefinition == null) {
			throw (new RegistryException("Class " + containingClass + " was not defined as @ScreenTable"));
		}
		ScreenTableActions screenTableActions = (ScreenTableActions)annotation;

		TableAction[] actions = screenTableActions.actions();
		if (actions.length > 0) {
			for (TableAction action : actions) {
				Class<? extends TerminalDrilldownAction> theAction = action.action();
				TerminalDrilldownAction drilldownAction = ReflectionUtil.newInstance(theAction);
				drilldownAction.setActionValue(action.actionValue());
				SimpleTerminalActionDefinition actionDefinition = new SimpleTerminalActionDefinition(drilldownAction,
						AdditionalKey.NONE, action.displayName(), null);

				if (StringUtils.isEmpty(action.alias())) {
					actionDefinition.setAlias(StringUtil.toJavaFieldName(action.displayName()));
				} else {
					actionDefinition.setAlias(action.alias());
				}

				if (action.targetEntity() != ScreenEntity.NONE.class) {
					actionDefinition.setTargetEntity(action.targetEntity());
				}

				screenTableDefinition.getActions().add(actionDefinition);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Action {0} - \"{1}\" was added to the registry for table {2}",
							theAction.getSimpleName(), action.displayName(), containingClass));
				}

			}
			logger.info(MessageFormat.format("Screen identifications for \"{0}\" was added to the screen registry",
					screenTableDefinition.getTableClass()));
		}
	}
}
