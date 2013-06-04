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
package org.openlegacy.terminal.modules.messages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.ApplicationConnection;
import org.openlegacy.RemoteAction;
import org.openlegacy.Snapshot;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class DefaultTerminalMessagesModule extends TerminalSessionModuleAdapter implements Messages {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(DefaultTerminalMessagesModule.class);

	@Inject
	private transient ApplicationContext applicationContext;

	private List<String> messages = new ArrayList<String>();

	private TerminalAction defaultSkipAction;

	private int skipLimit = 5;

	@Override
	public void afterAction(ApplicationConnection<?, ?> terminalConnection, RemoteAction action, Snapshot result) {

		ScreenEntity currentEntity = getSession().getEntity();

		// if screen is not identified, exit
		if (currentEntity == null) {
			return;
		}

		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		ScreenEntityDefinition entityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());

		int skippedScreens = 0;

		if (defaultSkipAction == null) {
			return;
		}

		// if screen is not messages screen, exit
		while (entityDefinition.getType() == Messages.MessagesEntity.class && skippedScreens < skipLimit) {
			ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);

			// collect all messages field into messages
			Collection<ScreenFieldDefinition> fieldDefinitions = entityDefinition.getFieldsDefinitions().values();
			ScreenFieldDefinition messagesFieldDefinition = null;
			for (ScreenFieldDefinition screenFieldDefinition : fieldDefinitions) {
				if (screenFieldDefinition.getType() == Messages.MessageField.class) {
					Object fieldValue = fieldAccessor.getFieldValue(screenFieldDefinition.getName());
					if (fieldValue instanceof String) {
						messagesFieldDefinition = screenFieldDefinition;
						messages.add((String)fieldValue);
					}
				}
			}
			if (messagesFieldDefinition == null) {
				throw (new RegistryException(MessageFormat.format(
						"Messages entity {0} doesnt contain a message field (@ScreenField(fieldType=MessagesField.class))",
						entityDefinition.getEntityClassName())));
			}

			// skip messages screen
			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format("Skipping screen {0} using action {1}", entityDefinition.getEntityClassName(),
						defaultSkipAction.getClass().getSimpleName()));
			}

			TerminalAction theSkipAction = defaultSkipAction;
			NavigationDefinition navigationDefinition = entityDefinition.getNavigationDefinition();
			if (navigationDefinition != null) {
				TerminalAction exitAction = navigationDefinition.getExitAction();
				if (navigationDefinition != null && exitAction != null) {
					theSkipAction = exitAction;
				}
			}
			getSession().doAction(theSkipAction);
			skippedScreens++;

			currentEntity = getSession().getEntity();

			if (currentEntity == null) {
				break;
			}
			entityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());

		}
	}

	public void setDefaultSkipActionClass(Class<? extends TerminalAction> terminalAction) {
		this.defaultSkipAction = ReflectionUtil.newInstance(terminalAction);
	}

	public void setSkipLimit(int skipLimit) {
		this.skipLimit = skipLimit;
	}

	public void setDefaultSkipAction(TerminalAction defaultSkipAction) {
		this.defaultSkipAction = defaultSkipAction;
	}

	public List<String> getMessages() {
		return new ArrayList<String>(messages);
	}

	public void resetMessages() {
		messages.clear();
	}

	@Override
	public void destroy() {
		resetMessages();
	}
}
