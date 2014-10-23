package org.openlegacy.rpc.samples.mvc.controllers;

import org.apache.commons.io.IOUtils;
import org.openlegacy.rpc.actions.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.samples.model.Items;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/Items")
public class ItemsController {

	@Inject
	private RpcSession rpcSession;

	@RequestMapping(value = "/help")
	public @ResponseBody
	String help(HttpServletRequest request) throws IOException {
		URL resource = request.getSession().getServletContext().getResource("/help/Items.html");
		return IOUtils.toString(resource.openStream());
	}
	
	@RequestMapping
	public String show(Model uiModel) {
		Items items = rpcSession.getEntity(Items.class);
		items = rpcSession.doAction(RpcActions.READ(), items);
		uiModel.addAttribute("items", items);
		return "Items";
	}

}
