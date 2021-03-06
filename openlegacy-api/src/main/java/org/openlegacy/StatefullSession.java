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

/**
 * Represents a logical state-full session. Returns a snapshot of the current state of the session
 * 
 * @author Roi Mor
 * 
 */
public interface StatefullSession<S extends Snapshot> extends Session {

	S getSnapshot();

	S fetchSnapshot();

}
