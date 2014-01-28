package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.rpc.generators.mock.ItemDetails;
import org.openlegacy.designtime.rpc.generators.mock.Items;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.layout.RpcPageBuilder;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("RpcEntityMvcGeneratorTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcEntityMvcGeneratorTest {

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Inject
	private RpcPageBuilder rpcPageBuilder;

	@Inject
	private RpcEntityMvcGenerator rpcEntityMvcGenerator;

	@Inject
	private RpcHelpGenerator helpGenerator;

	@Test
	public void testGenerateControllerWithParam() throws Exception {
		testGenerateController(ItemDetails.class, "ItemDetailsController.java.expected");

	}

	@Test
	public void testGenerateControllerWithNoParam() throws Exception {
		testGenerateController(Items.class, "Items.java.expected");

	}

	@Test
	public void testGenerateHelp() throws TemplateException, IOException {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(ItemDetails.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = rpcPageBuilder.build(rpcDefinition);
		helpGenerator.generate(pageDefinition, baos);
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ItemDetailsHelp.html.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	private void testGenerateController(Class<?> testClass, String expectedStream) throws Exception {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(testClass);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = rpcPageBuilder.build(rpcDefinition);
		rpcEntityMvcGenerator.generateController(pageDefinition, baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectedStream));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

}
