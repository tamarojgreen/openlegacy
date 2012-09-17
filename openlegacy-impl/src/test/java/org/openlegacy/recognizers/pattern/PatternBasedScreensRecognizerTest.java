package org.openlegacy.recognizers.pattern;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.recognizers.pattern.mock.SignOn;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration("PatternBasedScreensRecognizerTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PatternBasedScreensRecognizerTest extends AbstractTest {

	@Test
	public void testPattern() throws IOException {
		TerminalSession terminalSession = newTerminalSession();

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);
	}

}
