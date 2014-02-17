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
package org.openlegacy.rpc.mvc;

import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.render.RpcImageRenderer;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/entityViewer")
public class EntityViewerController {

	@Inject
	private RpcEntitiesRegistry entitiesRegistry;

	@Inject
	private RpcImageRenderer imageRenderer;

	@RequestMapping(value = "{entity}", method = RequestMethod.GET)
	public @ResponseBody
	String viewer(HttpServletRequest request, @PathVariable("entity") String entityName) throws IOException {
		String style = "";
		if (request.getParameter("small") != null) {
			style = "style=\"width:300px;height;200px\";";
		}
		return MessageFormat.format(
				"<html><body><img id=\"entityImage\" src=\"{0}/entityViewer/{1}/image?x={2}\"/ {3}></body></html>",
				request.getContextPath(), entityName, System.currentTimeMillis(), style);
	}

	@RequestMapping(value = "{entity}/image", method = RequestMethod.GET)
	public void entityImage(HttpServletResponse response, @PathVariable("entity") String entityName) throws IOException {
		RpcEntityDefinition entityDefinition = entitiesRegistry.get(entityName);
		if (entityDefinition != null && entityDefinition.getSourceCode() != null) {
			response.setContentType("image/jpeg");
			imageRenderer.render(entityDefinition.getSourceCode(), response.getOutputStream());
		}
	}

}
