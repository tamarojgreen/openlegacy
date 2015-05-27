package org.openlegacy.providers.db_stored_proc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.providers.db_stored_proc.procs.DoStuffWithTwoNumbersStoredProc;
import org.openlegacy.providers.db_stored_proc.procs.SayHelloStoredProc;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("StoredProcRpcConnectionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StoredProcRpcConnectionTest {

	@Inject
	StoredProcRpcConnectionFactory factory;

	SimpleRpcFlatField makeField(String name, Object value) {
		return makeField(name, value, value.getClass());
	}

	SimpleRpcFlatField makeField(String name, Object value, Class<?> type) {
		SimpleRpcFlatField f = new SimpleRpcFlatField();
		f.setName(name);
		f.setValue(value);
		f.setType(type);
		f.setLength(4);
		f.setDirection(Direction.INPUT);

		return f;
	}

	@Test
	public void intParamsTest() {
		RpcInvokeAction action = new SimpleRpcInvokeAction();

		List<RpcField> fields = action.getFields();

		fields.add(makeField("className", new String(
				DoStuffWithTwoNumbersStoredProc.class.getName())));
		fields.add(makeField("param1", new Integer(10)));
		fields.add(makeField("param2", new Integer(20)));

		RpcFlatField summResultField = makeField("sum", 0);
		RpcFlatField subResultField = makeField("sub", 0);
		RpcFlatField mulResultField = makeField("mul", 0);

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

		fields.add(makeField("className",
				new String(SayHelloStoredProc.class.getName())));
		fields.add(makeField("param", new String("World")));

		RpcFlatField stringResultField = makeField("result", 0);

		fields.add(stringResultField);

		RpcResult result = factory.getConnection().invoke(action);

		for (RpcField f : result.getRpcFields()) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;

				if (ff.getName().equals("stringResult")) {
					stringResultField = ff;
				}
			}
		}

		Assert.assertTrue("Hello, World".equals(stringResultField.getValue()));
	}

}
