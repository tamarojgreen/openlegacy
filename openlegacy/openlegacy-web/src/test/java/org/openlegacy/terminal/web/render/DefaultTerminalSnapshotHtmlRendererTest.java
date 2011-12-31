package org.openlegacy.terminal.web.render;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("classpath:/test-web-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotHtmlRendererTest extends AbstractTest {

	@Inject
	private TerminalSnapshotHtmlRenderer htmlRenderer;

	@Test
	public void testHtmlRender() {
		TerminalSession terminalSession = newTerminalSession();
		// terminalSession.doAction(TerminalActions.ENTER());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		htmlRenderer.render(terminalSession.getSnapshot(), baos);
		System.out.println(new String(baos.toByteArray()));
	}
}
