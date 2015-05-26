package org.openlegacy.providers.wsrpc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.providers.wsrpc.WsRpcConnection.WsRpcConnectionRuntimeException;
import org.openlegacy.providers.wsrpc.utils.WsRpcActionUtil;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("WsRpcConnectionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class WsRpcConnectionTest {

	@Inject
	WsRpcConnectionFactory rpcConnectionFactory;

	@Test
	public void stringTest() {
		String callBackValue = "Vlad Drake";
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		rpcInvokeAction.setAction(WsRpcActionUtil.buildWsRpcAction("http://SimpleWebService/", "SimpleWebService",
				"callBackString"));

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackValue"); // param name
		rpcField.setValue("Vlad Drake"); // param value
		rpcField.setLength(callBackValue.length());
		rpcField.setDirection(Direction.INPUT); // Also you can use one parameter which has INPUT_OTPUT direction (if input /
												// outparam has same name)
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackResult"); // result param name
		rpcField.setLength(callBackValue.length());
		rpcField.setType(String.class); // result type
		rpcField.setDirection(Direction.OUTPUT);

		rpcInvokeAction.getFields().add(rpcField);
		rpcInvokeAction.setRpcPath("http://localhost:8181/SimpleWebService");

		RpcResult rpcResult = localInvoke(rpcConnection, rpcInvokeAction);

		Assert.assertEquals(callBackValue, ((RpcFlatField)rpcResult.getRpcFields().get(1)).getValue());
	}

	@Test
	public void intTest() {
		int callBackValue = 100500;
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		rpcInvokeAction.setAction(WsRpcActionUtil.buildWsRpcAction("http://SimpleWebService/", "SimpleWebService",
				"callBackInteger"));

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackValue");
		rpcField.setValue(callBackValue);
		rpcField.setLength(String.valueOf(callBackValue).length());
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackResult");
		// rpcField.setLength(); //I think it`s will unneed wit primitives
		rpcField.setType(int.class);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcInvokeAction.setRpcPath("http://localhost:8181/SimpleWebService");

		RpcResult rpcResult = localInvoke(rpcConnection, rpcInvokeAction);

		Assert.assertEquals(Integer.valueOf(callBackValue), ((RpcFlatField)rpcResult.getRpcFields().get(1)).getValue());
	}

	public RpcResult localInvoke(RpcConnection rpcConnection, RpcInvokeAction rpcInvokeAction) {
		try {
			return rpcConnection.invoke(rpcInvokeAction);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new WsRpcConnectionRuntimeException(e));
		}
	}
}
