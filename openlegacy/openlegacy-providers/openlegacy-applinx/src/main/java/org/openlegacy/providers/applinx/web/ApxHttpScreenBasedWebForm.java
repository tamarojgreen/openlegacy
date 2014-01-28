/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.providers.applinx.web;

import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIField;
import com.sabratec.applinx.framework.web.GXIScreenBasedForm;
import com.sabratec.applinx.presentation.internal.tags.GXTagNamesUtil;
import com.sabratec.util.GXPosition;

import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.web.render.support.TerminalHtmlConstants;

import javax.servlet.http.HttpServletRequest;

public class ApxHttpScreenBasedWebForm implements GXIScreenBasedForm {

	private HttpServletRequest request;
	private TerminalSnapshot snapshot;
	private TerminalActionMapper terminalActionMapper;

	public ApxHttpScreenBasedWebForm(HttpServletRequest request, TerminalSnapshot snapshot,
			TerminalActionMapper terminalActionMapper) {
		this.request = request;
		this.snapshot = snapshot;
		this.terminalActionMapper = terminalActionMapper;
	}

	public String getAppFieldContent(String arg0) throws GXGeneralException {
		// not used
		return null;
	}

	public int getCaretPosition() {
		return 0;
	}

	public String getCursorPosition() {
		return request.getParameter(TerminalHtmlConstants.TERMINAL_CURSOR_HIDDEN);
	}

	public String getFormName() {
		// not used
		return null;
	}

	public String getHostFieldContent(GXPosition position) throws GXGeneralException {
		String fieldName = GXTagNamesUtil.getHostFieldName(position, snapshot.getSize().getColumns());
		return request.getParameter(fieldName);
	}

	public String getHostKeys() {
		String keyboardKey = request.getParameter(TerminalHtmlConstants.KEYBOARD_KEY);
		Object command = TerminalActions.getCommand(keyboardKey, terminalActionMapper);
		return command.toString();
	}

	public String getMultipleFieldContent(String arg0, int arg1) throws GXGeneralException {
		// not used
		return null;
	}

	public int getSeqScreenNumber() throws GXGeneralException {
		// TODO implement screen sequence from http
		return snapshot.getSequence();
	}

	public String get_CursorPosition() {
		return getCursorPosition();
	}

	public String get_FormName() {
		return getFormName();
	}

	public String get_HostKeys() {
		return getHostKeys();
	}

	public int get_SeqScreenNumber() throws GXGeneralException {
		return getSeqScreenNumber();
	}

	public void setAppField(GXIField arg0) {
		// not used
	}

	public void setHostField(GXIField arg0) throws GXGeneralException {
		// not used
	}

	public void setMultipleField(GXIField arg0) throws GXGeneralException {
		// not used
	}

}
