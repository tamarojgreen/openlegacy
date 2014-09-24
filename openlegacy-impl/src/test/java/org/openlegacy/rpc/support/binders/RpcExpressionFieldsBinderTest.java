package org.openlegacy.rpc.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.support.mock.ExpressionRpcEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("ExpressionFieldsBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcExpressionFieldsBinderTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testFillExpressionField() {
		final RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		final ExpressionRpcEntity expressionRpc = rpcSession.getEntity(ExpressionRpcEntity.class);
		Assert.assertTrue(expressionRpc.getBooleanTrue());
		Assert.assertEquals("Here it is", expressionRpc.getExpression1());
		Assert.assertEquals(new Integer(123), expressionRpc.getIntExpression());
		Assert.assertEquals(new Integer(445), expressionRpc.getIntExpression2());
		Assert.assertEquals(new Integer(778), expressionRpc.getIntExpression3());
		Assert.assertEquals(new Integer(568), expressionRpc.getIntExpression1and2());
		Assert.assertEquals((String)null, expressionRpc.getStringExpressionNull());
		Assert.assertFalse(expressionRpc.getPartBoolean().getBooleanInPart());

	}
}
