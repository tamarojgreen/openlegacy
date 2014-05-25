package org.openlegacy.providers.jt400;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.providers.jt400.mockup.DummyGroupEntity;
import org.openlegacy.providers.jt400.mockup.DummyGroupEntity.Container;
import org.openlegacy.providers.jt400.mockup.DummyGroupEntity.Group1;
import org.openlegacy.providers.jt400.mockup.DummyGroupEntity.Group2;
import org.openlegacy.providers.jt400.mockup.DummyLegacyContainerEntity;
import org.openlegacy.providers.jt400.mockup.DummyLegacyContainerEntity.Record;
import org.openlegacy.providers.jt400.mockup.Items;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("Jt400RpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Jt400RpcManipulateSessionTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testSimpleLegacyContainer() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		DummyLegacyContainerEntity entity = new DummyLegacyContainerEntity();
		Record record = new Record();
		entity.setRecord(record);
		record.setField1(new Byte((byte)10));
		DummyLegacyContainerEntity result = rpcSession.doAction(RpcActions.READ(), entity);
		Assert.assertEquals(new Byte((byte)20), result.getRecord().getField2());

	}

	@Test
	public void testListLegacyContainer() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		Items result = rpcSession.doAction(RpcActions.READ(), new Items());

		Assert.assertEquals(new Integer(1000), result.getInnerRecord().get(0).getItemNumber());
		Assert.assertEquals("Kid Guitar", result.getInnerRecord().get(0).getItemName());
		Assert.assertEquals(new Integer(1001), result.getInnerRecord().get(1).getItemNumber());

	}

	@Test
	public void testFilterEmpty() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		Items result = rpcSession.doAction(RpcActions.READ(), new Items());

		Assert.assertEquals(5, result.getInnerRecord().size());

	}

	@Test
	public void testManipulateGroup() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		DummyGroupEntity entity = new DummyGroupEntity();

		Container fieldContainer = new Container();

		Group1 g1 = new Group1();
		g1.setGroup1child1(15);
		g1.setGroup1child2(25);
		fieldContainer.setGroup1(g1);

		Group2 g2 = new Group2();
		g2.setGroup2child1(30);
		fieldContainer.setGroup2(g2);

		entity.setContainer(fieldContainer);

		DummyGroupEntity result = rpcSession.doAction(RpcActions.READ(), entity);
		Assert.assertEquals(new Integer(20), result.getContainer().getChild2());
		Assert.assertEquals(new Integer(40), result.getContainer().getGroup1().getGroup1child3());
		Assert.assertEquals(new Integer(30), result.getContainer().getGroup2().getGroup2child2());
	}
}