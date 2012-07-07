package org.openlegacy.terminal.web.mvc;

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
		String trailPath = openLegacyProperties.getProperty(OpenLegacyProperties.TRAIL_FOLDER_PATH);
		if (trailPath != null) {
			DefaultTerminalTrail trail = (DefaultTerminalTrail)terminalSession.getModule(Trail.class).getSessionTrail();
			trail.setHistoryCount(null);
		}

		return true;

	}
}
