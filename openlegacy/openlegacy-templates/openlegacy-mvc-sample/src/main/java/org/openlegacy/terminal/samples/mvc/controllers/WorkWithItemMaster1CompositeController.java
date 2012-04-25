package org.openlegacy.terminal.samples.mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for screen WorkWithItemMaster1Composite
 */
@Controller
@RequestMapping("/WorkWithItemMaster1")
public class WorkWithItemMaster1CompositeController {

	// handle page initial display
	@RequestMapping(method = RequestMethod.GET)
	public String show(Model uiModel) {
		return "WorkWithItemMaster1Composite";
	}

	@RequestMapping(value = "/updateNote", method = RequestMethod.GET)
	public @ResponseBody
	String updateNode() {
		System.out.println("On the server");
		return "";
	}

}
