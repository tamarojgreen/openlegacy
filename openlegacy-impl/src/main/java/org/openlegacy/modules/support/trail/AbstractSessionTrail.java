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
package org.openlegacy.modules.support.trail;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSessionTrail<S extends Snapshot> implements SessionTrail<S>, Serializable {

	private static final long serialVersionUID = 1L;

	private LinkedList<S> snapshots = new LinkedList<S>();

	private Integer historyCount = 3;

	public List<S> getSnapshots() {
		return snapshots;
	}

	public void appendSnapshot(S snapshot) {
		snapshots.add(snapshot);

		if (historyCount != null && snapshots.size() > historyCount) {
			snapshots.removeFirst();
		}
	}

	public Integer getHistoryCount() {
		return historyCount;
	}

	public void setHistoryCount(Integer historyCount) {
		this.historyCount = historyCount;
	}

	public void clear() {
		snapshots.clear();
	}
}
