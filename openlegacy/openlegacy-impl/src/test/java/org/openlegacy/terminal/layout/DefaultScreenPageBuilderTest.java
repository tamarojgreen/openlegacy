package org.openlegacy.terminal.layout;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.definitions.page.support.SimplePagePartDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.mock.Screen1;
import org.openlegacy.terminal.layout.support.DefaultScreenPageBuilder;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultScreenPageBuilderTest extends AbstractTest {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private DefaultScreenPageBuilder screenPageBuilder;

	@Test
	public void testPageBuild() throws IOException {
		ScreenEntityDefinition screen1Definition = screenEntitiesRegistry.get(Screen1.class);

		PageDefinition pageDefinition = screenPageBuilder.build(screen1Definition);
		Assert.assertNotNull(pageDefinition);
		Assert.assertEquals(3, pageDefinition.getPageParts().size());

		SimplePagePartDefinition part1 = (SimplePagePartDefinition)pageDefinition.getPageParts().get(0);
		Assert.assertEquals(1, part1.getColumns());
		Assert.assertEquals(3, part1.getPartRows().size());
		Assert.assertEquals(76, part1.getLeftMarginPercentage());
		Assert.assertEquals(4, part1.getTopMarginPercentage());
		Assert.assertEquals(22, part1.getWidthPercentage());

		SimplePagePartDefinition part2 = (SimplePagePartDefinition)pageDefinition.getPageParts().get(1);
		Assert.assertEquals(1, part2.getColumns());
		Assert.assertEquals(3, part2.getPartRows().size());
		Assert.assertEquals(53, part2.getLeftMarginPercentage());
		Assert.assertEquals(20, part2.getTopMarginPercentage());
		Assert.assertEquals(25, part2.getWidthPercentage());

		SimplePagePartDefinition part3 = (SimplePagePartDefinition)pageDefinition.getPageParts().get(2);
		Assert.assertEquals(2, part3.getColumns());
		Assert.assertEquals(1, part3.getPartRows().size());
		Assert.assertEquals(28, part3.getLeftMarginPercentage());
		Assert.assertEquals(37, part3.getTopMarginPercentage());
		Assert.assertEquals(36, part3.getWidthPercentage());

	}

}
