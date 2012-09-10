package org.openlegacy.terminal.web.mvc;

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
			if (cookieTheme != null) {
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
