package org.openlegacy.rpc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.mock.MockRpcConnection;
import org.openlegacy.rpc.recognizers.RpcRecognizer;
import org.openlegacy.support.SnapshotsList;
import org.openlegacy.support.SnapshotsList.SnapshotInfo;
import org.openlegacy.utils.ReflectionUtil;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MockupRpcSession extends DefaultRpcSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(MockupRpcSession.class);

	private Map<Class<?>, SnapshotsList<RpcSnapshot>> snapshotsMap = new HashMap<Class<?>, SnapshotsList<RpcSnapshot>>();

	@Inject
	private RpcRecognizer rpcRecognizer;

	@Override
	protected MockRpcConnection getConnection() {
		return (MockRpcConnection)super.getConnection();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getEntity(Class<T> rpcEntityClass, Object... keys) throws EntityNotFoundException {

		SnapshotsList<RpcSnapshot> snapshotsList = snapshotsMap.get(rpcEntityClass);
		if (snapshotsList == null) {
			throw (new EntityNotFoundException(MessageFormat.format("The entity {0} was not found in the recorded trail",
					rpcEntityClass.getSimpleName())));
		}
		SnapshotInfo<RpcSnapshot> snapshotInfo = snapshotsList.getCurrent();

		getConnection().setCurrentIndex(snapshotInfo.getIndexInSession());

		T entity = ReflectionUtil.newInstance(rpcEntityClass);

		RpcInvokeAction rpcInvokeAction = snapshotInfo.getSnapshot().getRpcInvokeAction();
		converToApiFields(rpcInvokeAction.getFields());

		// We put into Result so we can use populateEntity that works on entities.
		SimpleRpcResult result = new SimpleRpcResult();
		result.setRpcFields(rpcInvokeAction.getFields());
		populateEntity((RpcEntity)entity, null, result);
		snapshotsList.next();
		return (T)doAction(RpcActions.READ(), (RpcEntity)entity);

	}

	@Override
	public void setConnection(RpcConnection rpcConnection) {

		super.setConnection(rpcConnection);
		preserveSnapshots(getConnection());
	}

	private void preserveSnapshots(MockRpcConnection rpcConnection) {
		List<RpcSnapshot> snapshots = rpcConnection.getSnapshots();
		int count = 0;

		snapshotsMap.clear();
		for (RpcSnapshot rpcSnapshot : snapshots) {
			Class<?> matchedClass = rpcRecognizer.match(rpcSnapshot);
			if (matchedClass == null) {
				logger.warn("An unrecognized snapshot was found in the trail:");
				logger.warn(rpcSnapshot);
			} else {
				SnapshotsList<RpcSnapshot> snapshotsList = snapshotsMap.get(matchedClass);
				if (snapshotsMap.get(matchedClass) == null) {
					snapshotsList = new SnapshotsList<RpcSnapshot>();
					snapshotsMap.put(matchedClass, snapshotsList);
				}
				snapshotsList.add(rpcSnapshot, count);
			}
			count++;
		}
	}

}
