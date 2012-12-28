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
package org.openlegacy.designtime.terminal.generators;

import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Action;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Field;

import java.util.Collection;
import java.util.List;

/**
 * An interface which model the code model of screen classes annotation with @ScreenEntity, @ScreenPart, @ScreenTable
 * 
 * 
 */
public interface ScreenPojoCodeModel {

	boolean isRelevant();

	boolean isSupportTerminalData();

	String getClassName();

	String getEntityName();

	String getFormattedClassName();

	Collection<Field> getFields();

	String getPackageName();

	String getDisplayName();

	int getStartRow();

	int getEndRow();

	List<Action> getActions();

	String getTypeName();

	boolean isChildScreen();

	boolean isWindow();
}