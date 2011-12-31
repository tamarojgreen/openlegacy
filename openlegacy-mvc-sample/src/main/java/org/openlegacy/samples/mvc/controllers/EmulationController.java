package org.openlegacy.samples.mvc.controllers;

import javax.inject.Inject;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.web.JsonSerializationUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class EmulationController {

	
	@Inject
	private TerminalSession terminalSession;
	

	@RequestMapping(value = "/Emulation", method = RequestMethod.GET)
	public String show() {
		return "Emulation";
	}
	
    @RequestMapping(value = "/Emulation.json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getJson() {
    	return returnJson();
    }

    @RequestMapping(value = "/Emulation.json", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> postJson() {
    	terminalSession.doAction(TerminalActions.ENTER());
    	return returnJson();
    }
    
	private ResponseEntity<String> returnJson() {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        String result = JsonSerializationUtil.toJson(terminalSession.getSnapshot());
        
        return new ResponseEntity<String>(result, headers, HttpStatus.OK);
	}

	
}
