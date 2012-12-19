/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.terminal.mvc;

import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * Handles requests for dijit.Tree widget
 * 
 * @author Imivan
 * 
 */
@Controller
@RequestMapping("/menu/*")
public class MenuController {

	@Inject
	private TerminalSession terminalSession;
	private MenuItem menus = null;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getMenuId(@PathVariable("id") String id) {
		if (menus == null) {
			menus = terminalSession.getModule(Menu.class).getMenuTree();
		}
		if (menus == null) {
			return "{}";
		}
		MenuItem item = findByTreeId(menus, id);
		return generateJson(item, id, null, false);
	}

	private MenuItem findByTreeId(MenuItem menuItem, String id) {
		if (menuItem.getDisplayName().toLowerCase().replace(" ", "").equals(id) || id.equalsIgnoreCase("root")) {
			return menuItem;
		}
		for (MenuItem item : menuItem.getMenuItems()) {
			MenuItem byTreeId = findByTreeId(item, id);
			if (byTreeId != null) {
				return byTreeId;
			}
		}
		return null;
	}

	private static String generateJson(MenuItem menu, String id, String parentId, Boolean isChild) {
		StringBuilder sb = new StringBuilder();
		// {id:'',name:''
		sb.append("{id:'").append(id).append("',name:'").append(menu.getDisplayName()).append("'");
		if ((menu.getMenuItems().size() > 0) && (!isChild)) {
			// ,children:[
			sb.append(",children:[");
			for (MenuItem item : menu.getMenuItems()) {
				sb.append(generateJson(item, item.getDisplayName().toLowerCase().replace(" ", ""), id, true));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
		}
		if (isChild && (menu.getMenuItems().size() > 0)) {
			// ,children: true
			sb.append(",children: true");
		}
		if (isChild && (menu.getMenuItems().size() == 0)) {
			// ,url:''
			sb.append(",url:'").append(menu.getTargetEntity().getSimpleName()).append("'");
		}
		sb.append("}");
		return sb.toString();
	}
}
