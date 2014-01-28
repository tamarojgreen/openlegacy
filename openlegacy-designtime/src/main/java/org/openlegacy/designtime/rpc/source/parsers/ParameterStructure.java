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
package org.openlegacy.designtime.rpc.source.parsers;

import java.util.List;

/**
 * ParameterStructure interface is a uniform objects that hold information about the data structure of parameters
 * 
 * VariableDeclaration - hold the original deceleration of this field Level 01 for simple objects, for structures it holds the
 * level of this field from the root SubFields - list of the parameters list
 */

public interface ParameterStructure {

	String getVariableDeclaration();

	int getLevel();

	String getFieldName();

	int getCount();

	List<ParameterStructure> getSubFields();

	boolean isSimple();

}