/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.definitions;

import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.terminal.TerminalPosition;

/**
 * A screen part definition defines a repeatable class with mappings which can belongs to a 1 or more screen entities. Typically
 * loaded from {@link ScreenPart} annotation
 * 
 * @author Roi Mor
 */
public interface ScreenPartEntityDefinition extends PartEntityDefinition<ScreenFieldDefinition> {

	/**
	 * If defined, determine the part position on the screen
	 * 
	 * @return part position
	 */
	TerminalPosition getPartPosition();

	int getWidth();

	int getTopRow();

}
