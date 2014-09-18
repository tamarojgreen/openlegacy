package org.openlegacy.terminal.samples.mvc.controllers;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.WarehouseDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Handles requests for screen WarehouseDetails
 */
@Controller
@RequestMapping("/WarehouseDetails")
public class WarehouseDetailsController {

	@Inject
	private TerminalSession terminalSession;

	// actions are generated within WarehouseDetailsController_Aspect.aj

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

	// handle page navigation with friendly URL for drill-down
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
	public String items(@PathVariable("id") Integer itemNumber, Model uiModel) {
		return performDrillDownAction(itemNumber, uiModel, "6");
	}

	// handle page navigation with friendly URL for drill-down
	@RequestMapping(value = "/locations/{id}", method = RequestMethod.GET)
	public String locations(@PathVariable("id") Integer itemNumber, Model uiModel) {
		return performDrillDownAction(itemNumber, uiModel, "7");
	}

	private String performDrillDownAction(Integer itemNumber, Model uiModel, String actionValue) {
		WarehouseDetails WarehouseDetails = terminalSession.getEntity(WarehouseDetails.class, itemNumber);
		uiModel.addAttribute("warehouseDetails", WarehouseDetails);

		return "WarehouseDetails";
	}

}
