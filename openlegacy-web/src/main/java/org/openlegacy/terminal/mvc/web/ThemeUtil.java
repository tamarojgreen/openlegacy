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
package org.openlegacy.terminal.mvc.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ThemeUtil {

	private String defaultTheme;

	private static final String OL_THEME = "ol_theme";

	public void applyTheme(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
		String theme = defaultTheme;
		String requestTheme = request.getParameter(OL_THEME);

		if (requestTheme != null) {
			response.addCookie(new Cookie(OL_THEME, requestTheme));
			theme = requestTheme;
		} else {
			Cookie cookieTheme = WebUtils.getCookie(request, OL_THEME);
			if (cookieTheme != null && StringUtils.isNotEmpty(cookieTheme.getValue())) {
				theme = cookieTheme.getValue();
			} else {
				response.addCookie(new Cookie(OL_THEME, theme));
			}
		}
		modelAndView.addObject(OL_THEME, theme);
	}

	public void setDefaultTheme(String defaultTheme) {
		this.defaultTheme = defaultTheme;
	}
}
