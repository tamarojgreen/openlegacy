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
package org.openlegacy.terminal.support;

import org.openlegacy.ApplicationConnectionListener;
import org.openlegacy.support.SessionModuleAdapter;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionModule;
import org.openlegacy.utils.SpringUtil;

import java.io.Serializable;

/**
 * Define a terminal session override-able methods which happens before & after a terminal session action
 * 
 */
public abstract class TerminalSessionModuleAdapter extends SessionModuleAdapter<TerminalSession> implements TerminalSessionModule, ApplicationConnectionListener, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * for serialization purpose only
	 */
	public TerminalSessionModuleAdapter() {}

	public void destroy() {
		// allow override
	}

	public Object readResolve() {
		TerminalSession bean = SpringUtil.getApplicationContext().getBean(TerminalSession.class);
		setSession(bean);
		return this;
	}
}
