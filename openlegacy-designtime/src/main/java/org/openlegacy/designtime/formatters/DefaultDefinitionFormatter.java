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
package org.openlegacy.designtime.formatters;

import org.openlegacy.definitions.AbstractPartEntityDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.support.AbstractFieldDefinition;
import org.openlegacy.utils.StringUtil;

/**
 * Formats field and part default names. Allow customizing name and/or other definition sproperties
 * 
 * @author Roi Mor
 * 
 */
public class DefaultDefinitionFormatter implements DefinitionFormatter {

	@Override
	public void format(FieldDefinition fieldDefinition) {
		AbstractFieldDefinition<?> updatableFieldDefinition = (AbstractFieldDefinition<?>)fieldDefinition;
		updatableFieldDefinition.setName(StringUtil.toJavaFieldName(fieldDefinition.getName()));
	}

	@Override
	public void format(PartEntityDefinition<?> partDefinition) {
		String name = partDefinition.getPartName().toLowerCase();
		AbstractPartEntityDefinition<?> updatablePartDefinition = (AbstractPartEntityDefinition<?>)partDefinition;
		updatablePartDefinition.setPartName(StringUtil.toClassName(name));
	}

}
