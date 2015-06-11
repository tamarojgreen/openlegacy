package org.openlegacy.providers.db_stored_proc;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("StoredProcRpcConnectionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StoredProcRpcConnectionITCase {

	@Inject
	StoredProcRpcConnectionFactory factory;

	RpcConnection connection;

	@BeforeClass
	public static void beforeClass() {
		if (System.getProperty("mysql.port") == null) {
			System.setProperty("mysql.port", "3306");
		}

		System.out.println(System.getProperty("mysql.port"));
	}

	@Before
	public void before() {
		connection = factory.getConnection();

		connection.login("rpc_test", "password");
	}

	@After
	public void after() {
		connection.disconnect();
	}

	@Test
	public void intParamsTest() {
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("doStuffWithTwoNumbers");

		List<RpcField> fields = action.getFields();
		fields.add(FieldsUtils.makeField("param1", new Integer(10), Direction.INPUT, 1));
		fields.add(FieldsUtils.makeField("param2", new Integer(20), Direction.INPUT, 2));

		SimpleRpcStructureListField resultsField = new SimpleRpcStructureListField();
		resultsField.setName("results");
		fields.add(resultsField);

		RpcFlatField sumResultField = FieldsUtils.makeField("sum", 0);
		RpcFlatField subResultField = FieldsUtils.makeField("sub", 0);
		RpcFlatField mulResultField = FieldsUtils.makeField("mul", 0);

		RpcResult rpcResult = connection.invoke(action);

		// parse result

		for (RpcField f : rpcResult.getRpcFields()) {
			if (f instanceof RpcStructureListField) {
				if (f.getName().equals("results")) {
					SimpleRpcStructureListField lf = (SimpleRpcStructureListField)f;

					for (RpcField field : lf.getChildren(0)) {
						if (field instanceof RpcStructureField /* && field.getName().equals("item") */) {
							RpcStructureField sf = (RpcStructureField)field;

							for (RpcField itemField : sf.getChildrens()) {
								if (itemField instanceof RpcFlatField) {
									RpcFlatField ff = (RpcFlatField)itemField;

									if (ff.getName().equals("sum")) {
										sumResultField = ff;
									} else if (ff.getName().equals("sub")) {
										subResultField = ff;
									} else if (ff.getName().equals("mul")) {
										mulResultField = ff;
									}
								}
							}
						}
					}
				}
			}
		}

		Assert.assertEquals(30, ((BigDecimal)sumResultField.getValue()).intValue());
		Assert.assertEquals(-10, ((BigDecimal)subResultField.getValue()).intValue());
		Assert.assertEquals(200, ((BigDecimal)mulResultField.getValue()).intValue());
	}

	@Test
	public void intParamsWithOutParamsTest() {
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("doStuffWithTwoNumbersIntoParams");

		List<RpcField> fields = action.getFields();
		fields.add(FieldsUtils.makeField("param1", new Integer(10), Direction.INPUT, 1));
		fields.add(FieldsUtils.makeField("param2", new Integer(20), Direction.INPUT, 2));

		RpcFlatField sumResultField = FieldsUtils.makeField("sum", new Integer(0), Direction.OUTPUT, 3);
		RpcFlatField subResultField = FieldsUtils.makeField("sub", new Integer(0), Direction.OUTPUT, 4);
		RpcFlatField mulResultField = FieldsUtils.makeField("mul", new Integer(0), Direction.OUTPUT, 5);

		fields.add(sumResultField);
		fields.add(subResultField);
		fields.add(mulResultField);

		RpcResult rpcResult = connection.invoke(action);

		// parse result

		Assert.assertEquals(30, ((BigDecimal)sumResultField.getValue()).intValue());
		Assert.assertEquals(-10, ((BigDecimal)subResultField.getValue()).intValue());
		Assert.assertEquals(200, ((BigDecimal)mulResultField.getValue()).intValue());
	}

	@Test
	public void stringParamsTest() {
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("sayHello");

		List<RpcField> fields = action.getFields();
		fields.add(FieldsUtils.makeField("param", new String("World"), Direction.INPUT, 1));

		SimpleRpcStructureListField resultsField = new SimpleRpcStructureListField();
		resultsField.setName("results");
		fields.add(resultsField);

		RpcFlatField stringResultField = FieldsUtils.makeField("result", 0);

		RpcResult rpcResult = connection.invoke(action);

		// parse result

		for (RpcField f : rpcResult.getRpcFields()) {
			if (f instanceof RpcStructureListField) {
				if (f.getName().equals("results")) {
					SimpleRpcStructureListField lf = (SimpleRpcStructureListField)f;

					for (RpcField field : lf.getChildren(0)) {
						if (field instanceof RpcStructureField /* && field.getName().equals("item") */) {
							RpcStructureField sf = (RpcStructureField)field;

							for (RpcField itemField : sf.getChildrens()) {
								if (itemField instanceof RpcFlatField) {
									RpcFlatField ff = (RpcFlatField)itemField;

									// if (ff.getName().equals("result")) {
									stringResultField = ff;
									// }
								}
							}
						}
					}
				}
			}
		}

		Assert.assertTrue("Hello, World".equals(stringResultField.getValue()));
	}

	@Test
	public void stringOutputParamTest() {
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("sayHelloIntoParam");

		List<RpcField> fields = action.getFields();
		fields.add(FieldsUtils.makeField("param", new String("World"), Direction.INPUT, 1));

		RpcFlatField stringResultField = FieldsUtils.makeField("result", new String(""), Direction.OUTPUT, 2);
		fields.add(stringResultField);

		RpcResult rpcResult = connection.invoke(action);

		Assert.assertEquals("Hello, World", stringResultField.getValue());
	}

	@Test
	public void hierarchyDataTest() {
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("getItemDetails");

		List<RpcField> fields = action.getFields();
		fields.add(FieldsUtils.makeField("itemId", 1, Direction.INPUT, 1));

		SimpleRpcStructureListField resultsField = new SimpleRpcStructureListField();
		resultsField.setName("results");
		fields.add(resultsField);

		RpcResult rpcResult = connection.invoke(action);

		RpcFlatField itemNameField = null;

		for (RpcField f : rpcResult.getRpcFields()) {
			if (f instanceof RpcStructureListField) {
				if (f.getName().equals("results")) {
					SimpleRpcStructureListField lf = (SimpleRpcStructureListField)f;

					for (RpcField field : lf.getChildren(0)) {
						if (field instanceof RpcStructureField /* && field.getName().equals("item") */) {
							RpcStructureField sf = (RpcStructureField)field;
							for (RpcField itemField : sf.getChildrens()) {
								if (itemField instanceof RpcFlatField) {
									RpcFlatField ff = (RpcFlatField)itemField;

									if (ff.getName().equals("name")) {
										itemNameField = ff;
									}
								}
							}
						}
					}
				}
			}
		}

		Assert.assertTrue(itemNameField.getValue().equals("Kid Guitar"));
	}

	@Test
	public void arraysTest() {
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("getAllItems");

		List<RpcField> fields = action.getFields();

		SimpleRpcStructureListField resultsField = new SimpleRpcStructureListField();
		resultsField.setName("results");
		fields.add(resultsField);

		RpcResult rpcResult = connection.invoke(action);

		// parse result

		int resultsCount = 0;

		for (RpcField f : rpcResult.getRpcFields()) {
			if (f instanceof RpcStructureListField) {
				if (f.getName().equals("results")) {
					SimpleRpcStructureListField lf = (SimpleRpcStructureListField)f;

					for (RpcField field : lf.getChildren(0)) {
						if (field instanceof RpcStructureField /* && field.getName().equals("item") */) {
							resultsCount++;
						}
					}
				}
			}
		}

		Assert.assertEquals(resultsCount, 5);
	}

}
