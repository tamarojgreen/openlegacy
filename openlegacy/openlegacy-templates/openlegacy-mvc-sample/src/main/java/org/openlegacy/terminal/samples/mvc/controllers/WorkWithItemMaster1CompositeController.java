package org.openlegacy.terminal.samples.mvc.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
