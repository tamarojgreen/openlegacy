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
package org.openlegacy.providers.applinx.web;

import com.sabratec.applinx.framework.web.GXHiddenConstants;
import com.sabratec.applinx.presentation.internal.tags.GXTagNamesUtil;
import com.sabratec.util.GXPosition;

import org.openlegacy.providers.applinx.ApxPositionUtil;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.web.render.support.DefaultHttpPostSendActionBuilder;
import org.openlegacy.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;

public class ApxHttpPostTerminalSendActionBuilder extends DefaultHttpPostSendActionBuilder {

	@Override
	protected TerminalPosition getCursor(HttpServletRequest httpRequest, int columns) {
		String cursorValue = httpRequest.getParameter(GXHiddenConstants.CURSOR_POSITION_HIDDEN);
		if (StringUtil.isEmpty(cursorValue)) {
			return null;
		}
		GXPosition position = GXTagNamesUtil.getPositionByHostFieldName(cursorValue, 0, columns);
		return ApxPositionUtil.toTerminalPosition(position);
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
