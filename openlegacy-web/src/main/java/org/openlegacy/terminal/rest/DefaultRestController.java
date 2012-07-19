package org.openlegacy.terminal.rest;

import javax.inject.Inject;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultRestController {

	
	@Inject
	private TerminalSession terminalSession;
	
	@RequestMapping("/{screen}")
	public void getScreenEntity(@PathVariable("screen") String screenEntityName, ModelMap model) {
 
		Object screen = terminalSession.getEntity(screenEntityName);
		screen = ProxyUtil.getTargetObject(screen );
		model.addAttribute("model", screen);
	}
}
