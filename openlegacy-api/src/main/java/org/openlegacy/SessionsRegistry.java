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

import java.util.Set;

/**
 * A registry container for all application sessions
 * 
 * @author Roi Mor
 * 
 * @param <S>
 *            The type of the managed session
 */
public interface SessionsRegistry<S extends Session> {

	Set<SessionProperties> getSessionsProperties();

	S getSession(String sessionId);

	void register(S session);

	void unregister(S session);
}
