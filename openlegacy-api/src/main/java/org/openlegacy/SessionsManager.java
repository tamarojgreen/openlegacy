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
package org.openlegacy;

import org.openlegacy.modules.trail.SessionTrail;

import java.util.Set;

/**
 * A session manager is responsible for exposing session information to management tools such as JMX, web console plug-in
 * 
 * @author Roi Mor
 * 
 * @param <S>
 *            The type of managed session
 */
public interface SessionsManager<S extends Session> {

	/**
	 * 
	 * @return all sessions properties
	 */
	Set<SessionProperties> getSessionsProperties();

	SessionProperties getSessionProperties(String sessionId);

	<SN extends Snapshot> SessionTrail<SN> getTrail(String sessionId);

	void disconnect(String sessionId);
}
