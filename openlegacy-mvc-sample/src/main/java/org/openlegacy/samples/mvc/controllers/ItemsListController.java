package org.openlegacy.samples.mvc.controllers;

import java.util.List;

import javax.inject.Inject;

import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import apps.inventory.screens.ItemsList;
import apps.inventory.screens.ItemsList.ItemsListRow;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ItemsListController {

	@Inject
	private TerminalSession terminalSession;

	// URL
    @RequestMapping(value="/ItemsList", method = RequestMethod.GET)
    public String show(Model uiModel) {
    	// page model
    	ItemsList itemsList = terminalSession.getEntity(ItemsList.class);
        List<ItemsListRow> allItemRows = terminalSession.getModule(Table.class).collectAll(ItemsList.class, ItemsListRow.class);
		uiModel.addAttribute("itemsList", itemsList);
		uiModel.addAttribute("allItemRows", allItemRows);
        // jspx view name
        return "ItemsList";
    }
    
	
}
