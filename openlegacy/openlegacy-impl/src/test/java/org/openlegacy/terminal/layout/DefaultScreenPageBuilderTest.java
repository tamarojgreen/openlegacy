package org.openlegacy.terminal.layout;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.definitions.page.support.SimplePagePartDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.mock.ScreenForPage;
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
		ScreenEntityDefinition screen1Definition = screenEntitiesRegistry.get(ScreenForPage.class);

		PageDefinition pageDefinition = screenPageBuilder.build(screen1Definition);
		Assert.assertNotNull(pageDefinition);
		Assert.assertEquals(3, pageDefinition.getPageParts().size());

		SimplePagePartDefinition part1 = (SimplePagePartDefinition)pageDefinition.getPageParts().get(0);
		Assert.assertEquals(1, part1.getColumns());
		Assert.assertEquals(3, part1.getPartRows().size());
		Assert.assertEquals(13, part1.getLeftMargin());
		Assert.assertEquals(20, part1.getTopMargin());
		Assert.assertEquals(26, part1.getWidth());

		SimplePagePartDefinition part2 = (SimplePagePartDefinition)pageDefinition.getPageParts().get(1);
		Assert.assertEquals(2, part2.getColumns());
		Assert.assertEquals(1, part2.getPartRows().size());
		Assert.assertEquals(27, part2.getLeftMargin());
		Assert.assertEquals(37, part2.getTopMargin());
		Assert.assertEquals(51, part2.getWidth());

	}

}
