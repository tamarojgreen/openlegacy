package org.openlegacy.designtime.formatters;

import org.openlegacy.definitions.AbstractPartEntityDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.support.AbstractFieldDefinition;
import org.openlegacy.designtime.formatters.DefinitionFormatter;
import org.openlegacy.utils.StringUtil;

/**
 * Formats field and part default names. Allow customizing name and/or other definition sproperties
 * 
 * @author Roi Mor
 * 
 */
public class DefaultDefinitionFormatter implements DefinitionFormatter {

	public void format(FieldDefinition fieldDefinition) {
		AbstractFieldDefinition<?> updatableFieldDefinition = (AbstractFieldDefinition<?>)fieldDefinition;
		updatableFieldDefinition.setName(StringUtil.toJavaFieldName(fieldDefinition.getName()));
	}

	public void format(PartEntityDefinition<?> partDefinition) {
		String name = partDefinition.getPartName().toLowerCase();
		AbstractPartEntityDefinition<?> updatablePartDefinition = (AbstractPartEntityDefinition<?>)partDefinition;
		updatablePartDefinition.setPartName(StringUtil.toClassName(name));
	}

}
