package org.openlegacy.terminal.web.mvc;

import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenLegacyExceptionResolver extends SimpleMappingExceptionResolver {

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
		return super.resolveException(request, response, handler, ex);
	}
}
