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
package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXBaseObjectConstants;
import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXConnectionException;
import com.sabratec.applinx.baseobject.GXCursor;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXGetScreenRequest;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.baseobject.GXIScreen;
import com.sabratec.applinx.baseobject.GXInputField;
import com.sabratec.applinx.baseobject.GXSendKeysRequest;
import com.sabratec.applinx.baseobject.internal.GXClientScreen;
import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;
import com.sabratec.util.GXPosition;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.List;

public class ApxTerminalConnection implements TerminalConnection {

	private final GXIClientBaseObject baseObject;

	public ApxTerminalConnection(GXIClientBaseObject baseObject) {
		this.baseObject = baseObject;
	}

	public TerminalSnapshot getSnapshot() {
		return newTerminalSnapshot();
	}

	private TerminalSnapshot newTerminalSnapshot() {
		try {
			GXRuntimeScreen screen = ((GXClientScreen)fetchScreen()).getRuntimeScreen();
			return new ApxTerminalSnapshot(screen);
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	private GXIScreen fetchScreen() throws GXConnectionException, GXGeneralException {
		GXGetScreenRequest screenRequest = new GXGetScreenRequest();
		screenRequest.addVariable(GXBaseObjectConstants.GX_VAR_ENCODING_LOGICAL, "true");
		return baseObject.getScreen(screenRequest);
	}

	public void disconnect() {
		try {
			GXClientBaseObjectFactory.endSession(baseObject);
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public Object getDelegate() {
		return baseObject;
	}

	public void doAction(TerminalSendAction terminalSendAction) {

		List<TerminalField> fields = terminalSendAction.getModifiedFields();
		TerminalPosition cursorPosition = terminalSendAction.getCursorPosition();

		GXSendKeysRequest sendKeyRequest = buildRequest(fields, terminalSendAction.getCommand());
		GXIClientBaseObject baseObject = (GXIClientBaseObject)getDelegate();

		if (cursorPosition != null) {
			sendKeyRequest.setCursor(new GXCursor(ApxPositionUtil.toPosition(cursorPosition)));
		}
		try {
			baseObject.sendKeys(sendKeyRequest);
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	private static GXSendKeysRequest buildRequest(List<TerminalField> fields, Object command) {
		GXSendKeysRequest sendKeyRequest = new GXSendKeysRequest((String)command);

		if (fields == null) {
			return sendKeyRequest;
		}

		for (TerminalField terminalField : fields) {
			String value = terminalField.getValue();
			GXPosition apxPosition = ApxPositionUtil.toPosition(terminalField.getPosition());
			GXInputField inputField = new GXInputField(apxPosition, value);
			sendKeyRequest.addInputField(inputField);
		}
		return sendKeyRequest;
	}

	public TerminalSnapshot fetchSnapshot() {
		try {
			baseObject.refreshScreen();
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
		return getSnapshot();
	}

	public String getSessionId() {
		return baseObject.getSessionID();
	}

	public boolean isConnected() {
		return true;
	}

	public Integer getSequence() {
		try {
			return baseObject.getSeqScreenNumber();
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

}
