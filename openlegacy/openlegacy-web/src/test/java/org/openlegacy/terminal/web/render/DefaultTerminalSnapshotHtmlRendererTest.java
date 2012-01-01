package org.openlegacy.terminal.web.render;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("/test-web-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotHtmlRendererTest extends AbstractTest {

	@Inject
	private TerminalSnapshotHtmlRenderer htmlRenderer;

	@Test
	public void testHtmlRender() {
		TerminalSession terminalSession = newTerminalSession();
		// terminalSession.doAction(TerminalActions.ENTER());
		String result = htmlRenderer.render(terminalSession.getSnapshot());
		System.out.println(result);
	}
}
