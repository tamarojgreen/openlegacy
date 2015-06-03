package org.openlegacy.providers.wsrpc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.providers.wsrpc.utils.WsRpcActionUtil;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.support.SimpleRpcFields;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import junit.framework.Assert;

@ContextConfiguration("WsRpcConnectionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class WsRpcConnectionTest {

	@Inject
	WsRpcConnectionFactory rpcConnectionFactory;

	public static Map<QName, String> getProps() {
		Map<QName, String> p = new HashMap<QName, String>();
		p.put(new QName(WsRpcActionUtil.TARGET_NAMESPACE), "http://SimpleWebService/");
		p.put(new QName(WsRpcActionUtil.SERVICE_NAME), "SimpleWebService");
		return p;
	}

	@Test
	public void stringTest() {
		String callBackValue = "Vlad Drake";
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		rpcInvokeAction.setAction("callBackString");// At this moment this action name processing is inactive

		// Properties p = new Properties();
		Map<QName, String> p = getProps();
		p.put(new QName(WsRpcActionUtil.METHOD_NAME), "callBackString");
		rpcInvokeAction.setProperties(p);

		SimpleRpcStructureField inputValues = new SimpleRpcStructureField();
		inputValues.setVirtualGroup(WsRpcActionUtil.INPUT);

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackValue"); // param name
		rpcField.setValue("Vlad Drake"); // param value
		rpcField.setLength(callBackValue.length());
		rpcField.setDirection(Direction.INPUT); // Also you can use one parameter which has INPUT_OTPUT direction (if input /
												// outparam has same name)
		inputValues.getChildrens().add(rpcField);
		rpcInvokeAction.getFields().add(inputValues);

		SimpleRpcStructureField outputValues = new SimpleRpcStructureField();
		outputValues.setVirtualGroup(WsRpcActionUtil.OUTPUT);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackResult"); // result param name
		rpcField.setLength(callBackValue.length());
		rpcField.setType(String.class); // result type
		rpcField.setDirection(Direction.OUTPUT);

		outputValues.getChildrens().add(rpcField);
		rpcInvokeAction.getFields().add(outputValues);

		rpcInvokeAction.setRpcPath("http://dev.openlegacy.org/WsRpcConnectorExampleService/");

		RpcResult rpcResult = localInvoke(rpcConnection, rpcInvokeAction);

		outputValues = (SimpleRpcStructureField)rpcResult.getRpcFields().get(1);

		Assert.assertEquals(callBackValue, ((RpcFlatField)outputValues.getChildrens().get(0)).getValue());
	}

	@Test
	public void intTest() {
		int callBackValue = 100500;
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		rpcInvokeAction.setAction("callBackInteger");

		Map<QName, String> p = getProps();

		p.put(new QName(WsRpcActionUtil.METHOD_NAME), "callBackInteger");
		rpcInvokeAction.setProperties(p);

		SimpleRpcStructureField inputValues = new SimpleRpcStructureField();
		inputValues.setVirtualGroup(WsRpcActionUtil.INPUT);

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackValue");
		rpcField.setValue(callBackValue);
		rpcField.setLength(String.valueOf(callBackValue).length());
		rpcField.setDirection(Direction.INPUT);

		inputValues.getChildrens().add(rpcField);
		rpcInvokeAction.getFields().add(inputValues);

		SimpleRpcStructureField outputValues = new SimpleRpcStructureField();
		outputValues.setVirtualGroup(WsRpcActionUtil.OUTPUT);

		rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackResult");
		rpcField.setLength(4);
		rpcField.setType(int.class);
		rpcField.setDirection(Direction.OUTPUT);

		outputValues.getChildrens().add(rpcField);
		rpcInvokeAction.getFields().add(outputValues);

		rpcInvokeAction.setRpcPath("http://dev.openlegacy.org/WsRpcConnectorExampleService/");

		RpcResult rpcResult = localInvoke(rpcConnection, rpcInvokeAction);

		outputValues = (SimpleRpcStructureField)rpcResult.getRpcFields().get(1);
		Assert.assertEquals(BigDecimal.valueOf(callBackValue), ((RpcFlatField)outputValues.getChildrens().get(0)).getValue());
	}

	@Test
	public void intArrayTest() {
		int callBackValue = 100500;
		int arrayCount = 3;
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		rpcInvokeAction.setAction("callBackIntArray");

		Map<QName, String> p = getProps();
		p.put(new QName(WsRpcActionUtil.METHOD_NAME), "callBackIntArray");
		rpcInvokeAction.setProperties(p);

		SimpleRpcStructureField inputValues = new SimpleRpcStructureField();
		inputValues.setVirtualGroup(WsRpcActionUtil.INPUT);

		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		rpcField.setName("callBackValue");
		rpcField.setValue(callBackValue);
		rpcField.setLength(String.valueOf(callBackValue).length());
		rpcField.setDirection(Direction.INPUT);

		inputValues.getChildrens().add(rpcField);
		rpcInvokeAction.getFields().add(inputValues);

		SimpleRpcStructureField outputValues = new SimpleRpcStructureField();
		outputValues.setVirtualGroup(WsRpcActionUtil.OUTPUT);

		SimpleRpcStructureListField rpcArrayField = new SimpleRpcStructureListField();
		rpcArrayField.setName("callBackResult");
		SimpleRpcFields fields = new SimpleRpcFields();

		for (int i = 0; i < 3; i++) {
			rpcField = new SimpleRpcFlatField();
			rpcField.setName("item");
			rpcField.setDirection(Direction.OUTPUT);
			rpcField.setType(Integer.class);
			fields.add(rpcField);
		}

		rpcArrayField.getChildrens().add(fields);
		outputValues.getChildrens().add(rpcArrayField);
		rpcInvokeAction.getFields().add(outputValues);

		rpcInvokeAction.setRpcPath("http://dev.openlegacy.org/WsRpcConnectorExampleService/");

		RpcResult rpcResult = localInvoke(rpcConnection, rpcInvokeAction);

		rpcArrayField = (SimpleRpcStructureListField)((SimpleRpcStructureField)rpcResult.getRpcFields().get(1)).getChildrens().get(
				0);

		fields = (SimpleRpcFields)rpcArrayField.getChildrens().get(0);

		Assert.assertEquals(Integer.valueOf(arrayCount), Integer.valueOf(fields.getFields().size()));
		Assert.assertEquals(BigDecimal.valueOf(callBackValue), ((RpcFlatField)fields.getFields().get(0)).getValue());
		Assert.assertEquals(BigDecimal.valueOf(callBackValue + 1), ((RpcFlatField)fields.getFields().get(1)).getValue());
		Assert.assertEquals(BigDecimal.valueOf(callBackValue + 2), ((RpcFlatField)fields.getFields().get(2)).getValue());
	}

	@Test
	public void structureTest() {
		RpcConnection rpcConnection = rpcConnectionFactory.getConnection();
		SimpleRpcInvokeAction rpcInvokeAction = new SimpleRpcInvokeAction();

		rpcInvokeAction.setAction("callBackStructureArray");

		Map<QName, String> p = getProps();
		p.put(new QName(WsRpcActionUtil.METHOD_NAME), "callBackStructureArray");
		rpcInvokeAction.setProperties(p);

		SimpleRpcFlatField rpcField;

		SimpleRpcStructureField outputValues = new SimpleRpcStructureField();
		outputValues.setVirtualGroup(WsRpcActionUtil.OUTPUT);

		SimpleRpcStructureListField rpcArrayField = new SimpleRpcStructureListField();
		rpcArrayField.setName("callBackResult");
		SimpleRpcFields fields = new SimpleRpcFields();

		SimpleRpcStructureField field = new SimpleRpcStructureField();
		field.setName("item");

		for (int i = 0; i < 2; i++) {
			rpcField = new SimpleRpcFlatField();
			rpcField.setName(i == 0 ? "lastName" : "name");
			rpcField.setLength(10);
			rpcField.setType(String.class);
			rpcField.setDirection(Direction.OUTPUT);
			field.getChildrens().add(rpcField);
		}

		fields.add(field);
		rpcArrayField.getChildrens().add(fields);
		outputValues.getChildrens().add(rpcArrayField);
		rpcInvokeAction.getFields().add(outputValues);

		rpcInvokeAction.setRpcPath("http://dev.openlegacy.org/WsRpcConnectorExampleService/");

		RpcResult rpcResult = localInvoke(rpcConnection, rpcInvokeAction);
		outputValues = (SimpleRpcStructureField)rpcResult.getRpcFields().get(0);

		field = (SimpleRpcStructureField)((SimpleRpcFields)((SimpleRpcStructureListField)outputValues.getChildrens().get(0)).getChildrens().get(
				0)).getFields().get(0);

		Assert.assertEquals("Drake", ((RpcFlatField)field.getChildrens().get(0)).getValue());
		Assert.assertEquals("Vlad", ((RpcFlatField)field.getChildrens().get(1)).getValue());
	}

	public RpcResult localInvoke(RpcConnection rpcConnection, RpcInvokeAction rpcInvokeAction) {
		try {
			return rpcConnection.invoke(rpcInvokeAction);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new OpenLegacyRuntimeException(e));
		}
	}
}
