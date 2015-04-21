/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.remoting.web;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.Session;
import org.openlegacy.remoting.terminal.RemotingTerminalSession;
import org.openlegacy.terminal.mvc.rest.DefaultScreensRestController;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
@Controller
public class DefaultRemotingScreensRestController extends DefaultScreensRestController {

	@Inject
	private RemotingTerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Override
	protected Session getSession() {
		return terminalSession;
	}

	@Override
	protected EntitiesRegistry<?, ?, ?> getEntitiesRegistry() {
		return screenEntitiesRegistry;
	}

}
