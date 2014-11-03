package apps.position.screens;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 23, value = "DISPLAY THE LOADALL DETAIL") })
@ScreenNavigation(accessedFrom = PositionDrillDownMenu.class)
public class PositionDrillDownDetail {

	@ScreenField(column = 60, endColumn = 64, row = 1, editable = false)
	String rrn;

}
