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
package org.openlegacy.terminal.web.mvc;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.trail.TrailUtil;
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

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TrailUtil trailUtil;

	@RequestMapping(value = "/logoff", method = RequestMethod.GET)
	public String logoff() throws IOException {

		trailUtil.saveTrail(terminalSession);

		terminalSession.disconnect();
		return "logoff";
	}

}
