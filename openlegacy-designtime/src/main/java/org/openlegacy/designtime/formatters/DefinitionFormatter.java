package org.openlegacy.designtime.formatters;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;

public interface DefinitionFormatter {

	void format(FieldDefinition fieldDefinition);

	void format(PartEntityDefinition<?> partDefinition);
}
