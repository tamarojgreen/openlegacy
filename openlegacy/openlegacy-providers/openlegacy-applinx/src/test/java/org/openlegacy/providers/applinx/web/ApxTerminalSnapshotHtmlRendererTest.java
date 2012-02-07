package org.openlegacy.providers.applinx.web;

import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("/test-apx-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ApxTerminalSnapshotHtmlRendererTest extends AbstractTest {

	@Inject
	private TerminalSnapshotHtmlRenderer terminalSnapshotHtmlRenderer;

	@Test
	public void testScreenRecognizer() throws IOException, GXDesignTimeException {

		TerminalSession terminalSession = newTerminalSession();

		System.out.println(terminalSnapshotHtmlRenderer.render(terminalSession.getSnapshot()));
	}

}
