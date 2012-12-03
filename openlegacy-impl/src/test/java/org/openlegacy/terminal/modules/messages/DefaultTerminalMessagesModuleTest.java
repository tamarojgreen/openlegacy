package org.openlegacy.terminal.modules.messages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("DefaultTerminalMessagesModuleTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalMessagesModuleTest extends AbstractTest {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Test
	public void checkMessagesExistance() {
		TerminalSession terminalSession = newTerminalSession();
		terminalSession.doAction(TerminalActions.ENTER());

		// verify messages screen was skipped
		ScreenEntity currentEntity = terminalSession.getEntity();
		ScreenEntityDefinition currentScreenDefinition = screenEntitiesRegistry.get(currentEntity.getClass());
		Assert.assertEquals("MainMenu", currentScreenDefinition.getEntityClassName());

		// verify messages are stored
		Messages messagesModule = terminalSession.getModule(Messages.class);
		List<String> messages = messagesModule.getMessages();
		Assert.assertEquals(2, messages.size());
		Assert.assertEquals("Message line 1 Message line 2", messages.get(0));
		Assert.assertEquals("Message line 3 Message line 4", messages.get(1));

		// verify messages are reset
		messagesModule.resetMessages();
		messages = messagesModule.getMessages();
		Assert.assertEquals(0, messages.size());

	}
}
