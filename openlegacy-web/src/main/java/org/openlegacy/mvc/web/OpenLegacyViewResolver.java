package org.openlegacy.mvc.web;

import org.springframework.js.ajax.AjaxHandler;
import org.springframework.js.ajax.AjaxUrlBasedViewResolver;
import org.springframework.js.ajax.SpringJavascriptAjaxHandler;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenLegacyViewResolver extends AjaxUrlBasedViewResolver {

	public static final String WINDOW_URL_PREFIX = "window:/";

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		if (viewName.startsWith(WINDOW_URL_PREFIX)) {
			String windowUrl = viewName.substring(WINDOW_URL_PREFIX.length());
			return new OpenWindowView(windowUrl);
		}
		return super.createView(viewName, locale);
	}

	private class OpenWindowView extends RedirectView implements View {

		private AjaxHandler ajaxHandler = new SpringJavascriptAjaxHandler();

		public OpenWindowView(String redirectUrl) {
			super(redirectUrl, true, false);
		}

		@Override
		protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String targetUrl,
				boolean http10Compatible) throws IOException {
			if (ajaxHandler.isAjaxRequest(request, response)) {
				targetUrl = targetUrl.contains("?") ? targetUrl.substring(0, targetUrl.indexOf("?")) : targetUrl;
				ajaxHandler.sendAjaxRedirect(request.getContextPath() + "/" + targetUrl, request, response, true);
			} else {
				super.sendRedirect(request, response, targetUrl, http10Compatible);
			}
		}

	}

}
