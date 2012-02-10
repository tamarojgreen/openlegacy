package org.openlegacy.newmvc.web;

import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HtmlEmulationController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TerminalSnapshotHtmlRenderer snapshotHtmlRenderer;

	@Inject
	private TerminalSendActionBuilder<HttpServletRequest> terminalSendActionBuilder;

	@Inject
	private HttpServletRequest request;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String show(Model uiModel) {
		String result = snapshotHtmlRenderer.render(terminalSession.getSnapshot());
		uiModel.addAttribute("terminalHtml", result);
		return "HtmlEmulation";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String post(Model uiModel) {

		TerminalSendAction terminalSendAction = terminalSendActionBuilder.buildSendAction(terminalSession.getSnapshot(), request);
		terminalSession.doAction(terminalSendAction);
		return show(uiModel);
	}

}
