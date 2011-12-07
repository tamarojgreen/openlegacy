package org.openlegacy.samples.mvc.controllers;

import javax.inject.Inject;

import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemsList;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ItemsDetailsController {

	@Inject
	private TerminalSession terminalSession;

	// URL
    @RequestMapping(value="/ItemDetails/{itemNumber}", method = RequestMethod.GET)
    public String show(@PathVariable int itemNumber,Model uiModel) {
    	ItemDetails1 itemDetails1 = terminalSession.getModule(Table.class).drillDown(ItemsList.class, ItemDetails1.class, TerminalDrilldownActions.enter("2"), itemNumber);
		uiModel.addAttribute("itemDetails1", itemDetails1);
        // jspx view name
        return "ItemDetails";
    }
    
	
}
