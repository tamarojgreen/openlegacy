package org.openlegacy.designtime;

import org.openlegacy.EntityDefinition;

/**
 * Interface for communicate with the user interface during code generation of all entities, which allows user to customize the
 * generated code
 * 
 * @author Roi Mor
 * 
 */
public interface EntityUserInteraction<E extends EntityDefinition<?>> extends UserInteraction {

	void customizeEntity(E entityDefinition);
}
