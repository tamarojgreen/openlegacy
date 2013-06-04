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
package org.openlegacy.rpc.modules.trail;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.OutputStream;
import java.util.List;

public class RpcTrailXmlWriter implements TrailWriter {

	public void write(SessionTrail<? extends Snapshot> trail, OutputStream out) {

		RpcPersistedTrail persistedTrail = new RpcPersistedTrail();
		List<? extends Snapshot> snapshots = trail.getSnapshots();
		for (Snapshot snapshot : snapshots) {
			persistedTrail.appendSnapshot((RpcSnapshot)snapshot);
		}

		try {
			XmlSerializationUtil.serialize(RpcPersistedTrail.class, persistedTrail, out);
		} catch (Exception e) {
			throw (new IllegalArgumentException("Faild writing XML trail", e));
		}

	}

}
