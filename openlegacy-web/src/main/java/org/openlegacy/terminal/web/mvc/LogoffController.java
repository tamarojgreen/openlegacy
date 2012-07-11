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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.modules.trail.TrailWriter;
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

	private final static Log logger = LogFactory.getLog(LogoffController.class);

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Inject
	private TrailWriter trailWriter;

	@RequestMapping(value = "/logoff", method = RequestMethod.GET)
	public String logoff() throws IOException {

		String trailPath = openLegacyProperties.getProperty(OpenLegacyProperties.TRAIL_FOLDER_PATH);
		if (trailPath != null) {
			try {
				TrailUtil.saveTrail(terminalSession, trailWriter, trailPath);
			} catch (Exception e) {
				logger.error("Unable to save trail file", e);
			}
		}

		terminalSession.disconnect();
		return "logoff";
	}

}
