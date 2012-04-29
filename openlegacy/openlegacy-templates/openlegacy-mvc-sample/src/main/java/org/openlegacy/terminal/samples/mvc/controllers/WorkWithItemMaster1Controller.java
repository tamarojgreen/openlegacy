package org.openlegacy.terminal.samples.mvc.controllers;

import org.openlegacy.demo.db.model.StockItem;
import org.openlegacy.demo.db.services.StockItemsService;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.samples.model.WorkWithItemMaster1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles requests for screen WorkWithItemMaster1
 */
@Controller
@RequestMapping("/WorkWithItemMaster1")
public class WorkWithItemMaster1Controller {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private StockItemsService stockItemsService;

	// handle page initial display
	@RequestMapping(method = RequestMethod.GET, params = "partial=1")
	public String show(Model uiModel) {

		WorkWithItemMaster1 workWithItemMaster1 = terminalSession.getEntity(WorkWithItemMaster1.class);
		uiModel.addAttribute(workWithItemMaster1);

		StockItem stockItem = stockItemsService.getOrCreateStockItem(workWithItemMaster1.getItemNumber());
		uiModel.addAttribute(stockItem);

		return "WorkWithItemMaster1";
	}

	// handle submit action
	@RequestMapping(method = RequestMethod.POST)
	public String submit(WorkWithItemMaster1 workWithItemMaster1, Model uiModel, HttpServletRequest request) {
		ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.ENTER(), workWithItemMaster1);

		StockItem stockItem = stockItemsService.getOrCreateStockItem(workWithItemMaster1.getItemNumber());
		stockItem.setDescription(request.getParameter("description"));
		stockItemsService.updateStockItem(stockItem);

		// go to the controller for the resulting screen name
		if (resultScreenEntity != null) {
			workWithItemMaster1 = terminalSession.getEntity(WorkWithItemMaster1.class);
			uiModel.addAttribute(workWithItemMaster1);
			uiModel.addAttribute(stockItem);
			return "WorkWithItemMaster1";
		}

		return "redirect:/";

	}
	// actions are generated within WorkWithItemMaster1Controller_Aspect.aj

}
