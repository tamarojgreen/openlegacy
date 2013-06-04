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

import org.openlegacy.mvc.web.interceptors.AbstractInterceptor;
import org.openlegacy.terminal.TerminalSession;

import javax.inject.Inject;

public abstract class AbstractScreensInterceptor extends AbstractInterceptor<TerminalSession> {

	@Inject
	private TerminalSession terminalSession;

	@Override
	protected TerminalSession getSession() {
		return terminalSession;
	}

}
