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
package org.openlegacy;

/**
 * Represents a item for display, with value and display value. Returns Object to allow flexibility in value type (Integer,
 * Boolean, etc), and for the display as well. Value is used for the value from the legacy application. Display is used typically
 * used for displaying the record
 * 
 * @author Roi Mor
 * 
 * @see RecordsProvider
 */
public interface DisplayItem {

	/**
	 * The displayed item value
	 * 
	 * @return item value
	 */
	Object getValue();

	/**
	 * The displayed item presented content
	 * 
	 * @return item displayed value
	 */
	Object getDisplay();
}
