package org.openlegacy.providers.jt400;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.support.SimpleRpcField;
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

		SimpleRpcField rpcField = new SimpleRpcField();
		rpcField.setValue("roi");
		rpcField.setLength(20.0);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getRpcFields().add(rpcField);

		rpcField = new SimpleRpcField();
		rpcField.setValue("mor");
		rpcField.setLength(20.0);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getRpcFields().add(rpcField);

		rpcField = new SimpleRpcField();
		rpcField.setLength(30.0);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getRpcFields().add(rpcField);

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/RPGROICH.PGM");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);

		Assert.assertEquals("My name is roi mor age", rpcResult.getRpcFields().get(2).getValue());
	}

	@Test
	public void testJt400RpcConnectionWithNumber() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		SimpleRpcField rpcField = new SimpleRpcField();
		rpcField.setValue("roi");
		rpcField.setLength(20.0);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getRpcFields().add(rpcField);

		rpcField = new SimpleRpcField();
		rpcField.setValue("mor");
		rpcField.setLength(20.0);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getRpcFields().add(rpcField);

		rpcField = new SimpleRpcField();
		rpcField.setValue(37);
		rpcField.setLength(3.0);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getRpcFields().add(rpcField);

		rpcField = new SimpleRpcField();
		rpcField.setLength(100.0);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getRpcFields().add(rpcField);

		rpcInvokeAction.setRpcPath("/QSYS.LIB/RMR2L1.LIB/RPGROI.PGM");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);

		System.out.println(rpcResult.getRpcFields().get(3).getValue());
		Assert.assertEquals("My name is roi mor age 37 years !", rpcResult.getRpcFields().get(3).getValue());
	}
}