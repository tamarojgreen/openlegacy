package org.openlegacy.terminal.spa;

import freemarker.template.TemplateException;

import org.openlegacy.spa.AbstractAngularController;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AngularScreenController extends AbstractAngularController {

	@Inject
	private TerminalSnapshotHtmlRenderer htmlRenderer;

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TerminalSendActionBuilder<HttpServletRequest> terminalSendActionBuilder;

	@RequestMapping(value = "app/views/emulation.html", method = RequestMethod.GET)
	public void getEmulation(HttpServletResponse response) throws IOException, TemplateException {
		String result = htmlRenderer.render(terminalSession.getSnapshot());
		response.getWriter().write("<form id=\"EmulationForm\" method=\"post\">");
		response.getWriter().write(result);

		response.getWriter().write("<input type=\"button\" ng-click=\"doAction('enter')\" value=\"Enter\"/>");
		response.getWriter().write("</form>");
	}

	@RequestMapping(value = "app/", method = RequestMethod.POST)
	public void postEmulation(HttpServletResponse response, HttpServletRequest request) throws IOException, TemplateException {
		TerminalSendAction terminalSendAction = terminalSendActionBuilder.buildSendAction(terminalSession.getSnapshot(), request);
		terminalSession.doAction(terminalSendAction);
	}
}
