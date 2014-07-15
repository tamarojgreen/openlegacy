package org.openlegacy.terminal.modules.messages;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.ApplicationConnection;
import org.openlegacy.RemoteAction;
import org.openlegacy.Snapshot;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.util.List;

public class ResetMessagesModule extends TerminalSessionModuleAdapter {

	private static final long serialVersionUID = 1L;

	private List<String> messages;

	private String messageField;

	private TerminalAction terminalAction = TerminalActions.ESC();

	@Override
	public void afterAction(ApplicationConnection<?, ?> connection, RemoteAction action, Snapshot result) {
		ScreenEntity entity = getSession().getEntity();
		if (entity == null) {
			return;
		}

		if (messageField == null || messages == null) {
			return;
		}

		SimpleScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);
		if (!fieldAccessor.isExists(messageField)) {
			return;
		}
		String error = (String)fieldAccessor.getFieldValue(messageField);
		if (StringUtils.isEmpty(error)) {
			return;
		}

		for (String message : messages) {
			if (error.contains(message)) {
				getSession().doAction(terminalAction);
				break;
			}

		}
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public void setMessageField(String messageField) {
		this.messageField = messageField;
	}

	public void setTerminalAction(Class<TerminalAction> terminalAction) {
		this.terminalAction = org.openlegacy.utils.ReflectionUtil.newInstance(terminalAction);
	}
}
