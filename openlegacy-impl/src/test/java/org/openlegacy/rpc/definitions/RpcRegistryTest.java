package org.openlegacy.rpc.definitions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.RpcNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleRpcNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
import org.openlegacy.rpc.definitions.mock.RpcDummyEntity;
import org.openlegacy.rpc.definitions.mock.RpcDummyEntity.DummyPart;
import org.openlegacy.rpc.definitions.mock.RpcListEntity;
import org.openlegacy.rpc.definitions.mock.RpcSimpleEntity;
import org.openlegacy.rpc.definitions.mock.RpcTreeEntity;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
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
		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(RpcSimpleEntity.class);
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
		rpcFieldDefinition = fieldsDefinitions.get("param3");
		Assert.assertNotNull(rpcFieldDefinition);
		Assert.assertEquals(new Integer(4), rpcFieldDefinition.getLength());
		fieldTypeDefinition = rpcFieldDefinition.getFieldTypeDefinition();
		Assert.assertEquals("String", fieldTypeDefinition.getTypeName());

	}

	@Test
	public void testListEntity() throws ClassNotFoundException {

		Set<String> fieldsName = new TreeSet<String>();
		fieldsName.add("field1");
		fieldsName.add("field2");

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(RpcListEntity.class);
		Assert.assertNotNull(rpcEntityDefinition);

		Map<String, RpcFieldDefinition> fieldsDefinitions = rpcEntityDefinition.getFieldsDefinitions();
		Assert.assertEquals(fieldsName, fieldsDefinitions.keySet());

		RpcFieldDefinition fieldDefinitions = fieldsDefinitions.get("field1");
		Assert.assertNotNull(fieldDefinitions);
		Assert.assertEquals(new Integer(10), fieldDefinitions.getLength());
		Assert.assertEquals(List.class, fieldDefinitions.getJavaType());
		FieldTypeDefinition fieldTypeDefinition = fieldDefinitions.getFieldTypeDefinition();
		Assert.assertEquals(SimpleRpcListFieldTypeDefinition.class, fieldTypeDefinition.getClass());
		SimpleRpcListFieldTypeDefinition simpleRpcListFieldTypeDefinition = (SimpleRpcListFieldTypeDefinition)fieldTypeDefinition;
		Assert.assertEquals(5, simpleRpcListFieldTypeDefinition.getCount());
		Assert.assertEquals(SimpleTextFieldTypeDefinition.class,
				simpleRpcListFieldTypeDefinition.getItemTypeDefinition().getClass());
		Assert.assertEquals(String.class, simpleRpcListFieldTypeDefinition.getItemJavaType());

		fieldDefinitions = fieldsDefinitions.get("field2");
		Assert.assertNotNull(fieldDefinitions);
		Assert.assertEquals(new Integer(2), fieldDefinitions.getLength());
		Assert.assertEquals(List.class, fieldDefinitions.getJavaType());
		fieldTypeDefinition = fieldDefinitions.getFieldTypeDefinition();
		Assert.assertEquals(SimpleRpcListFieldTypeDefinition.class, fieldTypeDefinition.getClass());
		simpleRpcListFieldTypeDefinition = (SimpleRpcListFieldTypeDefinition)fieldTypeDefinition;
		Assert.assertEquals(3, simpleRpcListFieldTypeDefinition.getCount());
		Assert.assertEquals(SimpleRpcNumericFieldTypeDefinition.class,
				simpleRpcListFieldTypeDefinition.getItemTypeDefinition().getClass());
		Assert.assertEquals(Integer.class, simpleRpcListFieldTypeDefinition.getItemJavaType());
	}

	@Test
	public void testTreeEntity() {

		Set<String> partsName = new TreeSet<String>();
		partsName.add("simplePart");
		partsName.add("listPart");
		partsName.add("treePart");

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(RpcTreeEntity.class);
		Assert.assertNotNull(rpcEntityDefinition);

		Map<String, PartEntityDefinition<RpcFieldDefinition>> partsDefinitions = rpcEntityDefinition.getPartsDefinitions();
		Assert.assertEquals(partsName, partsDefinitions.keySet());

		PartEntityDefinition<RpcFieldDefinition> simplePart = partsDefinitions.get("simplePart");
		Map<String, RpcFieldDefinition> fieldDefinitions = simplePart.getFieldsDefinitions();
		Assert.assertEquals(1, fieldDefinitions.size());
		Assert.assertNotNull(fieldDefinitions.get("simplePart.firstName"));

		PartEntityDefinition<RpcFieldDefinition> listPart = partsDefinitions.get("listPart");
		fieldDefinitions = listPart.getFieldsDefinitions();
		Assert.assertEquals(1, fieldDefinitions.size());
		Assert.assertNotNull(fieldDefinitions.get("listPart.firstName"));

		RpcPartEntityDefinition treePart = (RpcPartEntityDefinition)partsDefinitions.get("treePart");
		fieldDefinitions = treePart.getFieldsDefinitions();
		Assert.assertEquals(0, fieldDefinitions.size());
		Map<String, RpcPartEntityDefinition> innerParts = treePart.getInnerPartsDefinitions();
		Assert.assertEquals(5, innerParts.size());
		RpcPartEntityDefinition nestedPart = innerParts.get("nestedPart");
		Map<String, RpcFieldDefinition> nestedPartFields = nestedPart.getFieldsDefinitions();
		RpcFieldDefinition innerFieldDefinition = nestedPartFields.get("nestedPart.innerVariable");
		Assert.assertNotNull(innerFieldDefinition);
		Assert.assertEquals(String.class, innerFieldDefinition.getJavaType());
	}

}
