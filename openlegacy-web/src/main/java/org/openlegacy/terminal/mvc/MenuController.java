/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.mvc.AbstractRestController;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests for dijit.Tree widget
 * 
 * @author Imivan
 * 
 */
@Controller
@RequestMapping("/menu/*")
public class MenuController {

	private final static Log logger = LogFactory.getLog(MenuController.class);

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	private MenuItem menus = null;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getMenuId(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

		try {
			if (menus == null || openLegacyProperties.isDesigntime()) {
				menus = terminalSession.getModule(Menu.class).getMenuTree();
			}
			if (menus == null) {
				return "{}";
			}
			MenuItem item = findByTreeId(menus, id);
			return generateJson(item, id, null, false);
		} catch (RuntimeException e) {
			handleException(response, e);
		}
		return null;
	}

	private MenuItem findByTreeId(MenuItem menuItem, String id) {
		if (getMenuUniqueId(menuItem).equals(id) || id.equalsIgnoreCase("root")) {
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
				sb.append(generateJson(item, getMenuUniqueId(item), id, true));
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

	private static String getMenuUniqueId(MenuItem item) {
		return item.getTargetEntityName();
	}

	@RequestMapping(value = "/flatMenu", method = RequestMethod.GET, consumes = AbstractRestController.JSON)
	public Object getFlatMenuEntries(HttpServletResponse response) throws IOException {
		try {
			Menu menus = terminalSession.getModule(Menu.class);
			if (menus.getFlatMenuEntries().isEmpty()) {
				return null;
			}
			return menus.getFlatMenuEntries();
		} catch (RuntimeException e) {
			handleException(response, e);
		}
		return null;
	}

	private static ModelAndView handleException(HttpServletResponse response, RuntimeException e) throws IOException {
		response.setStatus(500);
		response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
		logger.fatal(e.getMessage(), e);
		return null;
	}
}
