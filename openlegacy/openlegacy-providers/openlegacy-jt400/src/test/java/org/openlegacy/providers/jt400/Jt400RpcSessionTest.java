package org.openlegacy.providers.jt400;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.providers.jt400.mockup.BooleanEntity;
import org.openlegacy.providers.jt400.mockup.BooleanPartEntity;
import org.openlegacy.providers.jt400.mockup.BooleanPartEntity.BooleanPart;
import org.openlegacy.providers.jt400.mockup.BooleanPartListEntity;
import org.openlegacy.providers.jt400.mockup.BooleanPartListEntity.BooleanPart2;
import org.openlegacy.providers.jt400.mockup.DateEntity;
import org.openlegacy.providers.jt400.mockup.DummyFlatEntity;
import org.openlegacy.providers.jt400.mockup.DummyHierarchyStructEntity;
import org.openlegacy.providers.jt400.mockup.DummyHierarchyStructEntity.InnerRecord;
import org.openlegacy.providers.jt400.mockup.DummyHierarchyStructEntity.Top;
import org.openlegacy.providers.jt400.mockup.DummyIntegersEntity;
import org.openlegacy.providers.jt400.mockup.DummyIntegersEntity.Param;
import org.openlegacy.providers.jt400.mockup.DummyStructEntity;
import org.openlegacy.providers.jt400.mockup.DummyStructEntity.Record;
import org.openlegacy.providers.jt400.mockup.EnumEntity;
import org.openlegacy.providers.jt400.mockup.EnumEntity.ColorGroup;
import org.openlegacy.providers.jt400.mockup.ItemDetails;
import org.openlegacy.providers.jt400.mockup.MixedOrderEntity;
import org.openlegacy.providers.jt400.mockup.TreeArray;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.GregorianCalendar;
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

	@Test
	public void testNumberDefaultValue() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		DummyIntegersEntity entity = new DummyIntegersEntity();

		Param p = new Param();

		p.setGroup1child1(15);
		p.setGroup1child2(25);
		p.setGroup2child1(30);
		entity.setParam(p);

		DummyIntegersEntity result = rpcSession.doAction(RpcActions.READ(), entity);
		Assert.assertEquals(new Integer(20), result.getParam().getChild2());
		Assert.assertEquals(new Integer(40), result.getParam().getGroup1child3());
		Assert.assertEquals(new Integer(30), result.getParam().getGroup2child2());

	}

	@Test
	public void testBoolean() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		BooleanEntity boolenEntity = new BooleanEntity();
		boolenEntity.setBool(true);
		boolenEntity = rpcSession.doAction(RpcActions.READ(), boolenEntity);
		Assert.assertFalse(boolenEntity.getBool());
		boolenEntity.setBool(false);
		boolenEntity = rpcSession.doAction(RpcActions.READ(), boolenEntity);
		Assert.assertTrue(boolenEntity.getBool());
	}

	@Test
	public void testBooleanPart() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		BooleanPartEntity boolenEntity = new BooleanPartEntity();
		BooleanPart booleanPart = new BooleanPart();
		booleanPart.setBool(true);
		boolenEntity.setBooleanPart(booleanPart);
		boolenEntity = rpcSession.doAction(RpcActions.READ(), boolenEntity);
		Assert.assertFalse(boolenEntity.getBooleanPart().getBool());
		boolenEntity = rpcSession.doAction(RpcActions.READ(), boolenEntity);
		Assert.assertTrue(boolenEntity.getBooleanPart().getBool());
	}

	@Test
	public void testBooleanPartList() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		BooleanPartListEntity boolenEntity = new BooleanPartListEntity();
		List<BooleanPart2> boolist = new ArrayList<BooleanPart2>();
		BooleanPart2 first = new BooleanPart2();
		first.setBool(true);
		boolist.add(first);
		boolist.add(new BooleanPart2());
		boolenEntity.setBooleanPart2(boolist);
		boolenEntity = rpcSession.doAction(RpcActions.READ(), boolenEntity);
		Assert.assertTrue(boolenEntity.getBooleanPart2().get(0).getBool());
		Assert.assertFalse(boolenEntity.getBooleanPart2().get(1).getBool());
		first.setBool(false);
		boolenEntity = rpcSession.doAction(RpcActions.READ(), boolenEntity);
		Assert.assertTrue(boolenEntity.getBooleanPart2().get(1).getBool());
	}

	@Test
	public void testDate() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		DateEntity dateEntity = new DateEntity();
		dateEntity.setDateIn(new GregorianCalendar(2014, 5 - 1, 18).getTime());
		dateEntity = rpcSession.doAction(RpcActions.READ(), dateEntity);
		Assert.assertEquals(new GregorianCalendar(2014, 5 - 1, 10).getTime(), dateEntity.getDateOut());
	}

	@Test
	public void testEnum() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		EnumEntity enumeEntity = new EnumEntity();
		enumeEntity.setFirstColor(ColorGroup.Yellow);
		enumeEntity = rpcSession.doAction(RpcActions.READ(), enumeEntity);
		Assert.assertEquals(ColorGroup.Yellow, enumeEntity.getFirstColor());
		Assert.assertEquals(ColorGroup.Red, enumeEntity.getSecondColor());
	}

}