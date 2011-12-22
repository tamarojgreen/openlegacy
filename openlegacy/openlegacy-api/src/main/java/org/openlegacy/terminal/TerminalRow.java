package org.openlegacy.terminal;

import java.io.Serializable;
import java.util.List;

/**
 * A terminal screen interface. Defines common terminal screen properties Legacy vendors needs to implement this class
 */
public interface TerminalRow extends Serializable {

	List<TerminalField> getFields();

	/**
	 * Merge all following read-only fields to one sequenced field
	 */
	List<RowPart> getRowParts();

	TerminalField getField(int column);

	int getRowNumber();

	String getText();

	String getText(int column, int length);

}
