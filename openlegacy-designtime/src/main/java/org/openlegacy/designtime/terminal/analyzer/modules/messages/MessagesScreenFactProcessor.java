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
package org.openlegacy.designtime.terminal.analyzer.modules.messages;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;

import java.util.List;

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

		List<TerminalField> messageFields = messagesScreenFact.getMessageFields();
		TerminalField firstField = messageFields.get(0);
		TerminalField lastField = messageFields.get(messageFields.size() - 1);

		SimpleScreenFieldDefinition messageFieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinitionsBuilderUtils.addField(
				screenEntityDefinition, firstField, Messages.MESSAGE_FIELD);
		messageFieldDefinition.setEndPosition(lastField.getEndPosition());
		messageFieldDefinition.setRectangle(true);

		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, messageFieldDefinition,
				Messages.MessageField.class);
		screenEntityDefinition.getFieldsDefinitions().put(Messages.MESSAGE_FIELD, messageFieldDefinition);
	}
}
