package org.openlegacy.rpc.samples.model.web;

import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.samples.model.Items;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller
@RequestMapping("/Items")
public class ItemsController {

	@Inject
	private RpcSession rpcSession;

	@RequestMapping
	public String show(Model uiModel) {
		Items items = new Items();
		items = rpcSession.doAction(RpcActions.READ(), items);

		uiModel.addAttribute("items", items);
		return "Items";
	}

}
