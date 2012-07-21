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

/**
 * Interface for a screen entity. Currently used for identifying child screen getters methods calls to perform lazy fetching. Not
 * to be confused with {@link org.openlegacy.annotations.screen.ScreenEntity} which marks screen entity classes. This interface is
 * added to classes marked with {@link org.openlegacy.annotations.screen.ScreenEntity} using AspectJ class.
 * 
 * @author Roi Mor
 */
public interface ScreenEntity {

	String getFocusField();

	void setFocusField(String focusField);
}
