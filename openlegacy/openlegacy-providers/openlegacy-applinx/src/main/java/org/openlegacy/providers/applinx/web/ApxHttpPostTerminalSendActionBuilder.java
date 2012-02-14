package org.openlegacy.providers.applinx.web;

import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIScreen;
import com.sabratec.applinx.baseobject.GXInputField;
import com.sabratec.applinx.baseobject.GXSendKeysRequest;
import com.sabratec.applinx.framework.GXFrameworkHandler;
import com.sabratec.util.lang.GXLanguages;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.providers.applinx.ApxPositionUtil;
import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.SimpleTerminalSendAction;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class ApxHttpPostTerminalSendActionBuilder implements TerminalSendActionBuilder<HttpServletRequest> {

	@Inject
	private TerminalActionMapper terminalActionMapper;

	public TerminalSendAction buildSendAction(TerminalSnapshot terminalSnapshot, HttpServletRequest source) {
		GXIScreen screen = (GXIScreen)terminalSnapshot.getDelegate();
		ApxHttpScreenBasedWebForm apxForm = new ApxHttpScreenBasedWebForm(source, terminalSnapshot, terminalActionMapper);
		GXSendKeysRequest sendKeysRequext;
		try {
			sendKeysRequext = GXFrameworkHandler.prepareSendKeysRequest(screen, apxForm, GXLanguages.LANG_STRING_ENGLISH);

			SimpleTerminalSendAction sendAction = new SimpleTerminalSendAction(sendKeysRequext.getKeys());

			convertCursor(sendKeysRequext, sendAction);
			convertFields(terminalSnapshot, sendKeysRequext, sendAction);

			return sendAction;
		} catch (GXGeneralException e) {
			throw (new OpenLegacyRuntimeException(e));
		}

	}

	private static void convertCursor(GXSendKeysRequest sendKeysRequext, SimpleTerminalSendAction sendAction) {
		TerminalPosition cursorPosition = ApxPositionUtil.toScreenPosition(sendKeysRequext.getCursor().getPosition());
		sendAction.setCursorPosition(cursorPosition);
	}

	private static void convertFields(TerminalSnapshot terminalSnapshot, GXSendKeysRequest sendKeysRequext,
			SimpleTerminalSendAction sendAction) {
		GXInputField[] fields = sendKeysRequext.getInputFields();
		for (GXInputField field : fields) {
			TerminalPosition position = ApxPositionUtil.toScreenPosition(field.getPosition());
			TerminalField inputField = terminalSnapshot.getField(position);
			inputField.setValue(field.getValue());
			sendAction.getModifiedFields().add(inputField);
		}
	}
}
