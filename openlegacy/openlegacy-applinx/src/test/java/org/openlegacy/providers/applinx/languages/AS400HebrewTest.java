package org.openlegacy.providers.applinx.languages;

import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.utils.ScreenPainter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AS400HebrewTest extends AbstractTest {

	@Test
	public void testHebrew() throws IOException, GXDesignTimeException {

		TerminalSession terminalSession = newTerminalSession();
		System.out.println(ScreenPainter.paint(terminalSession.getSnapshot(), true));
	}

}
