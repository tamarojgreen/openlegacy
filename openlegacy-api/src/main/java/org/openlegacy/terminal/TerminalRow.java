/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
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
