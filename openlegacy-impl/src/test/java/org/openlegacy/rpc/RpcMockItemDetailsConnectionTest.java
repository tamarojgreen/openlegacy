package org.openlegacy.rpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("test-rpc-mock-item-details-conntext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcMockItemDetailsConnectionTest {

	@Inject
	RpcConnectionFactory rpcConnectionFactory;

	@Test
	public void testMockupConnectionForItemDetails() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		Assert.assertNotNull(rpcConnection);
		RpcInvokeAction rpcAction = new SimpleRpcInvokeAction("test");
		RpcResult rpcResult = rpcConnection.invoke(rpcAction);
		List<RpcField> result = rpcResult.getRpcFields();
		Assert.assertEquals(3, result.size());
		RpcFlatField itemNum = (RpcFlatField)result.get(0);
		Assert.assertEquals(1000, itemNum.getValue());
		RpcStructureField details = (RpcStructureField)result.get(1);
		Assert.assertEquals("Kids Guitar", ((RpcFlatField)details.getChildrens().get(0)).getValue());
		Assert.assertEquals("Kids Guitar - Musical Toys", ((RpcFlatField)details.getChildrens().get(1)).getValue());
		Assert.assertEquals(200, ((RpcFlatField)details.getChildrens().get(2)).getValue());
		RpcStructureField shipping = (RpcStructureField)result.get(2);
		Assert.assertEquals("AIR", ((RpcFlatField)shipping.getChildrens().get(0)).getValue());
		Assert.assertEquals(2, ((RpcFlatField)shipping.getChildrens().get(1)).getValue());
	}

}
