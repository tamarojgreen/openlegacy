package org.openlegacy.rpc.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.support.mock.RpcDateEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

@ContextConfiguration("DateFieldsBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcDateFieldsBinderTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testFillBooleanField() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		RpcDateEntity dateEntity = rpcSession.getEntity(RpcDateEntity.class);
		Calendar calendar = new GregorianCalendar(2014, 5 - 1, 18); // 18/May/2014
		Assert.assertEquals(calendar.getTime(), dateEntity.getDateField());
	}

}
