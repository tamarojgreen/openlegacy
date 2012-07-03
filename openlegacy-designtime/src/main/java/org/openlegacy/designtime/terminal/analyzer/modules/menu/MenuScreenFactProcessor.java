package org.openlegacy.designtime.terminal.analyzer.modules.menu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;

import javax.inject.Inject;

public class MenuScreenFactProcessor implements ScreenFactProcessor {

	private final static Log logger = LogFactory.getLog(MenuScreenFactProcessor.class);

	@Inject
	private ScreenEntityDefinitionsBuilderUtils screenEntityDefinitionsBuilderUtils;

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {

		MenuScreenFact menuScreenFact = (MenuScreenFact)screenFact;

		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(Menu.MenuEntity.class));
		screenEntityDefinition.setType(Menu.MenuEntity.class);
		TerminalSnapshot snapshot = screenEntityDefinition.getSnapshot();

		ScreenFieldDefinition fieldDefinition = screenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
				menuScreenFact.getSelectionField(), Menu.SELECTION_LABEL);
		if (fieldDefinition == null) {
			logger.warn("Menu selection field not added to screen entity");
			return;
		}

		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, fieldDefinition,
				Menu.MenuSelectionField.class);

		for (MenuItemFact menuItem : menuScreenFact.getMenuItems()) {
			snapshot.getFields().remove(menuItem.getCodeField());
			snapshot.getFields().remove(menuItem.getCaptionField());
		}

	}

	public boolean accept(ScreenFact screenFact) {
		return (screenFact instanceof MenuScreenFact);
	}

}
