package org.openlegacy.rpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import javax.inject.Inject;

@ContextConfiguration("test-rpc-mock-connection-conntext.xml")
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
		rpcConnection.doAction(null);
		Assert.assertNotNull(rpcResult);
		RpcFlatField rpcField = (RpcFlatField)rpcResult.getRpcFields().get(0);
		Assert.assertNotNull(rpcField);
		Assert.assertEquals("hello", rpcField.getValue());

		rpcField = (RpcFlatField)rpcResult.getRpcFields().get(1);
		Assert.assertNotNull(rpcField);
		Assert.assertEquals(BigDecimal.valueOf(1234), rpcField.getValue());

		// second call

		rpcResult = rpcConnection.invoke(rpcAction);

		Assert.assertNotNull(rpcResult);
		rpcField = (RpcFlatField)rpcResult.getRpcFields().get(0);
		Assert.assertNotNull(rpcField);
		Assert.assertEquals("hello-new", rpcField.getValue());

		rpcField = (RpcFlatField)rpcResult.getRpcFields().get(1);
		Assert.assertNotNull(rpcField);
		Assert.assertEquals(BigDecimal.valueOf(3456), rpcField.getValue());
	}
}