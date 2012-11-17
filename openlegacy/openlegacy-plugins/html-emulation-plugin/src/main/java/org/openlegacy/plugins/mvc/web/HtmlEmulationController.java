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
package org.openlegacy.plugins.mvc.web;

import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/emulation")
public class HtmlEmulationController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TerminalSnapshotHtmlRenderer snapshotHtmlRenderer;

	@Inject
	private TerminalSendActionBuilder<HttpServletRequest> terminalSendActionBuilder;

	@Inject
	private ScreenEntityUtils screenEntityUtils;

	private boolean emulationOnly = false;

	@RequestMapping(method = RequestMethod.GET)
	public String show(Model uiModel) {

		String result = snapshotHtmlRenderer.render(terminalSession.getSnapshot());
		uiModel.addAttribute("terminalHtml", result);
		return "HtmlEmulation";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String post(Model uiModel, HttpServletRequest request) {

		TerminalSendAction terminalSendAction = terminalSendActionBuilder.buildSendAction(terminalSession.getSnapshot(), request);
		terminalSession.doAction(terminalSendAction);
		if (!emulationOnly) {
			ScreenEntity currentEntity = terminalSession.getEntity();
			if (currentEntity != null) {
				String currentEntityName = screenEntityUtils.getEntityName(currentEntity);
				return "redirect:" + currentEntityName;

			}
		}
		return show(uiModel);
	}

	/**
	 * Define whether to show HTML emulation only and not try to navigate to identified entities
	 * 
	 * @param emulationOnly
	 *            whether to show HTML emulation only
	 */
	public void setEmulationOnly(boolean emulationOnly) {
		this.emulationOnly = emulationOnly;
	}
}
