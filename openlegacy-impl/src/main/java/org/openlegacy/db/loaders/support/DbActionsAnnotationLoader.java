/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.db.loaders.support;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.db.Action;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.db.actions.DbAction;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.SimpleDbActionDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
@Component
public class DbActionsAnnotationLoader extends AbstractClassAnnotationLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == DbActions.class;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		DbEntityDefinition dbEntityDefinition = (DbEntityDefinition)entitiesRegistry.get(containingClass);
		List<ActionDefinition> actionsRef = dbEntityDefinition.getActions();

		DbActions dbActions = (DbActions)annotation;
		Action[] actions = dbActions.actions();
		if (actions.length > 0) {
			for (Action action : actions) {
				Class<? extends DbAction> theAction = action.action();

				SimpleDbActionDefinition actionDefinition = null;
				String displayName = action.displayName().length() > 0 ? action.displayName()
						: StringUtil.toDisplayName(action.action().getSimpleName());
				actionDefinition = new SimpleDbActionDefinition(ReflectionUtil.newInstance(theAction), displayName);

				if (StringUtils.isEmpty(action.alias())) {
					actionDefinition.setAlias(StringUtils.uncapitalize(displayName));
				} else {
					actionDefinition.setAlias(action.alias());
				}

				actionDefinition.setGlobal(action.global());
				if (action.targetEntity() != void.class) {
					actionDefinition.setTargetEntity(action.targetEntity());
				}
				actionsRef.add(actionDefinition);
			}
		}
	}

}
