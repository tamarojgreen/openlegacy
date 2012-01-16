package apps.inventory.screens;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.menu.Menu.MenuEntity;

@ScreenEntity(screenType = MenuEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 37, value = "Demo Environment") })
public class MainMenu {

	@ScreenField(row = 21, column = 74, editable = true)
	private Integer company;
	@ScreenField(row = 21, column = 8, editable = true)
	private Integer selection;
}
