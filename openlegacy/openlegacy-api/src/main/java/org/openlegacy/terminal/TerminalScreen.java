package org.openlegacy.terminal;

import java.util.Collection;

import org.openlegacy.Snapshot;

/**
 *  A terminal screen interface. Defines common terminal screen properties  
 *  Legacy vendors needs to implement this class

 */
public interface TerminalScreen extends Snapshot {

	ScreenSize getSize();

	String getText();

	String getText(ScreenPosition position, int length);

	TerminalField getField(ScreenPosition position);

	Collection<TerminalField> getEditableFields();

	Object getDelegate();

}
