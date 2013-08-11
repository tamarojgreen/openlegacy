package org.openlegacy.designtime.rpc.formatters;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.support.AbstractFieldDefinition;
import org.openlegacy.utils.StringUtil;

public class DefaultFieldDefinitionFormatter implements FieldDefinitionFormatter {

	public void format(FieldDefinition fieldDefinition) {
		AbstractFieldDefinition<?> updatableFieldDefinition = (AbstractFieldDefinition<?>)fieldDefinition;
		updatableFieldDefinition.setName(StringUtil.toJavaFieldName(fieldDefinition.getName()));
	}

}
