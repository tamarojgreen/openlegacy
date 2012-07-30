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

import org.openlegacy.EntityFieldAccessor;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenPart;

/**
 * An interface for accessing POJO's marked with {@link ScreenEntity}, {@link ScreenPart} annotations
 * 
 * @author Roi Mor
 */
public interface ScreenPojoFieldAccessor extends EntityFieldAccessor {

	void setFocusField(String fieldName);

	void setTerminalField(String fieldName, TerminalField terminalField);

	void setTerminalSnapshot(TerminalSnapshot terminalSnapshot);

}