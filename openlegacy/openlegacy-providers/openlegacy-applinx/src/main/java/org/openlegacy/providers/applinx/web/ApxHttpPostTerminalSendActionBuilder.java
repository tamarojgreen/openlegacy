package org.openlegacy.providers.applinx.web;

import com.sabratec.applinx.framework.web.GXHiddenConstants;
import com.sabratec.applinx.presentation.internal.tags.GXTagNamesUtil;
import com.sabratec.util.GXPosition;

import org.openlegacy.providers.applinx.ApxPositionUtil;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.web.render.support.DefaultHttpPostSendActionBuilder;

import javax.servlet.http.HttpServletRequest;

public class ApxHttpPostTerminalSendActionBuilder extends DefaultHttpPostSendActionBuilder {

	@Override
	protected String getCursorHttpName() {
		return GXHiddenConstants.CURSOR_POSITION_HIDDEN;
	}

	@Override
	protected Object getCommand(HttpServletRequest httpRequest) {
		return httpRequest.getParameter(GXHiddenConstants.HOSTKEYS_HIDDEN);
	}

	@Override
	protected String getFieldHttpName(TerminalField terminalField, int columns) {
		GXPosition position = ApxPositionUtil.toPosition(terminalField.getPosition());
		return GXTagNamesUtil.getHostFieldName(position, columns);
	}
}
