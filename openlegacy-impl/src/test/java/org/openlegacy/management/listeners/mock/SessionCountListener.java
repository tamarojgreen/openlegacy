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
package org.openlegacy.management.listeners.mock;

import org.openlegacy.SessionPoolListner;

/**
 * @author Aleksey Yeremeyev
 * 
 */
public class SessionCountListener implements SessionPoolListner {

	/* (non-Javadoc)
	 * @see org.openlegacy.SessionPoolListner#newSession()
	 */
	@Override
	public void newSession() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openlegacy.SessionPoolListner#endSession()
	 */
	@Override
	public void endSession() {
		// TODO Auto-generated method stub

	}

}
