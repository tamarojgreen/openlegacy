package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXCursor;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.baseobject.GXInputField;
import com.sabratec.applinx.baseobject.GXSendKeysRequest;
import com.sabratec.applinx.baseobject.internal.GXClientScreen;
import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;
import com.sabratec.util.GXPosition;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.List;

public class ApxTerminalConnection implements TerminalConnection {

	private final GXIClientBaseObject baseObject;

	public ApxTerminalConnection(GXIClientBaseObject baseObject) {
		this.baseObject = baseObject;
	}

	public TerminalScreen getSnapshot() {
		return newTerminalScreen();
	}

	private TerminalScreen newTerminalScreen() {
		try {
			GXRuntimeScreen screen = ((GXClientScreen)baseObject.getScreen()).getRuntimeScreen();
			return new ApxTerminalScreen(screen);
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
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

	public TerminalConnection doAction(TerminalSendAction terminalSendAction) {

		List<TerminalField> fields = terminalSendAction.getModifiedFields();
		ScreenPosition cursorPosition = terminalSendAction.getCursorPosition();

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
		return this;
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

}
