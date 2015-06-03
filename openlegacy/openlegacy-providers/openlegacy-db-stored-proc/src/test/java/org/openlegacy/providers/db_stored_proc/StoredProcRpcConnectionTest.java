package org.openlegacy.providers.db_stored_proc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.providers.db_stored_proc.procs.DoStuffWithTwoNumbersStoredProc;
import org.openlegacy.providers.db_stored_proc.procs.GetAllItemsStoredProc;
import org.openlegacy.providers.db_stored_proc.procs.GetItemDetailsStoredProc;
import org.openlegacy.providers.db_stored_proc.procs.SayHelloStoredProc;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.support.SimpleRpcFields;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("StoredProcRpcConnectionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StoredProcRpcConnectionTest {

	@Inject
	StoredProcRpcConnectionFactory factory;

	@Test
	public void intParamsTest() {
		RpcInvokeAction action = new SimpleRpcInvokeAction();

		List<RpcField> fields = action.getFields();

		fields.add(FieldsUtils.makeField("className", new String(
				DoStuffWithTwoNumbersStoredProc.class.getName())));
		fields.add(FieldsUtils.makeField("param1", new Integer(10)));
		fields.add(FieldsUtils.makeField("param2", new Integer(20)));

		RpcFlatField summResultField = FieldsUtils.makeField("sum", 0);
		RpcFlatField subResultField = FieldsUtils.makeField("sub", 0);
		RpcFlatField mulResultField = FieldsUtils.makeField("mul", 0);

		fields.add(summResultField);
		fields.add(subResultField);
		fields.add(mulResultField);

		RpcResult result = factory.getConnection().invoke(action);

		for (RpcField f : result.getRpcFields()) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;

				if (ff.getName().equals("sum")) {
					summResultField = ff;
				} else if (ff.getName().equals("sub")) {
					subResultField = ff;
				} else if (ff.getName().equals("mul")) {
					mulResultField = ff;
				}
			}
		}

		Assert.assertEquals(30,
				((BigDecimal) summResultField.getValue()).intValue());
		Assert.assertEquals(-10,
				((BigDecimal) subResultField.getValue()).intValue());
		Assert.assertEquals(200,
				((BigDecimal) mulResultField.getValue()).intValue());
	}

	@Test
	public void stringParamsTest() {
		RpcInvokeAction action = new SimpleRpcInvokeAction();

		List<RpcField> fields = action.getFields();

		fields.add(FieldsUtils.makeField("className", new String(
				SayHelloStoredProc.class.getName())));
		fields.add(FieldsUtils.makeField("param", new String("World")));

		RpcFlatField stringResultField = FieldsUtils.makeField("result", 0);

		fields.add(stringResultField);

		RpcResult result = factory.getConnection().invoke(action);

		for (RpcField f : result.getRpcFields()) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;

				if (ff.getName().equals("result")) {
					stringResultField = ff;
				}
			}
		}

		Assert.assertTrue("Hello, World".equals(stringResultField.getValue()));
	}

	@Test
	public void hierarchyDataTest() {

		RpcInvokeAction action = new SimpleRpcInvokeAction();

		List<RpcField> fields = action.getFields();

		fields.add(FieldsUtils.makeField("className", new String(
				GetItemDetailsStoredProc.class.getName())));

		fields.add(FieldsUtils.makeField("itemId", 1));

		SimpleRpcStructureField sf = new SimpleRpcStructureField();
		RpcField itemNameField = FieldsUtils.makeField("name", "");
		RpcField itemDescriptionField = FieldsUtils
				.makeField("description", "");
		RpcField itemWeightField = FieldsUtils.makeField("weight", 0);

		sf.setName("item");
		sf.getChildrens().add(itemNameField);
		sf.getChildrens().add(itemDescriptionField);
		sf.getChildrens().add(itemWeightField);

		fields.add(sf);

		sf = new SimpleRpcStructureField();
		RpcField shippingMethodField = FieldsUtils.makeField("method", "");
		RpcField shippingDaysField = FieldsUtils.makeField("days", 0);

		sf.setName("shipping");
		sf.getChildrens().add(shippingMethodField);
		sf.getChildrens().add(shippingDaysField);

		fields.add(sf);

		RpcResult result = factory.getConnection().invoke(action);

		GetItemDetailsStoredProc sp = new GetItemDetailsStoredProc();
		sp.fetchFields(result.getRpcFields());

		GetItemDetailsStoredProc.Results rr = (GetItemDetailsStoredProc.Results) sp.unrollResult();

		Assert.assertTrue(rr.item.name.equals("Kid Guitar"));
	}

	@Test
	public void arraysTest() {
		RpcInvokeAction action = new SimpleRpcInvokeAction();

		List<RpcField> fields = action.getFields();

		fields.add(FieldsUtils.makeField("className", new String(
				GetAllItemsStoredProc.class.getName())));

		SimpleRpcStructureListField lf = new SimpleRpcStructureListField();
		lf.setName("items");
		lf.getChildrens().add(new SimpleRpcFields());

		fields.add(lf);

		RpcResult result = factory.getConnection().invoke(action);

		GetAllItemsStoredProc sp = new GetAllItemsStoredProc();
		sp.fetchFields(result.getRpcFields());

		GetAllItemsStoredProc.Results rr = (GetAllItemsStoredProc.Results) sp
				.unrollResult();

		Assert.assertTrue(rr.items.size() == 5);

	}

}
