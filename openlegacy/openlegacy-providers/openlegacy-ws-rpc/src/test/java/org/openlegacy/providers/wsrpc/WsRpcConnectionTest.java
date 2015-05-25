package org.openlegacy.providers.wsrpc;

import javax.inject.Inject;

import opr.openlegacy.rpc.wsrpc.WsRpcInvokeAction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("WsRpcConnectionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class WsRpcConnectionTest {

	@Inject
	WsRpcConnectionFactory rpcConnectionFactory;
	
	@Test
	public void stringTest(){
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		WsRpcInvokeAction rpcInvokeAction = new WsRpcInvokeAction();
		
		rpcInvokeAction.setAction("callBackString");
		rpcInvokeAction.setNamespace("http://SimpleWebService/");
		rpcInvokeAction.setServiceName("SimpleWebService");
		
		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackValue");
		rpcField.setValue("Vlad Drake");
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();		
		rpcField.setName("callBackResult");
		rpcField.setLength(20);
		rpcField.setType(String.class);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);
		
		rpcInvokeAction.setRpcPath("http://localhost:8181/SimpleWebService");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
	}
	
	@Test
	public void intTest(){
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		WsRpcInvokeAction rpcInvokeAction = new WsRpcInvokeAction();
		
		rpcInvokeAction.setAction("callBackString");
		rpcInvokeAction.setNamespace("http://SimpleWebService/");
		rpcInvokeAction.setServiceName("SimpleWebService");
		
		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackValue");
		rpcField.setValue(100500);
		rpcField.setLength(20);
		rpcField.setDirection(Direction.INPUT);
		rpcInvokeAction.getFields().add(rpcField);

		rpcField = new SimpleRpcFlatField();		
		rpcField.setName("callBackResult");
		rpcField.setLength(20);
		rpcField.setType(int.class);
		rpcField.setDirection(Direction.OUTPUT);
		rpcInvokeAction.getFields().add(rpcField);
		
		rpcInvokeAction.setRpcPath("http://localhost:8181/SimpleWebService");

		RpcResult rpcResult = rpcConnection.invoke(rpcInvokeAction);
	}
	
	
}
