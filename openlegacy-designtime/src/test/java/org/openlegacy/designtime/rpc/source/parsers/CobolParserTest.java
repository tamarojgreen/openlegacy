package org.openlegacy.designtime.rpc.source.parsers;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.support.SimpleRpcNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
import org.openlegacy.designtime.DesigntimeException;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcListFieldTypeDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

@ContextConfiguration("CobolParserTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CobolParserTest {

	@Inject
	private OpenlegacyCobolParser openlegacyCobolParser;

	private double precise = 0.001;

	private RpcEntityDefinition getEntity(String sourceFile) throws IOException {
		String source = IOUtils.toString(getClass().getResource(sourceFile));
		ParseResults parseResults = openlegacyCobolParser.parse(source, sourceFile);
		RpcEntityDefinition rpcEntityDefinition = parseResults.getEntityDefinition();
		return rpcEntityDefinition;
	}

	@Test
	public void testCobolParserSimpleField() throws IOException, DesigntimeException {

		String sourceFile = "simpleField.cbl";

		RpcEntityDefinition rpcEntityDefinition = getEntity(sourceFile);

		Assert.assertNotNull(rpcEntityDefinition);
		Assert.assertEquals(3, rpcEntityDefinition.getFieldsDefinitions().size());
		Map<String, RpcFieldDefinition> fieldDefinitions = rpcEntityDefinition.getFieldsDefinitions();

		// Fetch Integer
		RpcFieldDefinition fieldDefinition = fieldDefinitions.get("param1");
		Assert.assertEquals(new Integer(2), fieldDefinition.getLength());
		Assert.assertEquals(Integer.class, fieldDefinition.getJavaType());
		Assert.assertEquals("PARAM1", fieldDefinition.getOriginalName());
		FieldTypeDefinition fieldTypeDefinition = fieldDefinition.getFieldTypeDefinition();
		Assert.assertEquals(SimpleRpcNumericFieldTypeDefinition.class, fieldTypeDefinition.getClass());
		SimpleRpcNumericFieldTypeDefinition simpleRpcNumericFieldTypeDefinition = (SimpleRpcNumericFieldTypeDefinition)fieldTypeDefinition;
		Assert.assertEquals(99.0, ((SimpleRpcNumericFieldTypeDefinition)fieldTypeDefinition).getMaximumValue(), precise);
		Assert.assertEquals(0, simpleRpcNumericFieldTypeDefinition.getDecimalPlaces());
		Assert.assertEquals(0, fieldDefinition.getOrder());

		// Fetch Decimal
		fieldDefinition = fieldDefinitions.get("param2");
		Assert.assertEquals(new Integer(1), fieldDefinition.getLength());
		Assert.assertEquals(Double.class, fieldDefinition.getJavaType());
		Assert.assertEquals(SimpleRpcNumericFieldTypeDefinition.class, fieldTypeDefinition.getClass());
		fieldTypeDefinition = fieldDefinition.getFieldTypeDefinition();
		simpleRpcNumericFieldTypeDefinition = (SimpleRpcNumericFieldTypeDefinition)fieldTypeDefinition;
		Assert.assertEquals(9.9, simpleRpcNumericFieldTypeDefinition.getMaximumValue(), precise);
		Assert.assertEquals(1, simpleRpcNumericFieldTypeDefinition.getDecimalPlaces());
		Assert.assertEquals(1, fieldDefinition.getOrder());

		// String
		fieldDefinition = fieldDefinitions.get("param3");
		Assert.assertEquals(new Integer(4), fieldDefinition.getLength());
		Assert.assertEquals(String.class, fieldDefinition.getJavaType());
		fieldTypeDefinition = fieldDefinition.getFieldTypeDefinition();
		Assert.assertEquals(SimpleTextFieldTypeDefinition.class, fieldTypeDefinition.getClass());
		Assert.assertEquals(2, fieldDefinition.getOrder());
	}

	@Test
	public void testCobolParserSimpleStructure() throws IOException, DesigntimeException {

		String sourceFile = "as400sample.cbl";
		List<String> childs = new ArrayList<String>();
		childs.add("child1");
		childs.add("child2");
		RpcEntityDefinition rpcEntityDefinition = getEntity(sourceFile);
		Assert.assertNotNull(rpcEntityDefinition);
		Assert.assertEquals(0, rpcEntityDefinition.getFieldsDefinitions().size());
		Assert.assertEquals(1, rpcEntityDefinition.getPartsDefinitions().size());
		Map<String, PartEntityDefinition<RpcFieldDefinition>> partsEntityDefinition = rpcEntityDefinition.getPartsDefinitions();
		PartEntityDefinition<RpcFieldDefinition> partEntityDefinition = partsEntityDefinition.get("Param1");
		Assert.assertNotNull(partEntityDefinition);
		Map<String, RpcFieldDefinition> childFields = partEntityDefinition.getFieldsDefinitions();
		Assert.assertEquals(2, childFields.size());

		// CHILDS
		for (String childName : childs) {
			RpcFieldDefinition childField = childFields.get(childName);
			Assert.assertEquals(new Integer(2), childField.getLength());
			Assert.assertEquals(Integer.class, childField.getJavaType());
			FieldTypeDefinition fieldTypeDefinition = childField.getFieldTypeDefinition();
			Assert.assertEquals(SimpleRpcNumericFieldTypeDefinition.class, fieldTypeDefinition.getClass());
			Assert.assertEquals(99.0, ((SimpleRpcNumericFieldTypeDefinition)fieldTypeDefinition).getMaximumValue(), precise);
		}
	}

	@Test
	public void testCobolParserArrays() throws IOException {

		String sourceFile = "array.cbl";

		RpcEntityDefinition rpcEntityDefinition = getEntity(sourceFile);

		Assert.assertNotNull(rpcEntityDefinition);
		Map<String, RpcFieldDefinition> fieldsDefinitions = rpcEntityDefinition.getFieldsDefinitions();
		Map<String, PartEntityDefinition<RpcFieldDefinition>> partsDefinitions = rpcEntityDefinition.getPartsDefinitions();
		Assert.assertEquals(1, fieldsDefinitions.size());
		Assert.assertEquals(1, partsDefinitions.size());
		// Simple
		RpcFieldDefinition fieldDefinitions = fieldsDefinitions.get("field");
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

		// Array of part
		RpcPartEntityDefinition partDefinitions = (RpcPartEntityDefinition)partsDefinitions.get("Part");
		Assert.assertNotNull(partDefinitions);
		Assert.assertEquals(1, partDefinitions.getOrder());
		Assert.assertEquals(3, partDefinitions.getOccur());

		Assert.assertEquals("Part", partDefinitions.getPartName());
		Map<String, RpcFieldDefinition> partFieldDefinitions = partDefinitions.getFieldsDefinitions();
		Assert.assertEquals(2, partFieldDefinitions.size());

		// First Child
		RpcFieldDefinition childField = partFieldDefinitions.get("child1");
		Assert.assertEquals("CHILD1", childField.getOriginalName());
		Assert.assertNotNull(childField);
		Assert.assertEquals(String.class, childField.getJavaType());
		Assert.assertEquals(new Integer(10), childField.getLength());
		Assert.assertEquals(0, childField.getOrder());

		// second Child
		childField = partFieldDefinitions.get("child2");
		Assert.assertNotNull(childField);
		Assert.assertEquals(List.class, childField.getJavaType());
		fieldTypeDefinition = childField.getFieldTypeDefinition();
		Assert.assertEquals(1, childField.getOrder());

		Assert.assertEquals(new Integer(2), childField.getLength());

		SimpleRpcListFieldTypeDefinition internalListDefention = (SimpleRpcListFieldTypeDefinition)fieldTypeDefinition;
		Assert.assertEquals(6, internalListDefention.getCount());
		Assert.assertEquals(2, internalListDefention.getFieldLength());
		Assert.assertEquals(SimpleRpcNumericFieldTypeDefinition.class, internalListDefention.getItemTypeDefinition().getClass());
		Assert.assertEquals(Integer.class, internalListDefention.getItemJavaType());

	}

	@Test
	public void testInterfaceParameters() throws IOException {

		String sourceFile = "sampprog_expand.cbl";
		RpcEntityDefinition rpcEntityDefinition = getEntity(sourceFile);
		Set<String> interfaceParameters = new HashSet<String>();
		interfaceParameters.add("Dfhcommarea");
		// Test collecting parameters from program commands
		Assert.assertTrue(rpcEntityDefinition.getFieldsDefinitions().isEmpty());
		Map<String, PartEntityDefinition<RpcFieldDefinition>> partEntityDefinitions = rpcEntityDefinition.getPartsDefinitions();
		Assert.assertEquals(1, partEntityDefinitions.size());
		Assert.assertEquals(interfaceParameters, partEntityDefinitions.keySet());
	}

	@Test
	public void testTreeWithNoPreProcess() throws IOException {
		String sourceFile = "sampprog_expand.cbl";
		testTree(getEntity(sourceFile));
	}

	@Test
	public void testTreeWithPreProcess() throws IOException {
		String sourceFile = "sameprog.cbl";
		Map<String, InputStream> streamMap = new HashMap<String, InputStream>();

		streamMap.put("sampcpy1.cpy", getClass().getResourceAsStream("sampcpy1.cpy"));
		streamMap.put("sampcpy2.cpy", getClass().getResourceAsStream("sampcpy2.cpy"));
		String source = IOUtils.toString(getClass().getResource(sourceFile));
		ParseResults parseResults = openlegacyCobolParser.parse(source, streamMap);
		RpcEntityDefinition rpcEntityDefinition = parseResults.getEntityDefinition();
		testTree(rpcEntityDefinition);
	}

	@Test
	public void testCopyBookAsEntity() throws IOException {
		RpcEntityDefinition rpcEntityDefinition = getEntity("sampcpy2.cpy");

		Assert.assertNotNull(rpcEntityDefinition);
		Assert.assertTrue(rpcEntityDefinition.getFieldsDefinitions().isEmpty());
		Map<String, PartEntityDefinition<RpcFieldDefinition>> partsEntityDefinitions = rpcEntityDefinition.getPartsDefinitions();
		Assert.assertEquals(1, partsEntityDefinitions.size());

		PartEntityDefinition<RpcFieldDefinition> partEntityDefinitions = partsEntityDefinitions.get("CmVars");
		Assert.assertNotNull(partEntityDefinitions);
	}

	@Test
	public void testCopyBookWithReplaceAsEntity() throws IOException {
		RpcEntityDefinition rpcEntityDefinition = getEntity("sampcpy1.cpy");

		Assert.assertNotNull(rpcEntityDefinition);
	}

	private static void testTree(RpcEntityDefinition rpcEntityDefinition) {

		Map<String, PartEntityDefinition<RpcFieldDefinition>> partEntityDefinitions = rpcEntityDefinition.getPartsDefinitions();

		RpcPartEntityDefinition rpcPartEntityDefinition = (RpcPartEntityDefinition)partEntityDefinitions.get("Dfhcommarea");

		Map<String, RpcPartEntityDefinition> partsEntityInnerDefinitions = rpcPartEntityDefinition.getInnerPartsDefinitions();
		RpcPartEntityDefinition partEntityInnerDefinitions = partsEntityInnerDefinitions.get("CmVars");

		Map<String, RpcFieldDefinition> innerFields = partEntityInnerDefinitions.getFieldsDefinitions();
		Assert.assertEquals(3, innerFields.size());
		RpcFieldDefinition myvar = innerFields.get("cmMyvar");
		Assert.assertEquals(String.class, myvar.getJavaType());

		RpcFieldDefinition other = innerFields.get("cmOtherVar");
		Assert.assertEquals(Integer.class, other.getJavaType());
		RpcFieldDefinition another = innerFields.get("cmAnotherVar");
		Assert.assertEquals(String.class, another.getJavaType());
	}

}
