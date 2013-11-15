package org.openlegacy.terminal.mvc.web.interceptors;

import org.openlegacy.mvc.web.MvcConstants;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.login.LoginMetadata;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private LoginMetadata loginMetadata;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURI().toLowerCase().substring(1);
		if (!request.getRequestURI().contains(".") && !requestUri.endsWith(MvcConstants.LOGIN_VIEW)
				&& !requestUri.endsWith("logoff") && loginMetadata.getLoginScreenDefinition() != null
				&& !terminalSession.isConnected()) {
			String requestedPage = requestUri.substring(requestUri.indexOf("/") + 1);
			response.sendRedirect("login?requestedUrl=" + requestedPage);
			return false;
		}
		return true;
	}
}
