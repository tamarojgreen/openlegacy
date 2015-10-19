package org.openlegacy.cache.tests.stuff;

import org.apache.commons.lang.SerializationUtils;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.exceptions.OpenlegacyRemoteRuntimeException;
import org.openlegacy.rpc.MockRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.modules.trail.RpcPersistedTrail;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

public class CrudMockRpcConnectionFactory implements MockRpcConnectionFactory {

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
		List<RpcSnapshot> clonedSnapshots = new ArrayList<RpcSnapshot>();
		for (RpcSnapshot rpcSnapshot : fetchRpcSnapshots()) {
			clonedSnapshots.add((RpcSnapshot)SerializationUtils.clone(rpcSnapshot));
		}
		CrudMockRpcConnection result = new CrudMockRpcConnection(clonedSnapshots);

		result.setVerifySend(verifySend);
		return result;
	}

	private List<RpcSnapshot> fetchRpcSnapshots() {
		if (snapshots == null) {
			RpcPersistedTrail trail = null;
			String trailFilePath = openLegacyProperties.getTrailFilePath();
			if (trailFilePath != null) {
				trailName = trailFilePath;
				root = "";
			}
			try {

				String trailClasspath = MessageFormat.format("{0}{1}", root, trailName);
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
		try {
			rpcConnection.disconnect();
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

	public void setRoot(String root) {
		if (!root.endsWith("/")) {
			root = root + "/";
		}
		this.root = root;
	}

	public void setTrailName(String trailName) {
		this.trailName = trailName;
	}

}