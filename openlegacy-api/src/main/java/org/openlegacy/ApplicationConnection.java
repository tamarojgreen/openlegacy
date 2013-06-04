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

/**
 * Represent an connection to an abstract application. Currently implemented for
 * {@link org.openlegacy.terminal.TerminalConnection}.
 * 
 * Provides API for retrieving a snapshot with/without cached, and API for common abstract application interaction
 * 
 * @author Roi Mor
 * 
 * @param <S>
 *            The type of snapshot used
 * @param <A>
 *            The type of send action used
 */
public interface ApplicationConnection<S extends Snapshot, A extends RemoteAction> {

	S getSnapshot();

	S fetchSnapshot();

	Object getDelegate();

	boolean isConnected();

	void disconnect();

	void doAction(A sendAction);

	Integer getSequence();
}
