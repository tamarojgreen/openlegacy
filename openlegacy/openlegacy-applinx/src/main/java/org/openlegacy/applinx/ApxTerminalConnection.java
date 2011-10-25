package org.openlegacy.applinx;

import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXCursor;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.baseobject.GXInputField;
import com.sabratec.applinx.baseobject.GXSendKeysRequest;
import com.sabratec.applinx.baseobject.internal.GXClientScreen;
import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;
import com.sabratec.util.GXPosition;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.HostActionMapper;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;

public class ApxTerminalConnection implements TerminalConnection {

	private final GXIClientBaseObject baseObject;

	private HostActionMapper hostActionMapper;

	public ApxTerminalConnection(GXIClientBaseObject baseObject, HostActionMapper hostActionMapper) {
		this.baseObject = baseObject;
		this.hostActionMapper = hostActionMapper;
	}

	public TerminalScreen getSnapshot() {
		return newHostScreen();
	}

	private TerminalScreen newHostScreen() {
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

		HostAction hostAction = terminalSendAction.getHostAction();
		Map<ScreenPosition, String> fields = terminalSendAction.getFields();
		ScreenPosition cursorPosition = terminalSendAction.getCursorPosition();

		validateAction(hostAction);

		GXSendKeysRequest sendKeyRequest = buildRequest(fields, hostAction);
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

	private GXSendKeysRequest buildRequest(Map<ScreenPosition, String> fields, HostAction hostAction) {
		String command = hostActionMapper.map(hostAction);
		GXSendKeysRequest sendKeyRequest = new GXSendKeysRequest(command);

		if (fields == null) {
			return sendKeyRequest;
		}

		Set<ScreenPosition> fieldPositions = fields.keySet();
		for (ScreenPosition screenPosition : fieldPositions) {
			String value = fields.get(screenPosition);
			GXPosition apxPosition = ApxPositionUtil.toPosition(screenPosition);
			GXInputField inputField = new GXInputField(apxPosition, value);
			sendKeyRequest.addInputField(inputField);
		}
		return sendKeyRequest;
	}

	private static void validateAction(HostAction action) {
		if (!(action.getCommand() instanceof String)) {
			throw (new IllegalArgumentException(
					MessageFormat.format("Specified {0} action is not supported", action.getCommand())));
		}
	}

}
