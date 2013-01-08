package org.openlegacy.designtime.generators.mock;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.List;

@ScreenEntity
public class ScreenForPagewithKey implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 6, column = 22, endColumn = 31, key = true)
	private String keyField;
	
	public String getKeyField();
	
	String getFocusField(){return null;}

	void setFocusField(String focusField){}
	

}
