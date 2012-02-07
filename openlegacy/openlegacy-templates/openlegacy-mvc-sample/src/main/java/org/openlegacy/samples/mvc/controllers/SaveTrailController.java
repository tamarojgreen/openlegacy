package org.openlegacy.samples.mvc.controllers;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SaveTrailController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TrailWriter trailWriter;

	
	@RequestMapping(value = "/trail/download")
	public ModelAndView download(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(trail, baos);
		response.setHeader("Content-Disposition", "attachment; filename=\"trail.xml\"");
		response.getOutputStream().write(baos.toByteArray());
		return null;
	}
}
