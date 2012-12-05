package org.openlegacy.as400.menus.web;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for screen ProgrammingDevelopmentManagerpdmComposite
 */
@Controller
@RequestMapping("/ProgrammingDevelopmentManagerpdm")
public class ProgrammingDevelopmentManagerpdmCompositeController {

	// handle page initial display
    @RequestMapping( method = RequestMethod.GET)
    public String show( Model uiModel) {
        return "ProgrammingDevelopmentManagerpdmComposite";
    }
}
