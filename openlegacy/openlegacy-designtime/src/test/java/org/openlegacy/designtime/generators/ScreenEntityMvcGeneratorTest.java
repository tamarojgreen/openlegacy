package org.openlegacy.designtime.generators;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.generators.ScreenEntityMvcGenerator;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.ScreenPageBuilder;
import org.openlegacy.terminal.layout.mock.ScreenForPage;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntityMvcGeneratorTest {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;
	@Inject
	private ScreenPageBuilder screenPageBuilder;

	@Test
	public void testGenerateJspx() throws Exception {

		ScreenEntityDefinition screen1Definition = screenEntitiesRegistry.get(ScreenForPage.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PageDefinition pageDefinition = screenPageBuilder.build(screen1Definition);
		new ScreenEntityMvcGenerator().generateJspx(pageDefinition, baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPage.jspx.expected"));

		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}
}
