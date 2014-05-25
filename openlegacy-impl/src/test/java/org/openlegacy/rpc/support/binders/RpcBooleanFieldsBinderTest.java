package org.openlegacy.rpc.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.support.mock.BooleanRpcEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("BooleanFieldsBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcBooleanFieldsBinderTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testFillBooleanField() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		BooleanRpcEntity booleanRpc = rpcSession.getEntity(BooleanRpcEntity.class);
		Assert.assertTrue(booleanRpc.getBooleanTrue());
		Assert.assertFalse(booleanRpc.getBooleanFalse());
		Assert.assertNull(booleanRpc.getBooleanEmpty());
		Assert.assertFalse(booleanRpc.getPartBoolean().getBooleanInPart());

	}
}
