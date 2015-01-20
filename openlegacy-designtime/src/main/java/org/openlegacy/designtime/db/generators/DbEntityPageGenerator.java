package org.openlegacy.designtime.db.generators;

import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.generators.EntityPageGenerator;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;

public interface DbEntityPageGenerator extends EntityPageGenerator {
	void generateView(GenerateViewRequest generateViewRequest,
			EntityDefinition<?> entityDefinition, String fileName) throws GenerationException;
}