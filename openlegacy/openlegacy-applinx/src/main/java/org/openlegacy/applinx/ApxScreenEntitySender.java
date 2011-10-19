package org.openlegacy.applinx;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.spi.ScreenEntitySender;

import com.sabratec.applinx.baseobject.GXCursor;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.baseobject.GXInputField;
import com.sabratec.applinx.baseobject.GXSendKeysRequest;
import com.sabratec.util.GXPosition;

public class ApxScreenEntitySender implements ScreenEntitySender {

	public void perform(TerminalSession terminalSession,
			Map<ScreenPosition, String> fields,HostAction hostAction, ScreenPosition cursorPosition) {

		validateAction(hostAction);

		GXSendKeysRequest sendKeyRequest = buildRequest(fields, hostAction);
		GXIClientBaseObject baseObject = (GXIClientBaseObject) terminalSession
				.getDelegate();

		if (cursorPosition != null) {
			sendKeyRequest.setCursor(new GXCursor(new GXPosition(cursorPosition
					.getRow(), cursorPosition.getColumn())));
		}
		try {
			baseObject.sendKeys(sendKeyRequest);
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	private GXSendKeysRequest buildRequest(Map<ScreenPosition, String> fieldValues, HostAction hostAction) {
		GXSendKeysRequest sendKeyRequest = new GXSendKeysRequest((String) hostAction
				.getCommand());

		if (fieldValues == null) {
			return sendKeyRequest;
		}

		Set<ScreenPosition> fields = fieldValues.keySet();
		for (ScreenPosition screenPosition : fields) {
				String value = fieldValues.get(screenPosition);
				GXPosition apxPosition = new GXPosition(screenPosition.getRow(), screenPosition.getColumn());
				GXInputField inputField = new GXInputField(apxPosition, value);
				sendKeyRequest.addInputField(inputField);
		}
		return sendKeyRequest;
	}

	private void validateAction(HostAction action) {
		if (!(action.getCommand() instanceof String)) {
			throw (new IllegalArgumentException(MessageFormat.format(
					"Specified {0} action is not supported", action
							.getCommand())));
		}
	}
}
