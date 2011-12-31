package org.openlegacy.terminal.web.render.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.SimpleTerminalSendAction;
import org.openlegacy.terminal.utils.FieldsQuery;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class DefaultHttpPostSendActionBuilder implements TerminalSendActionBuilder<HttpServletRequest> {

	public TerminalSendAction buildSendAction(TerminalSnapshot terminalSnapshot, HttpServletRequest httpRequest) {
		String terminalAction = httpRequest.getParameter(HtmlNamingUtil.TERMINAL_ACTION);
		TerminalSendAction sendAction = new SimpleTerminalSendAction(terminalAction);

		String terminalCursor = httpRequest.getParameter(HtmlNamingUtil.TERMINAL_CURSOR);
		sendAction.setCursorPosition(HtmlNamingUtil.toPosition(terminalCursor));

		List<TerminalField> editableFields = FieldsQuery.queryFields(terminalSnapshot,
				FieldsQuery.EditableFieldsCriteria.instance());
		for (TerminalField terminalField : editableFields) {
			String value = httpRequest.getParameter(HtmlNamingUtil.getFieldName(terminalField));
			if (!terminalField.getValue().equals(value)) {
				terminalField.setValue(value);
				sendAction.getModifiedFields().add(terminalField);
			}
		}
		return sendAction;
	}
}
