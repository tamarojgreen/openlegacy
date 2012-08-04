package org.openlegacy.terminal.samples.mvc.controllers;

import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions;
import org.openlegacy.terminal.samples.model.WorkWithWarehouseDetails1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Handles requests for screen WorkWithWarehouseDetails1
 */
@Controller
@RequestMapping("/WorkWithWarehouseDetails1")
public class WorkWithWarehouseDetails1Controller {

	@Inject
	private TerminalSession terminalSession;

	// actions are generated within WorkWithWarehouseDetails1Controller_Aspect.aj

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
		terminalSession.getModule(Table.class).drillDown(WorkWithWarehouseDetails1.class,
				TerminalDrilldownActions.enter(actionValue), itemNumber);

		WorkWithWarehouseDetails1 workWithWarehouseDetails1 = terminalSession.getEntity(WorkWithWarehouseDetails1.class);
		uiModel.addAttribute("workWithWarehouseDetails1", workWithWarehouseDetails1);

		return "WorkWithWarehouseDetails1";
	}

}
