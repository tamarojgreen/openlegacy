package org.openlegacy.providers.applinx.languages;

import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MainframeArabicTest extends AbstractTest {

	@Test
	public void testArabic() throws IOException, GXDesignTimeException {

		TerminalSession terminalSession = newTerminalSession();
		System.out.println(terminalSession.getSnapshot().toString());
	}

}
