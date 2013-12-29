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
package org.openlegacy.web;

import net.sf.uadetector.UADetectorServiceFactory;
import net.sf.uadetector.UserAgent;
import net.sf.uadetector.UserAgentStringParser;

import org.openlegacy.SessionProperties;
import org.openlegacy.SessionPropertiesFiller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class RequestBasedSessionPropertiesFiller implements SessionPropertiesFiller {

	@Inject
	private HttpServletRequest request;

	public void fillProperties(SessionProperties sessionProperties) {
		// Get an UserAgentStringParser and analyze the requesting client
		UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
		UserAgent agent = parser.parse(request.getHeader("User-Agent"));

		sessionProperties.setProperty("IP", request.getRemoteAddr());
		sessionProperties.setProperty("OS", agent.getOperatingSystem().getName());
		sessionProperties.setProperty("browser", agent.getName());
		sessionProperties.setProperty("version", agent.getVersionNumber().getMajor() + "-" + agent.getVersionNumber().getMinor());
	}
}
