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
package org.openlegacy.rpc.modules.trail;

import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.persistance.RpcPersistedSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rpc-trail")
@XmlAccessorType(XmlAccessType.FIELD)
public class RpcPersistedTrail implements RpcSessionTrail {

	@XmlElement(name = "snapshot", type = RpcPersistedSnapshot.class)
	private List<RpcSnapshot> snapshots = new ArrayList<RpcSnapshot>();

	@Override
	public List<RpcSnapshot> getSnapshots() {
		return snapshots;
	}

	@Override
	public void appendSnapshot(RpcSnapshot snapshot) {
		snapshots.add(snapshot);
	}

	@Override
	public void clear() {
		snapshots.clear();
	}

	@Override
	public void advanceCurrent(int steps) {}

	@Override
	public RpcSnapshot getCurrent() {
		return snapshots.get(snapshots.size() - 1);
	}

}
