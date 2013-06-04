package org.openlegacy.rpc.samples.model.web;

import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.samples.model.ItemDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
@RequestMapping("/ItemDetails")
public class ItemDetailsController {

	@Inject
	private RpcSession rpcSession;

	@RequestMapping(value = "/{key:[\\w+[-_ ]*\\w+]+}", method = RequestMethod.GET)
	public String show(Model uiModel, @PathVariable("key") String key) {
		ItemDetails itemDetails = rpcSession.getEntity(ItemDetails.class, key);

		uiModel.addAttribute("itemDetails", itemDetails);
		return "ItemDetails";
	}

	@RequestMapping(method = RequestMethod.POST, params = "action=update")
	public String update(ItemDetails ItemDetails, Model uiModel) {
		ItemDetails itemDetails = rpcSession.doAction(RpcActions.UPDATE(), ItemDetails);

		uiModel.addAttribute("itemDetails", itemDetails);
		return "ItemDetails";
	}
}
