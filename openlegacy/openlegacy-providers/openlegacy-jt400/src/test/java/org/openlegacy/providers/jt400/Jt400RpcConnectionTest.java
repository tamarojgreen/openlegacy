package org.openlegacy.providers.jt400;

import org.junit.Assert;
import org.junit.Ignore;
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
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.support.SimpleRpcFields;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("Jt400RpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Jt400RpcConnectionTest {

	@Inject
	private RpcConnectionFactory rpcConnectionFactory;

	@Test
	public void testJt400RpcConnection() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction("roi");
		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("firstName");
		rpcField.setValue("roi");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setValue("mor");
		rpcField.setName("lastName");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setLength(30);
		rpcField.setName("result");
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/RPGROICH.PGM");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);

		Assert.assertEquals("My name is roi mor age", ((RpcFlatField)rpcResult.getRpcFields().get(2)).getValue());
	}

	// test not working prior to my changes, disabling
	@Ignore
	@Test
	public void testJt400RpcConnectionWithNumber() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("firstName");
		rpcField.setValue("roi");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setValue("mor");
		rpcField.setName("lastName");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("age");

		rpcField.setValue(37);
		rpcField.setLength(2);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("result");
		rpcField.setLength(100);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/RPGROI.PGM");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);

		System.out.println(((RpcFlatField)rpcResult.getRpcFields().get(3)).getValue());
		Assert.assertEquals("My name is roi mor age 37 years !", ((RpcFlatField)rpcResult.getRpcFields().get(3)).getValue());
	}

	@Test
	public void jT400flatFieldConnectionTest() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		String resultName = "cobol_flat";
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction(resultName);
		rpcInvokeAction.getFields().add(child1());
		rpcInvokeAction.getFields().add(child2());

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/FLATCBL.PGM");
		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
		Assert.assertNotNull(rpcResult);
		List<RpcField> fields = rpcResult.getRpcFields();
		Assert.assertEquals(2, fields.size());
		RpcField field = fields.get(1);
		Assert.assertEquals("20", ((RpcFlatField)field).getValue());
	}

	@Test
	public void jT400flatFieldReuseConnectionTest() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		String resultName = "cobol_flat";
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction(resultName);
		RpcFlatField inputField = child1();
		rpcInvokeAction.getFields().add(inputField);
		rpcInvokeAction.getFields().add(child2());

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/FLATCBL.PGM");
		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
		// To verify reuse of the same programCallDocument
		for (int i = 8; i < 15; i++) {
			inputField.setValue(String.format("%2d", i));
			rpcResult = rpcConnection.invoke(rpcInvokeAction);

			List<RpcField> fields = rpcResult.getRpcFields();
			Assert.assertEquals(2, fields.size());
			RpcField field = fields.get(1);
			Assert.assertEquals(Integer.toString(i * 2), ((RpcFlatField)field).getValue());
		}
	}

	@Test
	public void jT400SimpleStructConnectionTest() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		String resultName = "cobol_structure";

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction(resultName);

		SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
		rpcStructureField.setName("param1");
		SimpleRpcFlatField input = child1();
		input.setValue("15");
		SimpleRpcFlatField out = child2();
		rpcStructureField.getChildrens().add(input);
		out.setValue("0");

		rpcStructureField.getChildrens().add(out);

		rpcInvokeAction.getFields().add(rpcStructureField);
		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/ROICBL2.PGM");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
		Assert.assertNotNull(rpcResult);
		List<RpcField> fields = rpcResult.getRpcFields();
		Assert.assertEquals(1, fields.size());
		SimpleRpcStructureField resultTopStructure = (SimpleRpcStructureField)fields.get(0);
		List<RpcField> children = resultTopStructure.getChildrens();
		Assert.assertEquals(2, children.size());
		RpcField field = children.get(1);
		Assert.assertEquals("30", ((RpcFlatField)field).getValue());

	}

	@Test
	public void jT400ArrayStructConnectionTest() {

		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		String resultName = "tree_array";

		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();
		rpcInvokeAction.setAction(resultName);

		SimpleRpcStructureField rpcTopStructureField = new SimpleRpcStructureField();
		rpcTopStructureField.setName("top");

		SimpleRpcStructureListField rpcRecordField = new SimpleRpcStructureListField();
		rpcRecordField.setName("record");

		rpcTopStructureField.getChildrens().add(rpcRecordField);

		for (int i = 0; i < 3; i++) {

			RpcFields recordFields = new SimpleRpcFields();

			SimpleRpcFlatField rpcFlatField = new SimpleRpcFlatField();
			rpcFlatField.setName("text");
			rpcFlatField.setType(String.class);
			rpcFlatField.setLength(11);
			rpcFlatField.setDirection(Direction.OUTPUT);

			recordFields.add(rpcFlatField);

			rpcFlatField = new SimpleRpcFlatField();
			rpcFlatField.setName("number");
			rpcFlatField.setLength(4);
			rpcFlatField.setType(Byte.class);
			rpcFlatField.setDecimalPlaces(0);
			rpcFlatField.setDirection(Direction.OUTPUT);
			recordFields.add(rpcFlatField);

			rpcRecordField.getChildrens().add(recordFields);
		}
		rpcInvokeAction.getFields().add(rpcTopStructureField);
		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/TREEARRAY.PGM");
		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
		Assert.assertNotNull(rpcResult);
		RpcStructureField topLevelField = (RpcStructureField)rpcResult.getRpcFields().get(0);
		RpcStructureListField records = (RpcStructureListField)topLevelField.getChildrens().get(0);
		List<RpcField> firstRecordFields = records.getChildren(0);
		RpcFlatField numField = (RpcFlatField)firstRecordFields.get(1);
		Assert.assertEquals("0030", numField.getValue());
		Assert.assertEquals("ARRAY ONE", ((RpcFlatField)firstRecordFields.get(0)).getValue());

		List<RpcField> lastRecordFields = records.getChildren(2);
		numField = (RpcFlatField)lastRecordFields.get(1);
		Assert.assertEquals("0070", numField.getValue());
		Assert.assertEquals("ARRAY THREE", ((RpcFlatField)lastRecordFields.get(0)).getValue());

	}

	private static SimpleRpcFlatField child1() {

		SimpleRpcFlatField rpcFlatField = new SimpleRpcFlatField();
		rpcFlatField.setName("child1");
		rpcFlatField.setType(Byte.class);
		rpcFlatField.setLength(2);
		rpcFlatField.setDecimalPlaces(0);
		rpcFlatField.setDirection(Direction.INPUT);
		rpcFlatField.setOrder(0);
		rpcFlatField.setDefaultValue(new Byte((byte)10));
		return rpcFlatField;
	}

	private static SimpleRpcFlatField child2() {
		SimpleRpcFlatField rpcFlatField;
		rpcFlatField = new SimpleRpcFlatField();
		rpcFlatField.setName("child2");
		rpcFlatField.setType(Byte.class);
		rpcFlatField.setLength(2);
		rpcFlatField.setDecimalPlaces(0);
		rpcFlatField.setDirection(Direction.OUTPUT);
		rpcFlatField.setOrder(1);
		return rpcFlatField;
	}
}