// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).
package org.openlegacy.mvc.frontend.controllers;

import org.apache.commons.io.IOUtils;
import org.openlegacy.mvc.remoting.entities.ItemDetails;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

privileged aspect ItemDetailsController_Aspect {

	@Inject
	private ScreenEntitiesRegistry ItemDetailsController.screenEntitiesRegistry;

	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public @ResponseBody
	String ItemDetailsController.systemHelp(HttpServletRequest request) throws IOException {
		URL resource = request.getSession().getServletContext().getResource("/help/ItemDetails.html");
		String result = "";
		if (resource != null) {
			result = IOUtils.toString(resource.openStream());
		}
		return result;
	}

	// handle help action
	@RequestMapping(params = "action=help", method = RequestMethod.POST)
	public String ItemDetailsController.help(ItemDetails ItemDetails, Model uiModel, HttpServletRequest httpServletRequest) {
		ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.F1(), ItemDetails);
		// go to the controller for the resulting screen name
		if (resultScreenEntity != null) {
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null) {
				ItemDetails = terminalSession.getEntity(ItemDetails.class);
				uiModel.addAttribute("itemDetails", ItemDetails);
				return "ItemDetails";
			}
			return "redirect:" + screenEntityName;
		}
		return "redirect:/";

	}

	// handle prompt action
	@RequestMapping(params = "action=prompt", method = RequestMethod.POST)
	public String ItemDetailsController.prompt(ItemDetails ItemDetails, Model uiModel, HttpServletRequest httpServletRequest) {
		ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.F4(), ItemDetails);
		// go to the controller for the resulting screen name
		if (resultScreenEntity != null) {
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null) {
				ItemDetails = terminalSession.getEntity(ItemDetails.class);
				uiModel.addAttribute("itemDetails", ItemDetails);
				return "ItemDetails";
			}
			return "redirect:" + screenEntityName;
		}
		return "redirect:/";

	}

	// handle cancel action
	@RequestMapping(params = "action=cancel", method = RequestMethod.POST)
	public String ItemDetailsController.cancel(ItemDetails ItemDetails, Model uiModel, HttpServletRequest httpServletRequest) {
		ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.F12(), ItemDetails);
		// go to the controller for the resulting screen name
		if (resultScreenEntity != null) {
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null) {
				ItemDetails = terminalSession.getEntity(ItemDetails.class);
				uiModel.addAttribute("itemDetails", ItemDetails);
				return "ItemDetails";
			}
			return "redirect:" + screenEntityName;
		}
		return "redirect:/";

	}

	// handle delete action
	@RequestMapping(params = "action=delete", method = RequestMethod.POST)
	public String ItemDetailsController.delete(ItemDetails ItemDetails, Model uiModel, HttpServletRequest httpServletRequest) {
		ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.F2(), ItemDetails);
		// go to the controller for the resulting screen name
		if (resultScreenEntity != null) {
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null) {
				ItemDetails = terminalSession.getEntity(ItemDetails.class);
				uiModel.addAttribute("itemDetails", ItemDetails);
				return "ItemDetails";
			}
			return "redirect:" + screenEntityName;
		}
		return "redirect:/";

	}

	@InitBinder
	public void ItemDetailsController.initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

}