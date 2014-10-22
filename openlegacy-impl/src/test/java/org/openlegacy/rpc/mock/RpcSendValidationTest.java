package org.openlegacy.rpc.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcActions;
import org.openlegacy.rpc.mock.mock.FieldsEntity;
import org.openlegacy.rpc.mock.mock.FieldsEntity.InnerPart;
import org.openlegacy.rpc.mock.mock.FieldsEntity.ListPart;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("rpc-send-validation-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcSendValidationTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testCompare() {

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		FieldsEntity fieldsEntity = getFieldEntity();

		rpcSession.doAction(RpcActions.READ(), fieldsEntity);
	}

	@Test
	public void testFailedText() {
		boolean gotExeption = false;

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		FieldsEntity fieldsEntity = getFieldEntity();
		fieldsEntity.setItemName("stam");
		try {
			rpcSession.doAction(RpcActions.READ(), fieldsEntity);
		} catch (Exception e) {
			gotExeption = true;
		}

		Assert.assertEquals(true, gotExeption);
	}

	@Test
	public void testFailedNumber() {
		boolean gotExeption = false;

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		FieldsEntity fieldsEntity = getFieldEntity();
		fieldsEntity.setItemNumber(1);
		try {
			rpcSession.doAction(RpcActions.READ(), fieldsEntity);
		} catch (Exception e) {
			gotExeption = true;
		}

		Assert.assertEquals(true, gotExeption);
	}

	@Test
	public void testFailedBoolean() {
		boolean gotExeption = false;

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		FieldsEntity fieldsEntity = getFieldEntity();
		fieldsEntity.setBool(false);
		try {
			rpcSession.doAction(RpcActions.READ(), fieldsEntity);
		} catch (Exception e) {
			gotExeption = true;
		}

		Assert.assertEquals(true, gotExeption);
	}

	@Test
	public void testFaileDate() {
		boolean gotExeption = false;

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		FieldsEntity fieldsEntity = getFieldEntity();
		Calendar calendar = new GregorianCalendar(2013, 5 - 1, 18);
		fieldsEntity.setDateField(calendar.getTime());

		try {
			rpcSession.doAction(RpcActions.READ(), fieldsEntity);
		} catch (Exception e) {
			gotExeption = true;
		}

		Assert.assertEquals(true, gotExeption);

	}

	@Test
	public void testFailedStruct() {

		boolean gotExeption = false;

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		FieldsEntity fieldsEntity = getFieldEntity();
		InnerPart innerPart = new InnerPart();
		innerPart.setTextField("stam");
		fieldsEntity.setInnerPart(innerPart);
		try {
			rpcSession.doAction(RpcActions.READ(), fieldsEntity);
		} catch (Exception e) {
			gotExeption = true;
		}
		Assert.assertEquals(true, gotExeption);
	}

	@Test
	public void testFailedStructList() {
		boolean gotExeption = false;

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		FieldsEntity fieldsEntity = getFieldEntity();

		ListPart item = fieldsEntity.getListPart().get(1);
		item.setTextField("stam");
		try {
			rpcSession.doAction(RpcActions.READ(), fieldsEntity);
		} catch (Exception e) {
			gotExeption = true;
		}
		Assert.assertEquals(true, gotExeption);
	}

	private static FieldsEntity getFieldEntity() {
		FieldsEntity fieldsEntity = new FieldsEntity();
		fieldsEntity.setItemNumber(1000);
		fieldsEntity.setItemName("Input text");
		fieldsEntity.setBool(true);
		Calendar calendar = new GregorianCalendar(2014, 5 - 1, 18);
		fieldsEntity.setDateField(calendar.getTime());
		InnerPart innerPart = new InnerPart();
		innerPart.setTextField("Inner text");
		fieldsEntity.setInnerPart(innerPart);
		List<ListPart> listPart = new ArrayList<ListPart>();
		fieldsEntity.setListPart(listPart);
		ListPart item = new ListPart();
		item.setTextField("First item");
		listPart.add(item);
		item = new ListPart();
		item.setTextField("Second item");
		listPart.add(item);

		return fieldsEntity;
	}

}
