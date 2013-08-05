package org.openlegacy.providers.jt400;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setValue("roi");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setValue("mor");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setLength(30);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/RPGROICH.PGM");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);

		Assert.assertEquals("My name is roi mor age", ((RpcFlatField)rpcResult.getRpcFields().get(2)).getValue());
	}

	@Test
	public void testJt400RpcConnectionWithNumber() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setValue("roi");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setValue("mor");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setValue(37);
		rpcField.setLength(3);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setLength(100);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/RPGROI.PGM");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);

		System.out.println(((RpcFlatField)rpcResult.getRpcFields().get(3)).getValue());
		Assert.assertEquals("My name is roi mor age 37 years !", ((RpcFlatField)rpcResult.getRpcFields().get(3)).getValue());
	}
}