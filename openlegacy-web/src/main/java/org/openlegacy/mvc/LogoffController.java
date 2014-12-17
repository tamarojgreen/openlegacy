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
package org.openlegacy.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.Session;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.trail.TrailUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LogoffController {

	private final static Log logger = LogFactory.getLog(LogoffController.class);

	@Inject
	private List<Session> sessions;

	@Inject
	private TrailUtil trailUtil;

	private boolean invalidateWebSession = true;

	@RequestMapping(value = "/logoff", method = RequestMethod.GET)
	public String logoff(HttpSession webSession, Model uiModel) throws IOException {

		List<String> trailFiles = new ArrayList<String>();
		for (Session session : sessions) {
			try {
				File trailFile = trailUtil.saveTrail(session);
				if (trailFile != null) {
					trailFiles.add(trailFile.getAbsolutePath());
				}
			} catch (Exception e) {
				logger.warn("Failed to save trail - " + e.getMessage(), e);
			} finally {
				Login loginModule = session.getModule(Login.class);
				if (loginModule != null) {
					loginModule.logoff();
				} else {
					session.disconnect();
				}
			}

		}
		if (invalidateWebSession) {
			webSession.invalidate();
		}
		if (trailFiles.size() > 0) {
			uiModel.addAttribute("trail", trailFiles.get(0));
		}
		return "logoff";
	}

	public void setInvalidateWebSession(boolean invalidateWebSession) {
		this.invalidateWebSession = invalidateWebSession;
	}

}
