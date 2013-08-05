package org.openlegacy.rpc.modules.trail;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openlegacy.rpc.persistance.RpcPersistedSnapshot;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcResult;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

public class RpcTrailSerializeTest {

	@Test
	public void testTrail() throws JAXBException, IOException {
		RpcPersistedTrail rpcTrail = new RpcPersistedTrail();
		RpcPersistedSnapshot rpcSnapshot = new RpcPersistedSnapshot();

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction("test");

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setLength(10);
		rpcField.setValue("hello");
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setLength(5);
		rpcField.setValue(1234);
		rpcInvokeAction.getFields().add(rpcField);

		rpcInvokeAction.setRpcPath("rpctest");

		rpcSnapshot.setRpcInvokeAction(rpcInvokeAction);

		SimpleRpcResult result = new SimpleRpcResult();
		result.setRpcFields(rpcInvokeAction.getFields());

		rpcSnapshot.setRpcResult(result);
		rpcTrail.getSnapshots().add(rpcSnapshot);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmlSerializationUtil.serialize(RpcPersistedTrail.class, rpcTrail, baos);
		byte[] expected = IOUtils.toByteArray(getClass().getResource("RpcTrail.expected"));
		AssertUtils.assertContent(expected, baos.toByteArray());
	}
}
