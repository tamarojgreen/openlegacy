package org.openlegacy.rpc.samples.mvc.controllers;

import org.apache.commons.io.IOUtils;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.samples.model.ItemDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ItemDetails")
public class ItemDetailsController {

	@Inject
	private RpcSession rpcSession;

	@RequestMapping(value = "/help")
	public @ResponseBody
	String help(HttpServletRequest request) throws IOException {
		URL resource = request.getSession().getServletContext().getResource("/help/ItemDetails.html");
		return IOUtils.toString(resource.openStream());
	}

	@RequestMapping(value = "/{key:[\\w+[-_ ]*\\w+]+}", method = RequestMethod.GET)
	public String show(Model uiModel, @PathVariable("key") String key) {

		ItemDetails itemDetails = rpcSession.getEntity(ItemDetails.class, key);

		uiModel.addAttribute("itemDetails", itemDetails);
		return "ItemDetails";
	}

	@RequestMapping(method = RequestMethod.POST, params = "action=update")
	public String update(ItemDetails newItemDetails, Model uiModel) {
		newItemDetails = rpcSession.doAction(RpcActions.UPDATE(), newItemDetails);

		uiModel.addAttribute("itemDetails", newItemDetails);
		return "ItemDetails";
	}
}
