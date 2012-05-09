package org.openlegacy.terminal.modules.messages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ReflectionUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class DefaultTerminalMessagesModule extends TerminalSessionModuleAdapter implements Messages {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(DefaultTerminalMessagesModule.class);

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private List<String> messages = new ArrayList<String>();

	private TerminalAction skipAction;

	@Override
	public void afterSendAction(TerminalConnection terminalConnection) {

		ScreenEntity currentEntity = getSession().getEntity();

		// if screen is not identified, exit
		if (currentEntity == null) {
			return;
		}

		ScreenEntityDefinition entityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());

		// if screen is not messages screen, exit
		if (entityDefinition.getType() != Messages.MessagesEntity.class) {
			return;
		}
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);

		// collect all messages field into messages
		Collection<ScreenFieldDefinition> fieldDefinitions = entityDefinition.getFieldsDefinitions().values();
		for (ScreenFieldDefinition screenFieldDefinition : fieldDefinitions) {
			if (screenFieldDefinition.getType() == Messages.MessageField.class) {
				Object fieldValue = fieldAccessor.getFieldValue(screenFieldDefinition.getName());
				if (fieldValue instanceof String) {
					messages.add((String)fieldValue);
				}
			}
		}

		// skip messages screen
		if (skipAction != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format("Skipping screen {0} using action {1}", entityDefinition.getEntityClassName(),
						skipAction.getClass().getSimpleName()));
			}
			getSession().doAction(skipAction);
		}
	}

	public void setSkipActionClass(Class<? extends TerminalAction> terminalAction) {
		this.skipAction = ReflectionUtil.newInstance(terminalAction);
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
