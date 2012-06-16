package org.openlegacy.terminal.samples.mvc.controllers;

import flexjson.JSONSerializer;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.samples.model.WorkWithItemMaster;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * Handles requests for screen WorkWithItemMaster
 */
@Controller
@RequestMapping("/WorkWithItemMaster")
public class WorkWithItemMasterController {

	@Inject
	private TerminalSession terminalSession;

	// actions are generated within WorkWithItemMasterController_Aspect.aj

	// handle ajax request for warehouseType field
	@RequestMapping(value = "/more", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> more() {
		WorkWithItemMaster workWithItemMaster = terminalSession.getEntity(WorkWithItemMaster.class);
		workWithItemMaster = terminalSession.doAction(TerminalActions.PAGEDOWN(), workWithItemMaster);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");

		String result = new JSONSerializer().serialize(workWithItemMaster.getWorkWithItemMasterRecords());
		return new ResponseEntity<String>(result, headers, HttpStatus.OK);
	}

}
