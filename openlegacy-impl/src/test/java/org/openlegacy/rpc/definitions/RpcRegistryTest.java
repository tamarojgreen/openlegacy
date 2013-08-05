package org.openlegacy.rpc.definitions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.RpcNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleRpcNumericFieldTypeDefinition;
import org.openlegacy.rpc.definitions.mock.RpcDummyEntity;
import org.openlegacy.rpc.definitions.mock.RpcDummyEntity.DummyPart;
import org.openlegacy.rpc.definitions.mock.rpcSimpleEntity;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

@ContextConfiguration("/test-rpc-basic-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcRegistryTest {

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Test
	public void testRegistryLoad() {
		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(RpcDummyEntity.class);
		Assert.assertNotNull(rpcEntityDefinition);

		RpcPartEntityDefinition rpcPartDefinition = rpcEntitiesRegistry.getPart(DummyPart.class);
		Assert.assertNotNull(rpcPartDefinition);
	}

	@Test
	public void testSimpleEntity() {
		Set<String> fieldsName = new TreeSet<String>();
		fieldsName.add("param1");
		fieldsName.add("param2");
		fieldsName.add("param3");
		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(rpcSimpleEntity.class);
		Assert.assertNotNull(rpcEntityDefinition);
		Map<String, RpcFieldDefinition> fieldsDefinitions = rpcEntityDefinition.getFieldsDefinitions();
		Assert.assertEquals(fieldsName, fieldsDefinitions.keySet());
		RpcFieldDefinition rpcFieldDefinition = fieldsDefinitions.get("param1");
		Assert.assertNotNull(rpcFieldDefinition);
		FieldTypeDefinition fieldTypeDefinition = rpcFieldDefinition.getFieldTypeDefinition();
		Assert.assertEquals(SimpleRpcNumericFieldTypeDefinition.class, fieldTypeDefinition.getClass());
		RpcNumericFieldTypeDefinition param1TypeDefinition = new SimpleRpcNumericFieldTypeDefinition(-99, 99, 0);
		Assert.assertTrue(param1TypeDefinition.equals(fieldTypeDefinition));

		RpcNumericFieldTypeDefinition param2TypeDefinition = new SimpleRpcNumericFieldTypeDefinition(-9.9, 9.9, 1);
		rpcFieldDefinition = fieldsDefinitions.get("param2");
		Assert.assertNotNull(rpcFieldDefinition);
		fieldTypeDefinition = rpcFieldDefinition.getFieldTypeDefinition();
		Assert.assertTrue(param2TypeDefinition.equals(fieldTypeDefinition));

	}

}
