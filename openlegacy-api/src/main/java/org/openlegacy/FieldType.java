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
package org.openlegacy;

/**
 * Field type define the business purpose of the field. Example usage: User field, password field, menu field, etc
 * 
 * FieldType may be used by session modules which are interested of understand the legacy application fields, by querying the
 * registry
 * 
 * It is possible to define more field types by implementing this interface
 * 
 * @see org.openlegacy.modules.login.Login.UserField
 * @see org.openlegacy.modules.SessionModule
 * 
 * @author Roi Mor
 */
public interface FieldType {

	/**
	 * General field type. The default field type for all fields unless specified oterwise
	 */
	public static class General implements FieldType {
	}
}
