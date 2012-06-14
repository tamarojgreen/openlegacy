package apps.inventory.screens;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.modules.messages.Messages.MessageField;
import org.springframework.stereotype.Component;

@Component
@ScreenEntity(screenType = Messages.MessagesEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 28, value = "Display Program Messages") })
public class DisplayProgramMessages implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 3, column = 2, fieldType = MessageField.class)
	private String message;

	public String getMessage() {
		return message;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}
}
