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
package org.openlegacy.terminal.web.render.support;

import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
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
			// replace newlines in text areas (browser OS might be different then server OS)
			if (value != null) {
				value = value.replaceAll("\r", "");
				if (!terminalField.getValue().equals(value)) {
					value = value.replaceAll("\n", " ");
					terminalField.setValue(value);
					sendAction.getFields().add(terminalField);
				}
			}
		}
		return sendAction;
	}

	protected TerminalPosition getCursor(HttpServletRequest httpRequest, int columns) {
		String terminalCursor = httpRequest.getParameter(TerminalHtmlConstants.TERMINAL_CURSOR_HIDDEN);
		if (terminalCursor == null) {
			return null;
		}
		return HtmlNamingUtil.toPosition(terminalCursor);
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
