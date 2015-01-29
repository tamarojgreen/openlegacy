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
package org.openlegacy.definitions;

/**
 * Defines a dynamic field registry information stored within {@link FieldDefinition}.
 * 
 * @author Gad Salner
 */
public interface DynamicFieldDefinition{

	String getText();
	
	Integer getRow();

	Integer getColumn();

	Integer getEndColumn();

	Integer getEndRow();
	
	Integer getFieldOffset();

}
