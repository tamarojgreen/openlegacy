package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 24, value = "Attributes") })
public class ConditinalFieldEntity implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 8, column = 10, when = "IF.*")
	private String whenTrue;

	@ScreenField(row = 9, column = 10, when = "IF")
	private String whenFalse;

	@ScreenField(row = 10, column = 10, unless = "UNLESS.*")
	private String unlessTrue;

	@ScreenField(row = 11, column = 10, unless = "UNLESS")
	private String unlessFalse;

	@ScreenField(row = 12, column = 10, when = "M.*", unless = ".*S")
	private String bothTrue;

	@ScreenField(row = 13, column = 10, when = "M", unless = "S")
	private String bothFalse;

	@ScreenField(row = 14, column = 10)
	private String none;

	public String getWhenTrue() {
		return whenTrue;
	}

	public void setWhenTrue(String whenTrue) {
		this.whenTrue = whenTrue;
	}

	public String getWhenFalse() {
		return whenFalse;
	}

	public void setWhenFalse(String whenFalse) {
		this.whenFalse = whenFalse;
	}

	public String getUnlessTrue() {
		return unlessTrue;
	}

	public void setUnlessTrue(String unlessTrue) {
		this.unlessTrue = unlessTrue;
	}

	public String getUnlessFalse() {
		return unlessFalse;
	}

	public void setUnlessFalse(String unlessFalse) {
		this.unlessFalse = unlessFalse;
	}

	public String getBothTrue() {
		return bothTrue;
	}

	public void setBothTrue(String bothTrue) {
		this.bothTrue = bothTrue;
	}

	public String getBothFalse() {
		return bothFalse;
	}

	public void setBothFalse(String bothFalse) {
		this.bothFalse = bothFalse;
	}

	public String getNone() {
		return none;
	}

	public void setNone(String none) {
		this.none = none;
	}

	public String getFocusField() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFocusField(String focusField) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public List<TerminalActionDefinition> getActions() {
		return Collections.EMPTY_LIST;
	}
}
