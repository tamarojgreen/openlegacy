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
package org.openlegacy.rpc.mock;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.SerializationUtils;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.exceptions.UnableToLoadSnapshotException;
import org.openlegacy.rpc.MockHostRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.modules.trail.RpcPersistedTrail;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.util.Assert;

public class MockRpcConnectionFactory implements MockHostRpcConnectionFactory {

	private List<RpcSnapshot> snapshots = null;
	private String root;
	private String trailName;
	private boolean verifySend;
	
	@Inject
	private OpenLegacyProperties openLegacyProperties;

	public boolean isVerifySend() {
		return verifySend;
	}

	public void setVerifySend(boolean verifySend) {
		this.verifySend = verifySend;
	}

	@Override
	public RpcConnection getConnection() {
		String trailFilePath = openLegacyProperties.getTrailFilePath();
		
		if (StringUtil.isEmpty(trailFilePath)) {
			throw (new UnableToLoadSnapshotException("Can't define both trail and files for mock terminal connection factory"));
		}
		
		List<RpcSnapshot> clonedSnapshots = new ArrayList<RpcSnapshot>();
		for (RpcSnapshot rpcSnapshot : fetchRpcSnapshots()) {
			clonedSnapshots.add((RpcSnapshot)SerializationUtils.clone(rpcSnapshot));
		}
		MockRpcConnection result = new MockRpcConnection(clonedSnapshots);
		result.setVerifySend(verifySend);
		return result;
	}

	private List<RpcSnapshot> fetchRpcSnapshots() {
		if (snapshots == null) {
			RpcPersistedTrail trail = null;
			try {
				String trailClasspath = openLegacyProperties.getTrailFilePath();
				InputStream trailStream = getClass().getResourceAsStream(trailClasspath);
				Assert.notNull(trailStream, "Trail file not found. In development, Verify it exist in a src/main/resources");
				trail = XmlSerializationUtil.deserialize(RpcPersistedTrail.class, trailStream);
				snapshots = trail.getSnapshots();
			} catch (JAXBException e) {
				throw (new IllegalArgumentException(MessageFormat.format("Faild reading XML trail:{0}", trailName), e));
			}
		}
		return snapshots;
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public void setTrailName(String trailName) {
		this.trailName = trailName;
	}

}
