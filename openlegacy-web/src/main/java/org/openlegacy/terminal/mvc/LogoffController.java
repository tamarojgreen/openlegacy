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
package org.openlegacy.terminal.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.trail.TrailUtil;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LogoffController {

	private final static Log logger = LogFactory.getLog(LogoffController.class);

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TrailUtil trailUtil;

	@RequestMapping(value = "/logoff", method = RequestMethod.GET)
	public String logoff() throws IOException {

		try {
			trailUtil.saveTrail(terminalSession);
		} catch (Exception e) {
			logger.warn("Failed to save trail - " + e.getMessage(), e);
		} finally {
			terminalSession.getModule(Login.class).logoff();
		}

		return "logoff";
	}

}
