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
package org.openlegacy.designtime.terminal.analyzer.modules.menu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;

import javax.inject.Inject;

public class MenuScreenFactProcessor implements ScreenFactProcessor {

	private final static Log logger = LogFactory.getLog(MenuScreenFactProcessor.class);

	@Inject
	private ScreenEntityDefinitionsBuilderUtils screenEntityDefinitionsBuilderUtils;

	@Override
	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {

		MenuScreenFact menuScreenFact = (MenuScreenFact)screenFact;

		if (screenEntityDefinition.getType() == MenuEntity.class) {
			return;
		}

		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(Menu.MenuEntity.class));
		screenEntityDefinition.setType(Menu.MenuEntity.class);
		TerminalSnapshot snapshot = screenEntityDefinition.getSnapshot();

		ScreenFieldDefinition fieldDefinition = screenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
				menuScreenFact.getSelectionFields().get(0), Menu.SELECTION_LABEL);
		if (fieldDefinition == null) {
			logger.warn("Menu selection field not added to screen entity");
			return;
		}

		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, fieldDefinition,
				Menu.MenuSelectionField.class);

		for (MenuItemFact menuItem : menuScreenFact.getMenuItems()) {
			snapshot.getFields().remove(menuItem.getCodeField());
			if (menuItem.getCaptionField() != null) {
				snapshot.getFields().remove(menuItem.getCaptionField());
			}
		}

	}

	@Override
	public boolean accept(ScreenFact screenFact) {
		return (screenFact instanceof MenuScreenFact);
	}

}
