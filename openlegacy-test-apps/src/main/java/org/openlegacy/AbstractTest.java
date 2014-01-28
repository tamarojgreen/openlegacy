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
package org.openlegacy;

import org.apache.commons.io.IOUtils;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import javax.inject.Inject;

public class AbstractTest {

	@Inject
	private ApplicationContext applicationContext;

	protected TerminalSession newTerminalSession() {
		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		return terminalSession;
	}

	protected String readResource(String resourceName) throws IOException {
		ClassPathResource resource = new ClassPathResource(resourceName);
		return IOUtils.toString(resource.getInputStream());
	}

}
