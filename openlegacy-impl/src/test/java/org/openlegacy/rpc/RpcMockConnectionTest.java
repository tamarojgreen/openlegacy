package org.openlegacy.rpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("/test-rpc-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcMockConnectionTest {

	@Inject
	RpcConnectionFactory rpcConnectionFactory;

	@Test
	public void testMockupConnection() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		Assert.assertNotNull(rpcConnection);
		RpcInvokeAction rpcAction = new SimpleRpcInvokeAction("test");
		RpcResult rpcResult = rpcConnection.invoke(rpcAction);

		Assert.assertNotNull(rpcResult);
		RpcField rpcField = rpcResult.getRpcFields().get(0);
		Assert.assertNotNull(rpcField);
		Assert.assertEquals("hello", rpcField.getValue());

		rpcField = rpcResult.getRpcFields().get(1);
		Assert.assertNotNull(rpcField);
		Assert.assertEquals(1234, rpcField.getValue());
	}
}