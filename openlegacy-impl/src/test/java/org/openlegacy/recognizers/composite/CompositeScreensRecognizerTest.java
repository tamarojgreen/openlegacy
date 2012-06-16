package org.openlegacy.recognizers.composite;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.recognizers.composite.mock.ScreenWithIdentifiers;
import org.openlegacy.recognizers.composite.mock.ScreenWithoutIdentifiers;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration("CompositeScreensRecognizerTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CompositeScreensRecognizerTest extends AbstractTest {

	@Test
	public void testComposite() throws IOException {
		TerminalSession terminalSession = newTerminalSession();

		ScreenWithIdentifiers screenWithIdentifiers = terminalSession.getEntity(ScreenWithIdentifiers.class);
		Assert.assertNotNull(screenWithIdentifiers);
		terminalSession.doAction(TerminalActions.ENTER());
		ScreenWithoutIdentifiers screenWithoutIdentifiers = terminalSession.getEntity(ScreenWithoutIdentifiers.class);
		Assert.assertNotNull(screenWithoutIdentifiers);
	}
}
