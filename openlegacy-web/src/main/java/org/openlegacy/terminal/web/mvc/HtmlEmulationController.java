package org.openlegacy.terminal.web.mvc;

import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.trail.DefaultTerminalTrail;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/HtmlEmulation")
public class HtmlEmulationController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TerminalSnapshotHtmlRenderer snapshotHtmlRenderer;

	@Inject
	private TerminalSendActionBuilder<HttpServletRequest> terminalSendActionBuilder;

	@Inject
	private HttpServletRequest request;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@RequestMapping(method = RequestMethod.GET)
	public String show(Model uiModel) {

		// check out if configured to save trail - if so, define unlimited recording size (design-time)
		String trailPath = openLegacyProperties.getProperty(OpenLegacyProperties.TRAIL_FOLDER_PATH);
		if (trailPath != null) {
			DefaultTerminalTrail trail = (DefaultTerminalTrail)terminalSession.getModule(Trail.class).getSessionTrail();
			trail.setHistoryCount(null);
		}

		String result = snapshotHtmlRenderer.render(terminalSession.getSnapshot());
		uiModel.addAttribute("terminalHtml", result);
		return "HtmlEmulation";
	}

	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public void image(HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");
		TerminalSnapshotImageRenderer.instance().render(terminalSession.getSnapshot(), response.getOutputStream());
	}

	@RequestMapping(value = "/sessionViewer", method = RequestMethod.GET)
	public @ResponseBody
	String viewer(HttpServletRequest request) throws IOException {
		return MessageFormat.format("<html><body><image src=\"{0}/HtmlEmulation/image\"</body></html>", request.getContextPath());
	}

	@RequestMapping(method = RequestMethod.POST)
	public String post(Model uiModel) {

		TerminalSendAction terminalSendAction = terminalSendActionBuilder.buildSendAction(terminalSession.getSnapshot(), request);
		terminalSession.doAction(terminalSendAction);
		return show(uiModel);
	}

}
