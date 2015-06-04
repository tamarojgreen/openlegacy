package org.openlegacy.providers.db_stored_proc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.providers.db_stored_proc.entities.ItemDetailsEntity;
import org.openlegacy.providers.db_stored_proc.entities.ItemsEntity;
import org.openlegacy.providers.db_stored_proc.procs.GetAllItemsStoredProc;
import org.openlegacy.providers.db_stored_proc.procs.GetItemDetailsStoredProc;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
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
	StoredProcRpcConnection connection;

	@Test
	public void intParamsTest() {
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("doStuffWithTwoNumbers");

		List<RpcField> fields = action.getFields();
		fields.add(FieldsUtils.makeField("param1", new Integer(10)));
		fields.add(FieldsUtils.makeField("param2", new Integer(20)));

		RpcFlatField summResultField = FieldsUtils.makeField("sum", 0);
		RpcFlatField subResultField = FieldsUtils.makeField("sub", 0);
		RpcFlatField mulResultField = FieldsUtils.makeField("mul", 0);

		fields.add(summResultField);
		fields.add(subResultField);
		fields.add(mulResultField);

		RpcResult result = connection.invoke(action);

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
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("sayHello");

		List<RpcField> fields = action.getFields();
		fields.add(FieldsUtils.makeField("param", new String("World")));

		RpcFlatField stringResultField = FieldsUtils.makeField("result", 0);

		fields.add(stringResultField);

		RpcResult result = connection.invoke(action);

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
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("getItemDetails");

		List<RpcField> fields = action.getFields();
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

		RpcResult result = connection.invoke(action);

		GetItemDetailsStoredProc sp = new GetItemDetailsStoredProc();
		sp.fetchFields(result.getRpcFields());

		ItemDetailsEntity e = sp.getEntity();
		Assert.assertTrue(e.item.name.equals("Kid Guitar"));
	}

	@Test
	public void arraysTest() {
		SimpleRpcInvokeAction action = new SimpleRpcInvokeAction();
		action.setRpcPath("getAllItems");

		List<RpcField> fields = action.getFields();

		SimpleRpcStructureListField lf = new SimpleRpcStructureListField();
		lf.setName("items");
		lf.getChildrens().add(new SimpleRpcFields());

		fields.add(lf);

		RpcResult result = connection.invoke(action);

		GetAllItemsStoredProc sp = new GetAllItemsStoredProc();
		sp.fetchFields(result.getRpcFields());

		ItemsEntity e = sp.getEntity();
		Assert.assertTrue(e.items.size() == 5);

	}

}
