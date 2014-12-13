package org.openlegacy.terminal.spa;

import freemarker.template.TemplateException;

import org.openlegacy.spa.AbstractAngularController;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.web.render.support.DefaultTerminalSnapshotHtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AngularScreenController extends AbstractAngularController {

	@Inject
	private DefaultTerminalSnapshotHtmlRenderer htmlRenderer;

	@Inject
	private TerminalSession terminalSession;

	@RequestMapping(value = "app/views/emulation.html", method = RequestMethod.GET)
	public void getEmulation(HttpServletResponse response) throws IOException, TemplateException {
		htmlRenderer.setIncludeTemplate(false);
		htmlRenderer.setRenderActionButtons(true);
		htmlRenderer.setOnclickProperty("ng-click");
		String result = htmlRenderer.render(terminalSession.getSnapshot());
		response.getWriter().write(result);
	}
}
