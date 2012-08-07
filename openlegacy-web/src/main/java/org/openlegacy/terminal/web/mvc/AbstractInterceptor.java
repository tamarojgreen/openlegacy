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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private TerminalSession terminalSession;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

		if (modelAndView == null) {
			return;
		}

		modelAndView.addObject("ol_version", getClass().getPackage().getImplementationVersion());

		if (!terminalSession.isConnected()) {
			return;
		}

		if ((modelAndView.getViewName().startsWith("redirect"))) {
			return;
		}

		insertModelData(modelAndView);
	}

	protected TerminalSession getTerminalSession() {
		return terminalSession;
	}

	protected abstract void insertModelData(ModelAndView modelAndView);
}
