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
package org.openlegacy.terminal.mvc.web.interceptors;

import org.openlegacy.modules.messages.Messages;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepter class for spring MVC. Injects messages into the page context so they can be display within the web page
 * 
 * @author RoiM
 * 
 */
public class InsertMessagesInterceptor extends AbstractInterceptor {

	@Override
	protected void insertModelData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
		if (!getTerminalSession().isConnected()) {
			return;
		}
		Messages messagesModule = getTerminalSession().getModule(Messages.class);
		if (messagesModule == null) {
			return;
		}

		List<String> messages = messagesModule.getMessages();
		if (messages.size() > 0) {
			modelAndView.addObject("ol_messages", messages.toArray());
		}
		messagesModule.resetMessages();

	}
}
