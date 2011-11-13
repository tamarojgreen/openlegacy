package org.openlegacy.samples.mvc.controllers;

import java.util.List;

import javax.inject.Inject;

import org.openlegacy.samples.model.ItemsList;
import org.openlegacy.samples.model.MainMenu;
import org.openlegacy.samples.model.ItemsList.ItemsListRow;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ItemsListController {

	@Inject
	private TerminalSession terminalSession;

	// URL
    @RequestMapping(value="/items", method = RequestMethod.GET)
    public String show(Model uiModel) {
    	// page model
        ItemsList itemsList = terminalSession.getEntity(ItemsList.class);
		List<ItemsListRow> itemListRows = itemsList.getItemListRows();
		uiModel.addAttribute("itemsList", itemListRows);
        // jsp view name
        return "/items";
    }
    
	
}
