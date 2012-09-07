package org.openlegacy.terminal.samples.mvc.controllers;

import org.openlegacy.demo.db.model.StockItem;
import org.openlegacy.demo.db.model.StockItemNote;
import org.openlegacy.demo.db.services.StockItemsService;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions;
import org.openlegacy.terminal.samples.model.WorkWithItemMaster1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

import javax.inject.Inject;

/**
 * Handles requests for screen WorkWithItemMaster1Composite
 */
@Controller
@RequestMapping("/WorkWithItemMaster1")
public class WorkWithItemMaster1CompositeController {

	@Inject
	private StockItemsService stockItemsService;

	@Inject
	private TerminalSession terminalSession;

	// handle page initial display
	@RequestMapping(method = RequestMethod.GET)
	public String show(Model uiModel) {
		// get the item number from the host session
		Integer itemNumber = terminalSession.getEntity(WorkWithItemMaster1.class).getItemNumber();

		addNotesToPage(uiModel, itemNumber);

		return "WorkWithItemMaster1Composite";
	}

	private void addNotesToPage(Model uiModel, Integer itemNumber) {
		// fetch relevant notes from the DB and pass the page
		StockItem stockItem = stockItemsService.getOrCreateStockItem(itemNumber);

		Collection<StockItemNote> notes = stockItem.getNotes().values();
		uiModel.addAttribute("notes", notes);
	}

	// handle page navigation with friendly URL for drill-down
	@RequestMapping(value = "/revise/{id}", method = RequestMethod.GET)
	public String revise(@PathVariable("id") Integer itemNumber, Model uiModel) {
		return performDrillDownAction(itemNumber, uiModel, "2");
	}

	// handle page navigation with friendly URL for drill-down
	@RequestMapping(value = "/display/{id}", method = RequestMethod.GET)
	public String display(@PathVariable("id") Integer itemNumber, Model uiModel) {
		return performDrillDownAction(itemNumber, uiModel, "5");
	}

	// handle page navigation with friendly URL for drill-down
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Integer itemNumber, Model uiModel) {
		return performDrillDownAction(itemNumber, uiModel, "4");
	}

	private String performDrillDownAction(Integer itemNumber, Model uiModel, String actionValue) {
		terminalSession.getModule(Table.class).drillDown(WorkWithItemMaster1.class, TerminalDrilldownActions.enter(actionValue),
				itemNumber);

		addNotesToPage(uiModel, itemNumber);

		return "WorkWithItemMaster1Composite";
	}

	@RequestMapping(value = "/updateNote", method = RequestMethod.GET)
	public @ResponseBody
	String updateNode(@RequestParam("itemNumber") Integer itemNumber, @RequestParam("noteId") String noteId,
			@RequestParam("text") String text) {

		stockItemsService.addOrUpdateNote(itemNumber, noteId, text);
		return "";
	}

}
