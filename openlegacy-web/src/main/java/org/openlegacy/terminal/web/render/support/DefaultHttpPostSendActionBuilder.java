package org.openlegacy.terminal.web.render.support;

import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.SimpleTerminalSendAction;
import org.openlegacy.terminal.utils.FieldsQuery;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class DefaultHttpPostSendActionBuilder implements TerminalSendActionBuilder<HttpServletRequest> {

	@Inject
	private TerminalActionMapper terminalActionMapper;

	public TerminalSendAction buildSendAction(TerminalSnapshot terminalSnapshot, HttpServletRequest httpRequest) {
		Object command = getCommand(httpRequest);
		TerminalSendAction sendAction = new SimpleTerminalSendAction(command);

		int columns = terminalSnapshot.getSize().getColumns();

		TerminalPosition position = getCursor(httpRequest, columns);
		sendAction.setCursorPosition(position);

		List<TerminalField> editableFields = FieldsQuery.queryFields(terminalSnapshot,
				FieldsQuery.EditableFieldsCriteria.instance());
		for (TerminalField terminalField : editableFields) {
			String value = httpRequest.getParameter(getFieldHttpName(terminalField, columns));
			if (!terminalField.getValue().equals(value)) {
				terminalField.setValue(value);
				sendAction.getModifiedFields().add(terminalField);
			}
		}
		return sendAction;
	}

	protected TerminalPosition getCursor(HttpServletRequest httpRequest, int columns) {
		String terminalCursor = httpRequest.getParameter(TerminalHtmlConstants.TERMINAL_CURSOR_HIDDEN);
		HtmlNamingUtil.toPosition(terminalCursor);
		return null;
	}

	protected Object getCommand(HttpServletRequest httpRequest) {
		String keyboardKey = httpRequest.getParameter(TerminalHtmlConstants.KEYBOARD_KEY);
		Object command = TerminalActions.getCommand(keyboardKey, terminalActionMapper);
		return command;

	}

	protected String getFieldHttpName(TerminalField terminalField, int columns) {
		return HtmlNamingUtil.getFieldName(terminalField);
	}
}
