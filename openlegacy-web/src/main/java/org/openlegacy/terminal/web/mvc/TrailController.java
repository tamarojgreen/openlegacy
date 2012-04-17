package org.openlegacy.terminal.web.mvc;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
public class TrailController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TrailWriter trailWriter;

	@Inject
	private TerminalSnapshotHtmlRenderer snapshotHtmlRenderer;

	@RequestMapping(value = "/trail/download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!terminalSession.isConnected()) {
			response.getWriter().write("Session is not connected");
			return;
		}
		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(trail, baos);
		response.setHeader("Content-Disposition",
				MessageFormat.format("attachment; filename=\"{0}.trail.xml\"", terminalSession.getSessionId()));
		response.getOutputStream().write(baos.toByteArray());
	}

	@RequestMapping(value = "/trail/{id}")
	public String showSnapshot(@PathVariable("id") int id, Model uiModel) throws Exception {
		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
		Snapshot snapshot = trail.getSnapshots().get(id);

		String result = snapshotHtmlRenderer.render((TerminalSnapshot)snapshot);
		uiModel.addAttribute("terminalHtml", result);
		return "HtmlEmulation";
	}
}
