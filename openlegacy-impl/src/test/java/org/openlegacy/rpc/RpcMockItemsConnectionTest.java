package org.openlegacy.rpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("test-rpc-mock-items-conntext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcMockItemsConnectionTest {

	@Inject
	RpcConnectionFactory rpcConnectionFactory;

	@Test
	public void testMockupConnectionForItems() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		Assert.assertNotNull(rpcConnection);
		RpcInvokeAction rpcAction = new SimpleRpcInvokeAction("test");
		RpcResult rpcResult = rpcConnection.invoke(rpcAction);
		List<RpcField> result = rpcResult.getRpcFields();
		Assert.assertEquals(1, result.size());
		RpcStructureField legacyContainer = (RpcStructureField)result.get(0);
		RpcStructureListField records = (RpcStructureListField)legacyContainer.getChildrens().get(0);
		List<RpcField> theItem = records.getChildren(2);
		Assert.assertEquals(1002, ((RpcFlatField)theItem.get(0)).getValue());
		Assert.assertEquals("Water Ball", ((RpcFlatField)theItem.get(1)).getValue());
		Assert.assertEquals("Water Ball - Balls", ((RpcFlatField)theItem.get(2)).getValue());
	}
}
