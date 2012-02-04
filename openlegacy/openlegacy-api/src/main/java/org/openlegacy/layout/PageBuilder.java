package org.openlegacy.layout;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.FieldDefinition;

public interface PageBuilder<D extends EntityDefinition<F>, F extends FieldDefinition> {

	PageDefinition build(D entityDefinition);
}
