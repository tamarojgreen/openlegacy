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
package org.openlegacy.mvc;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.layout.PageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * OpenLegacy help controller
 * 
 * @author Roi Mor
 * 
 */
@Controller
public class OnlineHelpController {

	private static final String PAGE = "page";

	@Inject
	private EntitiesRegistry<?, ?, ?> entitiesRegistry;

	@Inject
	private PageBuilder<EntityDefinition<?>, ?> pageBuilder;

	@RequestMapping(value = "/{entity}/online-help", method = RequestMethod.GET)
	public String help(@PathVariable("entity") String entityName, Model uiModel) {
		EntityDefinition<?> entityDefintion = entitiesRegistry.get(entityName);
		uiModel.addAttribute(PAGE, pageBuilder.build(entityDefintion));

		return "help";

	}

}
