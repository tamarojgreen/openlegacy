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
package org.openlegacy.terminal.mvc;

import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/current")
public class CurrentEntityController {

	@Inject
	private TerminalSession terminalSession;
	private String result = "";

	@RequestMapping(method = RequestMethod.GET)
	public void getSequence(HttpServletResponse response) throws IOException {
		if (!terminalSession.isConnected()) {
			response.getWriter().write("");
		} else if (terminalSession.getEntity() != null) {
			result = ProxyUtil.getTargetObject(terminalSession.getEntity()).getClass().getSimpleName();
		} else {
			result = ScreenEntity.NONE.class.getSimpleName();
		}
		response.getWriter().write(result);
	}

}
