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
package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;

import java.util.List;

/**
 * Defines a session trail. A session trail is compound of multiple {@link Snapshot}.
 * 
 * @author Roi Mor
 * @param <S>
 *            The snapshot type
 */
public interface SessionTrail<S extends Snapshot> {

	List<S> getSnapshots();

	void appendSnapshot(S snapshot);

	void clear();
}
