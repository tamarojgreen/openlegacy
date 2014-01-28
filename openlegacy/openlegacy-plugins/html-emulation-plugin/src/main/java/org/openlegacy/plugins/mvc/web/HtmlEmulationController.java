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

import org.openlegacy.mvc.web.MvcConstants;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.modules.login.LoginMetadata;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.openlegacy.utils.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles requests for the application home page.
 */
@Controller("htmlEmulationController")
@RequestMapping(value = "/emulation")
public class HtmlEmulationController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private TerminalSnapshotHtmlRenderer snapshotHtmlRenderer;

	@Inject
	private TerminalSendActionBuilder<HttpServletRequest> terminalSendActionBuilder;

	@Inject
	private EntityUtils entityUtils;

	@Inject
	private LoginMetadata loginMetadata;

	boolean emulationOnly = false;

	@RequestMapping(method = RequestMethod.GET)
	public String show(Model uiModel, HttpServletRequest request, @RequestParam(value = "flip", required = false) Object flip,
			@RequestParam(value = "fetch", required = false) Object fetch,
			@RequestParam(value = "stick", required = false) Object stick) {

		if (!terminalSession.isConnected() && loginMetadata.getLoginScreenDefinition() != null) {
			return MvcConstants.LOGIN_URL;
		}
		if (fetch != null) {
			terminalSession.fetchSnapshot();
		}

		if (flip != null) {
			terminalSession.flip();
		}

		if (stick != null) {
			if (stick.equals("false")) {
				emulationOnly = false;
			} else {
				emulationOnly = true;
			}

		}
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
				ScreenEntityDefinition loginScreenDefinition = loginMetadata.getLoginScreenDefinition();
				if (loginScreenDefinition != null
						&& loginScreenDefinition.getEntityClass().isAssignableFrom(currentEntity.getClass())) {
					terminalSession.disconnect();
					return MvcConstants.LOGIN_URL;
				}

				String currentEntityName = entityUtils.getEntityName(currentEntity);
				return "redirect:" + currentEntityName;

			}
		}
		return show(uiModel, request, null, null, null);
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
