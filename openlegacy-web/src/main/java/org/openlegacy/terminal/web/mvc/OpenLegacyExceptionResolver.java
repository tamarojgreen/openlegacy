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
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenLegacyExceptionResolver extends SimpleMappingExceptionResolver {

	private final static Log logger = LogFactory.getLog(OpenLegacyExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (ex instanceof SessionEndedException) {
			TerminalSession terminalSession = (TerminalSession)request.getSession().getAttribute(
					WebConstants.TERMINAL_SESSION_WEB_SESSION_ATTRIBUTE_NAME);
			if (terminalSession != null) {
				try {
					terminalSession.disconnect();
				} catch (Exception e) {
					// do nothing
				}
			}
		}

		logger.fatal(ex.getMessage(),ex);

		return super.resolveException(request, response, handler, ex);
	}
}
