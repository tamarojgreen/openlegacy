package org.openlegacy.terminal;

import org.openlegacy.Snapshot;

import java.util.Collection;
import java.util.List;

/**
 * A terminal screen interface. Defines common terminal screen properties Legacy vendors needs to implement this class
 */
public interface TerminalScreen extends Snapshot {

	ScreenSize getSize();

	String getText();

	String getText(ScreenPosition position, int length);

	TerminalField getField(ScreenPosition position);

	List<TerminalRow> getRows();

	Collection<TerminalField> getEditableFields();

	Object getDelegate();

}
