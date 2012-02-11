package org.openlegacy.newmvc.web;

import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.trail.TrailUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LogoffController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Inject
	private TrailWriter trailWriter;

	@RequestMapping(value = "/logoff", method = RequestMethod.GET)
	public String logoff() throws IOException {

		String trailPath = openLegacyProperties.getProperty(OpenLegacyProperties.TRAIL_FOLDER_PATH);
		if (trailPath != null) {
			TrailUtil.saveTrail(terminalSession, trailWriter, trailPath);
		}

		terminalSession.disconnect();
		return "logoff";
	}

}
