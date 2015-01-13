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

package com.openlegacy.enterprise.ide.eclipse.editors.models.enums;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public interface IEnumFieldModel {

	String getPrevJavaTypeName();

	String getJavaTypeName();

	String getFieldName();

	List<EnumEntryModel> getEntries();

}
