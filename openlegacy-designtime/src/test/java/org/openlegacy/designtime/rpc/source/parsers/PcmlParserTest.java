package org.openlegacy.designtime.rpc.source.parsers;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.DesigntimeException;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.rpc.model.support.SimpleRpcEntityDesigntimeDefinition;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("PcmlParserTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PcmlParserTest {

	@Inject
	private PcmlParser pcmlParser;

	@Inject
	private GenerateUtil generateUtil;

	private RpcEntityDefinition getEntity(String sourceFile) throws IOException {
		String source = IOUtils.toString(getClass().getResource(sourceFile));
		ParseResults parseResults = pcmlParser.parse(source, sourceFile);
		RpcEntityDefinition rpcEntityDefinition = parseResults.getEntityDefinition();
		return rpcEntityDefinition;
	}

	@Test
	public void testCobolParserFlat() throws IOException, DesigntimeException {

		String sourceFile = "cobol_flat.pcml";
		SimpleRpcEntityDesigntimeDefinition rpcEntityDefinition = (SimpleRpcEntityDesigntimeDefinition)getEntity(sourceFile);
		Assert.assertNotNull(rpcEntityDefinition);

		rpcEntityDefinition.setPackageName("com.test");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		generateUtil.generate(rpcEntityDefinition, output, "RpcEntity.java.template", "");
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("CobolFlat.java.expected"));
		AssertUtils.assertContent(expectedBytes, output.toByteArray());
	}

	@Test
	public void testCobolParserStructure() throws IOException, DesigntimeException {

		String sourceFile = "cobol_structure.pcml";
		SimpleRpcEntityDesigntimeDefinition rpcEntityDefinition = (SimpleRpcEntityDesigntimeDefinition)getEntity(sourceFile);
		Assert.assertNotNull(rpcEntityDefinition);

		rpcEntityDefinition.setPackageName("com.test");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		generateUtil.generate(rpcEntityDefinition, output, "RpcEntity.java.template", "");
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("CobolStructure.java.expected"));
		AssertUtils.assertContent(expectedBytes, output.toByteArray());
	}
}
