package org.openlegacy.terminal.support.injectors;

import org.openlegacy.FetchMode;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ChildScreenDefinition;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.ChildScreensDefinitionProvider;
import org.openlegacy.terminal.utils.ScreenAccessUtils;
import org.springframework.context.annotation.Scope;

import java.util.Collection;

import javax.inject.Inject;

@Scope("sesssion")
public class ScreenEntityChildScreensInjector implements ScreenEntityDataInjector<ChildScreensDefinitionProvider> {

	@Inject
	private ChildScreensDefinitionProvider childScreensDefinitionProvider;

	@Inject
	private TerminalSession terminalSession;

	public void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen,
			boolean deep) {

		if (deep) {
			return;
		}

		Collection<ChildScreenDefinition> childScreenDefinitions = childScreensDefinitionProvider.getChildScreenDefinitions(screenEntityClass);

		for (ChildScreenDefinition childScreenDefinition : childScreenDefinitions) {

			if (childScreenDefinition.getFetchMode() == FetchMode.LAZY) {
				continue;
			}

			String fieldName = childScreenDefinition.getFieldName();

			Class<?> fieldType = fieldAccessor.getFieldType(fieldName);
			Object childScreen = ScreenAccessUtils.getChildScreen(terminalSession, fieldType, childScreenDefinition);

			fieldAccessor.setFieldValue(fieldName, childScreen);
		}

	}

	public ChildScreensDefinitionProvider DefinitionsProvider() {
		return childScreensDefinitionProvider;
	}

}
