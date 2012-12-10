package org.openlegacy.plugins.management.web;

import org.openlegacy.SessionsManager;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/management")
public class ManagementController {

	@Inject
	private SessionsManager<TerminalSession> sessionsManager;

	@Inject
	private ManagementPlugin managementPlugin;

	@Inject
	private TerminalSnapshotImageRenderer imageRenderer;

	@RequestMapping(method = RequestMethod.GET)
	public String getSessions(Model uiModel) throws IOException {
		uiModel.addAttribute("sessionsProperties", sessionsManager.getSessionsProperties());
		uiModel.addAttribute(managementPlugin);
		return "Management";
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String viewer(HttpServletRequest request, @PathVariable("id") String sessionId) throws IOException {
		if (managementPlugin.isEnableSessionViewer()) {
			return MessageFormat.format("<html><body><img src=\"{0}/management/image/{1}\"/></body></html>",
					request.getContextPath(), sessionId);
		} else {
			return MessageFormat.format("<html><body>Not allowed</body></html>", request.getContextPath(), sessionId);
		}
	}

	@RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
	public void viewSession(HttpServletResponse response, @PathVariable("id") String sessionId) throws IOException {
		if (managementPlugin.isEnableSessionViewer()) {
			response.setContentType("image/jpeg");
			SessionTrail<Snapshot> trail = sessionsManager.getTrail(sessionId);
			List<Snapshot> snapshots = trail.getSnapshots();
			TerminalSnapshot snapshot = (TerminalSnapshot)snapshots.get(snapshots.size() - 1);
			imageRenderer.render(snapshot, response.getOutputStream());
		}
	}

	@RequestMapping(value = "/disconnect/{id}", method = RequestMethod.GET)
	public String disconnect(Model uiModel, @PathVariable("id") String sessionId) throws IOException {
		if (managementPlugin.isEnableDisconnect()) {
			sessionsManager.disconnect(sessionId);
		}
		uiModel.addAttribute("sessionsProperties", sessionsManager.getSessionsProperties());
		uiModel.addAttribute(managementPlugin);
		return "Management";
	}
}
