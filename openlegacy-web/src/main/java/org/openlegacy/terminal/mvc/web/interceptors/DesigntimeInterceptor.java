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
package org.openlegacy.terminal.mvc.web.interceptors;

import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.trail.DefaultTerminalTrail;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DesigntimeInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (!terminalSession.isConnected()) {
			return true;
		}

		// check out if configured to save trail - if so, define unlimited recording size (design-time)
		String trailPath = openLegacyProperties.getTrailPath();
		if (trailPath != null) {
			DefaultTerminalTrail trail = (DefaultTerminalTrail)terminalSession.getModule(Trail.class).getSessionTrail();
			trail.setHistoryCount(null);
		}

		return true;

	}
}
