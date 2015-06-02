package org.openlegacy.rpc.modules.trail;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcFields;
import org.openlegacy.rpc.persistance.RpcPersistedSnapshot;
import org.openlegacy.rpc.support.SimpleRpcFields;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcResult;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

public class RpcTrailSerializeTest {

	@Test
	public void testTrail() throws JAXBException, IOException {
		RpcPersistedTrail rpcTrail = new RpcPersistedTrail();
		RpcPersistedSnapshot rpcSnapshot = new RpcPersistedSnapshot();

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction("test");

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setLength(10);
		rpcField.setValue("hello");
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setLength(5);
		rpcField.setValue(1234);
		rpcField.setOrder(1);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);

		compareResult(rpcTrail, rpcSnapshot, rpcInvokeAction, "RpcTrail.expected");

	}

	@Test
	public void testStructTrail() throws JAXBException, IOException {
		RpcPersistedTrail rpcTrail = new RpcPersistedTrail();
		RpcPersistedSnapshot rpcSnapshot = new RpcPersistedSnapshot();

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction("test");

		SimpleRpcStructureField rpcStructFieldWithContainer = new SimpleRpcStructureField();
		rpcInvokeAction.getFields().add(rpcStructFieldWithContainer);

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setLength(10);
		rpcField.setValue("hello");
		rpcField.setOrder(0);
		rpcStructFieldWithContainer.getChildrens().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setLength(5);
		rpcField.setValue(1234);
		rpcField.setOrder(1);

		rpcField.setOrder(1);
		rpcStructFieldWithContainer.getChildrens().add(rpcField);
		rpcStructFieldWithContainer.setLegacyContainerName("containerTest");
		rpcStructFieldWithContainer.setOrder(0);

		SimpleRpcStructureField rpcVirtualStructField = new SimpleRpcStructureField();
		rpcVirtualStructField.setVirtual(true);
		rpcVirtualStructField.setOrder(1);
		rpcVirtualStructField.setName("virtualPart");
		rpcInvokeAction.getFields().add(rpcVirtualStructField);
		rpcField = new SimpleRpcFlatField();
		rpcField.setLength(5);
		rpcField.setValue(1234);
		rpcField.setOrder(0);
		rpcVirtualStructField.getChildrens().add(rpcField);

		compareResult(rpcTrail, rpcSnapshot, rpcInvokeAction, "RpcStructTrail.expected");
	}

	@Test
	public void testStructHirarcyTrail() throws JAXBException, IOException {
		RpcPersistedTrail rpcTrail = new RpcPersistedTrail();
		RpcPersistedSnapshot rpcSnapshot = new RpcPersistedSnapshot();

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction("test");

		SimpleRpcStructureField rpcStructField = new SimpleRpcStructureField();
		rpcStructField.setOrder(0);

		SimpleRpcStructureField rpcContainerStructField = new SimpleRpcStructureField();
		rpcContainerStructField.setOrder(0);
		rpcInvokeAction.getFields().add(rpcContainerStructField);
		rpcContainerStructField.getChildrens().add(rpcStructField);

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setLength(10);
		rpcField.setValue("hello");
		rpcField.setOrder(0);
		rpcStructField.getChildrens().add(rpcField);
		compareResult(rpcTrail, rpcSnapshot, rpcInvokeAction, "RpcStructHirarcyTrail.expected");
	}

	@Test
	public void testStructListTrail() throws JAXBException, IOException {
		RpcPersistedTrail rpcTrail = new RpcPersistedTrail();
		RpcPersistedSnapshot rpcSnapshot = new RpcPersistedSnapshot();

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction("test");

		SimpleRpcStructureListField rpcStructListField = new SimpleRpcStructureListField();
		rpcStructListField.setOrder(0);

		for (int i = 0; i < 3; i++) {

			RpcFields fields = new SimpleRpcFields();
			SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
			rpcField.setLength(10);
			rpcField.setValue("hello");
			rpcField.setOrder(0);
			fields.add(rpcField);

			rpcField = new SimpleRpcFlatField();
			rpcField.setLength(5);
			rpcField.setValue(i);
			rpcField.setOrder(1);
			fields.add(rpcField);
			rpcStructListField.getChildrens().add(fields);
		}
		rpcInvokeAction.getFields().add(rpcStructListField);
		compareResult(rpcTrail, rpcSnapshot, rpcInvokeAction, "RpcStructListTrail.expected");
	}

	private void compareResult(RpcPersistedTrail rpcTrail, RpcPersistedSnapshot rpcSnapshot,
			SimpleRpcInvokeAction rpcInvokeAction, String expectedFile) throws JAXBException, IOException {
		rpcInvokeAction.setRpcPath("rpctest");

		rpcSnapshot.setRpcInvokeAction(rpcInvokeAction);

		SimpleRpcResult result = new SimpleRpcResult();
		result.setRpcFields(rpcInvokeAction.getFields());

		rpcSnapshot.setRpcResult(result);
		rpcTrail.getSnapshots().add(rpcSnapshot);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmlSerializationUtil.serialize(RpcPersistedTrail.class, rpcTrail, baos);
		byte[] expected = IOUtils.toByteArray(getClass().getResource(expectedFile));
		AssertUtils.assertContent(expected, baos.toByteArray());
	}

	@Test
	public void testActionTrail() throws JAXBException, IOException {
		RpcPersistedTrail rpcTrail = new RpcPersistedTrail();
		RpcPersistedSnapshot rpcSnapshot = new RpcPersistedSnapshot();

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction("test");
		rpcInvokeAction.setAction("READ");
		Map<QName, String> properties = new HashMap<QName, String>();
		properties.put(new QName("p1"), "v1");
		properties.put(new QName("p2"), "v2");

		rpcInvokeAction.setProperties(properties);
		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setLength(10);
		rpcField.setValue("hello");
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setLength(5);
		rpcField.setValue(1234);
		rpcField.setOrder(1);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);

		compareResult(rpcTrail, rpcSnapshot, rpcInvokeAction, "RpcActionTrail.expected");

	}
}
