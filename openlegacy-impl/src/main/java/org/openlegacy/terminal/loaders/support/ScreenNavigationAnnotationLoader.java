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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalMappedAction;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.definitions.SimpleFieldAssignDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenNavigationDefinition;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.table.TerminalDrilldownAction;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
public class ScreenNavigationAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenNavigationAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenNavigation.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenNavigation screenNavigation = (ScreenNavigation)annotation;
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		SimpleScreenNavigationDefinition navigationDefinition = new SimpleScreenNavigationDefinition();
		navigationDefinition.setAccessedFrom(screenNavigation.accessedFrom());
		navigationDefinition.setTargetEntity(containingClass);

		if (screenNavigation.drilldownValue().length() > 0) {
			if (screenNavigation.terminalAction() != ENTER.class) {
				throw (new RegistryException(MessageFormat.format(
						"@ScreenNavigation, drilldownValue is supported only with ENTER action. Entity:{0}",
						containingClass.getName())));
			}
			navigationDefinition.setDrilldownValue(screenNavigation.drilldownValue());
			navigationDefinition.setTerminalAction(TerminalDrilldownActions.enter(screenNavigation.drilldownValue()));
		} else {
			if (TerminalDrilldownAction.class.isAssignableFrom(screenNavigation.terminalAction())) {
				navigationDefinition.setTerminalAction(ReflectionUtil.newInstance(screenNavigation.terminalAction()));
			} else if (TerminalMappedAction.class.isAssignableFrom(screenNavigation.terminalAction())){
				navigationDefinition.setTerminalAction((TerminalAction)TerminalActions.combined(screenNavigation.additionalKey(),
						screenNavigation.terminalAction()));
			}
			else{
				navigationDefinition.setTerminalAction(ReflectionUtil.newInstance(screenNavigation.terminalAction()));
			}
		}
		navigationDefinition.setExitAction(ReflectionUtil.newInstance(screenNavigation.exitAction()));
		navigationDefinition.setRequiresParameters(screenNavigation.requiresParameters());

		AssignedField[] assignedFields = screenNavigation.assignedFields();
		for (AssignedField assignedField : assignedFields) {
			String value = assignedField.value();
			if (AnnotationConstants.NULL.equals(value)) {
				value = null;
			}
			navigationDefinition.getAssignedFields().add(new SimpleFieldAssignDefinition(assignedField.field(), value));
		}

		// @author Ivan Bort, refs assembla #112
		// -----
		navigationDefinition.setAdditionalKey(screenNavigation.additionalKey());
		navigationDefinition.setExitAdditionalKey(screenNavigation.exitAdditionalKey());
		// -----
		SimpleScreenEntityDefinition screenEntityDefinition = (SimpleScreenEntityDefinition)screenEntitiesRegistry.get(containingClass);
		Assert.notNull(
				screenEntityDefinition,
				MessageFormat.format(
						"Screen entity definition for class {0} not found. Verify @ScreenNavigation is defined along @ScreenEntity annotation",
						containingClass.getName()));
		screenEntityDefinition.setNavigationDefinition(navigationDefinition);
		logger.info("Added screen navigation information to the registry for:" + containingClass);
	}
}
