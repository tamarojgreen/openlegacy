package org.openlegacy.providers.mfrpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFields;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcFields;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("mfRpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MfRpcConnectionTest {

	@Inject
	private RpcConnectionFactory rpcConnectionFactory;

	@Test
	public void testCICSRpcConnectionFlat() throws RemoteException {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction("RPC");

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("caName");
		rpcField.setValue("Roi");
		rpcField.setLength(8);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Char");
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caLast");
		rpcField.setValue("Mor");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Char");
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caAge");
		rpcField.setValue(-1);
		rpcField.setLength(2);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Binary Integer");
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caSalary");
		rpcField.setValue(1000000);
		rpcField.setLength(4);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);
		rpcField.setLegacyType("Binary Integer");

		rpcInvokeAction.setRpcPath("RPC");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
		Assert.assertNotNull(rpcResult);
		List<RpcField> fields = rpcResult.getRpcFields();
		RpcFlatField field = (RpcFlatField)fields.get(0);
		Assert.assertEquals("JANE", field.getValue());
		field = (RpcFlatField)fields.get(1);
		Assert.assertEquals("DOE", field.getValue());
		field = (RpcFlatField)fields.get(2);
		Assert.assertEquals(new BigDecimal(32), field.getValue());
		field = (RpcFlatField)fields.get(3);
		Assert.assertEquals(new BigDecimal(64000), field.getValue());
	}

	@Test
	public void testCICSRpcConnection() throws RemoteException {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction("RPC");

		SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
		rpcStructureField.setName("dfhcommarea");
		rpcInvokeAction.getFields().add(rpcStructureField);

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("caName");
		rpcField.setValue("Roi");
		rpcField.setLength(8);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Char");
		rpcField.setOrder(0);
		rpcStructureField.getChildrens().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caLast");
		rpcField.setValue("Mor");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Char");
		rpcField.setOrder(1);
		rpcStructureField.getChildrens().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caAge");
		rpcField.setValue(1);
		rpcField.setLength(2);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Binary Integer");
		rpcField.setOrder(2);
		rpcStructureField.getChildrens().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("caSalary");
		rpcField.setValue(1000000);
		rpcField.setLength(4);
		rpcField.setDirection(Direction.INPUT_OUTPUT);
		rpcField.setLegacyType("Binary Integer");
		rpcField.setOrder(3);
		rpcStructureField.getChildrens().add(rpcField);

		rpcInvokeAction.setRpcPath("RPC");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
		Assert.assertNotNull(rpcResult);
		List<RpcField> fields = rpcResult.getRpcFields();
		Assert.assertEquals(1, fields.size());

		RpcStructureField topField = (RpcStructureField)fields.get(0);
		List<RpcField> inFields = topField.getChildrens();

		RpcFlatField inField = (RpcFlatField)inFields.get(0);
		Assert.assertEquals("JANE", inField.getValue());
		inField = (RpcFlatField)inFields.get(1);
		Assert.assertEquals("DOE", inField.getValue());
		inField = (RpcFlatField)inFields.get(2);
		Assert.assertEquals(new BigDecimal(32), inField.getValue());
		inField = (RpcFlatField)inFields.get(3);
	}

	@Test
	public void testOccursCICSRpcConnection() throws RemoteException {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction("RPC");
		rpcInvokeAction.setRpcPath("RPC4");

		SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
		rpcStructureField.setName("dfhcommarea");
		rpcInvokeAction.getFields().add(rpcStructureField);

		SimpleRpcStructureListField caOutList = new SimpleRpcStructureListField();
		rpcStructureField.getChildrens().add(caOutList);
		caOutList.setName("caOutList");
		caOutList.setOrder(0);

		for (int i = 0; i < 2; i++) {

			RpcFields recordFields = new SimpleRpcFields();

			SimpleRpcFlatField rpcFlatField = new SimpleRpcFlatField();
			rpcFlatField.setName("caFirst");
			rpcFlatField.setValue("Roi");
			rpcFlatField.setLegacyType("Char");
			rpcFlatField.setLength(4);
			rpcFlatField.setOrder(0);
			rpcFlatField.setDirection(Direction.INPUT_OUTPUT);

			recordFields.add(rpcFlatField);

			rpcFlatField = new SimpleRpcFlatField();
			rpcFlatField.setName("caSecond");
			rpcFlatField.setValue(i + 20);
			rpcFlatField.setLength(4);
			rpcFlatField.setLegacyType("Binary Integer");
			rpcFlatField.setDecimalPlaces(0);
			rpcFlatField.setDirection(Direction.INPUT_OUTPUT);
			recordFields.add(rpcFlatField);

			SimpleRpcStructureListField inner = new SimpleRpcStructureListField();
			inner.setName("caIn");
			inner.setOrder(3);
			recordFields.add(inner);

			for (int j = 0; j < 3; j++) {
				RpcFields inRecordFields = new SimpleRpcFields();

				rpcFlatField = new SimpleRpcFlatField();
				rpcFlatField.setName("caInFirst");
				rpcFlatField.setValue("a");
				rpcFlatField.setLegacyType("Char");
				rpcFlatField.setLength(2);
				rpcFlatField.setOrder(0);
				rpcFlatField.setDirection(Direction.INPUT_OUTPUT);
				inRecordFields.add(rpcFlatField);
				inner.getChildrens().add(inRecordFields);
			}

			caOutList.getChildrens().add(recordFields);
		}

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
		SimpleRpcStructureField dfh = (SimpleRpcStructureField)rpcResult.getRpcFields().get(0);
		SimpleRpcStructureListField outList = (SimpleRpcStructureListField)dfh.getChildrens().get(0);

		SimpleRpcFlatField first = (SimpleRpcFlatField)outList.getChildren(0).get(1);
		Assert.assertEquals(new BigDecimal("21"), first.getValue());
		first = (SimpleRpcFlatField)outList.getChildren(1).get(1);
		Assert.assertEquals(new BigDecimal("22"), first.getValue());
	}

}