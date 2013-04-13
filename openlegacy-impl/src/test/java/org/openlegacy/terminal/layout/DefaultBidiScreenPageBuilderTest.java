package org.openlegacy.terminal.layout;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.definitions.page.support.SimplePagePartDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.layout.PagePartRowDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.mock_bidi.BidiScreenForPage;
import org.openlegacy.terminal.layout.support.DefaultScreenPageBuilder;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("DefaultBidiScreenPageBuilderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultBidiScreenPageBuilderTest extends AbstractTest {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private DefaultScreenPageBuilder screenPageBuilder;

	@Test
	public void testPageBuild() throws IOException {
		ScreenEntityDefinition screen1Definition = screenEntitiesRegistry.get(BidiScreenForPage.class);

		PageDefinition pageDefinition = screenPageBuilder.build(screen1Definition);
		Assert.assertNotNull(pageDefinition);
		Assert.assertEquals(1, pageDefinition.getPageParts().size());

		SimplePagePartDefinition part1 = (SimplePagePartDefinition)pageDefinition.getPageParts().get(0);
		Assert.assertEquals(3, part1.getColumns());
		Assert.assertEquals(2, part1.getPartRows().size());
		PagePartRowDefinition firstRow = part1.getPartRows().get(0);
		Assert.assertEquals(2, firstRow.getFields().size());
		Assert.assertEquals("fldCol2", firstRow.getFields().get(0).getName());
		// left position in % of the first field (label is on the right)
		Assert.assertEquals(27, part1.getLeftMargin());
		Assert.assertEquals(20, part1.getTopMargin());
		Assert.assertEquals(71, part1.getWidth());

	}

}
