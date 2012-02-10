package org.openlegacy.newmvc.web;

import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LogoffController {

	@Inject
	private TerminalSession terminalSession;

	@RequestMapping(value = "/logoff", method = RequestMethod.GET)
	public String logoff() {
		terminalSession.disconnect();
		return "logoff";
	}

}
