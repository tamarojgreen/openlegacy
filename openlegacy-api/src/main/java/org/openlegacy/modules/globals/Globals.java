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
package org.openlegacy.modules.globals;

import org.openlegacy.FieldType;
import org.openlegacy.modules.SessionModule;

import java.util.Map;

/**
 * A Globals module is handling global fields and store them in the module for a later usage
 * 
 * @author Roi Mor
 */
public interface Globals extends SessionModule {

	public static final String MESSAGE_FIELD = "message";

	Map<String, Object> getGlobals();

	Object getGlobal(String name);

	public static class GlobalField implements FieldType {
	}
}
