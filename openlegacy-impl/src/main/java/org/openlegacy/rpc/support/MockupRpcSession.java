package org.openlegacy.rpc.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcPojoFieldAccessor;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.mock.MockRpcConnection;
import org.openlegacy.rpc.recognizers.RpcRecognizer;
import org.openlegacy.rpc.utils.HierarchyRpcPojoFieldAccessor;
import org.openlegacy.rpc.utils.SimpleHierarchyRpcPojoFieldAccessor;
import org.openlegacy.support.SnapshotsList;
import org.openlegacy.support.SnapshotsList.SnapshotInfo;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.util.Assert;

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
	public RpcConnection getConnection() {
		return (RpcConnection)super.getConnection();
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

		((MockRpcConnection)getConnection()).setCurrentIndex(snapshotInfo.getIndexInSession());

		T entity = ReflectionUtil.newInstance(rpcEntityClass);

		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(rpcEntityClass);
		List<? extends FieldDefinition> keysDefinitions = rpcDefinition.getKeys();
		Assert.isTrue(
				keysDefinitions.size() == keys.length,
				MessageFormat.format("Provided keys {0} doesnt match entity {1} keys", StringUtils.join(keys, "-"),
						rpcDefinition.getEntityName()));
		int index = 0;

		HierarchyRpcPojoFieldAccessor fieldAccesor = new SimpleHierarchyRpcPojoFieldAccessor(entity);
		for (FieldDefinition fieldDefinition : keysDefinitions) {

			RpcPojoFieldAccessor directFieldAccessor = fieldAccesor.getPartAccessor(fieldDefinition.getName());
			directFieldAccessor.setFieldValue(StringUtil.removeNamespace(fieldDefinition.getName()), keys[index]);
			index++;
		}
		RpcInvokeAction rpcInvokeAction = snapshotInfo.getSnapshot().getRpcInvokeAction();
		converToApiFields(rpcInvokeAction.getFields());

		snapshotsList.next();
		return (T)doAction(RpcActions.READ(), (RpcEntity)entity);

	}

	@Override
	public RpcEntity doAction(RpcAction action, RpcEntity rpcEntity) {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());

		SimpleRpcInvokeAction rpcAction = new SimpleRpcInvokeAction();
		RpcActionDefinition actionDefinition = (RpcActionDefinition)rpcDefinition.getAction(action.getClass());
		if (actionDefinition != null) {
			rpcAction.setRpcPath(actionDefinition.getProgramPath());
		}
		populateRpcFields(rpcEntity, rpcDefinition, rpcAction);
		converToLegacyFields(rpcAction);
		RpcResult rpcResult = invoke(rpcAction, rpcEntity.getClass().getSimpleName());
		RpcResult rpcResultCopy = rpcResult.clone();

		converToApiFields(rpcResultCopy.getRpcFields());
		populateEntity(rpcEntity, rpcDefinition, rpcResultCopy);

		return rpcEntity;

	}

	@Override
	public void setConnection(RpcConnection rpcConnection) {

		super.setConnection(rpcConnection);
		preserveSnapshots((MockRpcConnection)getConnection());
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
