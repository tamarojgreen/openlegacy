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

import java.util.List;

/**
 * A field splitter purpose is to split a field to more than one field in case content/behavior is different Common use cases can
 * be blanks, different colors, etc.
 */
public interface TerminalFieldSplitter {

	/**
	 * Split a terminal field to multiple once. If no reason to split, return null
	 * 
	 * @param terminalField
	 * @return a split list of the field
	 */
	List<TerminalField> split(TerminalField terminalField);
}
