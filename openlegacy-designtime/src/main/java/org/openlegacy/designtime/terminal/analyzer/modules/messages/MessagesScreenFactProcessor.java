package org.openlegacy.designtime.terminal.analyzer.modules.messages;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;

import javax.inject.Inject;

public class MessagesScreenFactProcessor implements ScreenFactProcessor {

	@Inject
	private ScreenEntityDefinitionsBuilderUtils screenEntityDefinitionsBuilderUtils;

	public boolean accept(ScreenFact screenFact) {
		return (screenFact instanceof MessagesScreenFact);
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		MessagesScreenFact messagesScreenFact = (MessagesScreenFact)screenFact;

		screenEntityDefinition.setType(Messages.MessagesEntity.class);
		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(Messages.MessagesEntity.class));

		// TODO handle multiple field
		ScreenFieldDefinition messageFieldDefinition = screenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
				messagesScreenFact.getMessageFields().get(0), "");
		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, messageFieldDefinition,
				Messages.MessageField.class);
		screenEntityDefinition.getFieldsDefinitions().put(Messages.MESSAGE_FIELD, messageFieldDefinition);
	}
}
