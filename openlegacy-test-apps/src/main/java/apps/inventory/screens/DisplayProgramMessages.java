package apps.inventory.screens;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.modules.messages.Messages.MessageField;

@ScreenEntity(screenType = Messages.MessagesEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 28, value = "Display Program Messages") })
public class DisplayProgramMessages {

	@ScreenField(row = 3, column = 2, endRow = 4, endColumn = 77, rectangle = true, fieldType = MessageField.class)
	private String message;

}
