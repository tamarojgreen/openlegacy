package org.openlegacy.providers.jt400;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.providers.jt400.mockup.DummyFlatEntity;
import org.openlegacy.providers.jt400.mockup.DummyHierarchyStructEntity;
import org.openlegacy.providers.jt400.mockup.DummyHierarchyStructEntity.InnerRecord;
import org.openlegacy.providers.jt400.mockup.DummyHierarchyStructEntity.Top;
import org.openlegacy.providers.jt400.mockup.DummyStructEntity;
import org.openlegacy.providers.jt400.mockup.DummyStructEntity.Record;
import org.openlegacy.providers.jt400.mockup.ItemDetails;
import org.openlegacy.providers.jt400.mockup.MixedOrderEntity;
import org.openlegacy.providers.jt400.mockup.TreeArray;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("Jt400RpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Jt400RpcSessionTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testJT400FlatSession() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		DummyFlatEntity entity = new DummyFlatEntity();
		entity.setFirstName("Dganit");
		entity.setLastName("David");

		entity = rpcSession.doAction(RpcActions.READ(), entity);
		Assert.assertEquals("My name is Dganit David age", entity.getResultName());
	}

	@Test
	public void testJT400SimpleStructSession() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		DummyStructEntity entity = new DummyStructEntity();
		Record record = new Record();
		entity.setRecored(record);
		record.setField1(new Byte((byte)12));

		DummyStructEntity result = rpcSession.doAction(RpcActions.READ(), entity);
		record = result.getRecored();
		Assert.assertEquals(new Byte((byte)24), record.getField2());
	}

	@Test
	public void testJT400HierarchyStructSession() {

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		DummyHierarchyStructEntity entity = new DummyHierarchyStructEntity();

		Top top = new Top();
		entity.setTop(top);
		InnerRecord record = new InnerRecord();
		top.setRecord(record);
		for (int i = 10; i < 30; i += 5) {
			record.setField1(new Byte((byte)i));
			DummyHierarchyStructEntity result = rpcSession.doAction(RpcActions.READ(), entity);
			top = result.getTop();
			record = top.getRecord();
			Assert.assertEquals(new Byte((byte)(i * 2)), record.getField2());
		}

	}

	@Test
	public void testJt400TreeArray() {

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		TreeArray treeArray = new TreeArray();

		treeArray = rpcSession.doAction(RpcActions.READ(), treeArray);

		List<org.openlegacy.providers.jt400.mockup.TreeArray.ArArray.ArArrayRecords> records = treeArray.getArArray().getArArrayRecords();
		Assert.assertEquals(3, records.size());
		org.openlegacy.providers.jt400.mockup.TreeArray.ArArray.ArArrayRecords record = records.get(0);
		Assert.assertEquals("ARRAY ONE", record.getArText());
		Assert.assertEquals(new Integer(30), record.getArNum());
		record = records.get(1);
		Assert.assertEquals("ARRAY TOW", record.getArText());
		Assert.assertEquals(new Integer(40), record.getArNum());
		record = records.get(2);
		Assert.assertEquals("ARRAY THREE", record.getArText());
		Assert.assertEquals(new Integer(70), record.getArNum());
	}

	@Test
	public void testJt400Mixed() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		MixedOrderEntity mixedOrderEntity = new MixedOrderEntity();
		mixedOrderEntity = rpcSession.doAction(RpcActions.READ(), mixedOrderEntity);
		Assert.assertEquals("VAR1", mixedOrderEntity.getVar1());
		Assert.assertEquals("VAR2", mixedOrderEntity.getVar2());
		Assert.assertEquals("CHILD1", mixedOrderEntity.getStruct1().getChild1());
		Assert.assertEquals("CHILD2", mixedOrderEntity.getStruct1().getChild2());
		Assert.assertEquals("CHILD3", mixedOrderEntity.getStruct2().getChild3());
		Assert.assertEquals("CHILD4", mixedOrderEntity.getStruct2().getChild4());

	}

	@Test
	public void testSetNumber() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		ItemDetails itemDetails = new ItemDetails();
		itemDetails.setItemNumber(1000);
		itemDetails = rpcSession.doAction(RpcActions.READ(), itemDetails);
		Assert.assertEquals(new Integer(200), itemDetails.getItemWeight());

	}

}