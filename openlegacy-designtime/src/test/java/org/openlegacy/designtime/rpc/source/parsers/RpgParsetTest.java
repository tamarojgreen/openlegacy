package org.openlegacy.designtime.rpc.source.parsers;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openlegacy.designtime.DesigntimeException;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

import java.io.IOException;
import java.util.Map;

public class RpgParsetTest {

	@Test
	public void testRpgParsing() throws IOException, DesigntimeException {
		RpgParser rpgParser = new RpgParser();
		String sourceFile = "example.rpg";
		String source = IOUtils.toString(getClass().getResource(sourceFile));
		// String entityName = sourceFile.substring(0, sourceFile.indexOf(".") > 0 ? sourceFile.indexOf(".") :
		// sourceFile.length());
		RpcEntityDefinition rpcEntityDefinition = rpgParser.parse(source);
		Assert.assertNotNull(rpcEntityDefinition);
		Assert.assertEquals(rpcEntityDefinition.getFieldsDefinitions().size(), 4);
		Map<String, RpcFieldDefinition> fieldDefinition = rpcEntityDefinition.getFieldsDefinitions();
		RpcFieldDefinition firstNameField = fieldDefinition.get("firstName");
		Assert.assertTrue(Math.abs(firstNameField.getLength() - 20) < 0.001);
		Assert.assertEquals(firstNameField.getJavaType(), String.class);

		RpcFieldDefinition ageField = fieldDefinition.get("myAge");
		Assert.assertTrue(Math.abs(ageField.getLength() - 3) < 0.001);
		Assert.assertEquals(ageField.getJavaType(), Integer.class);

	}
}
