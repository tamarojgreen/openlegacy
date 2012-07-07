package org.openlegacy;

/**
 * Define a bidirectional binder between a session snapshot and an entity. Used for building a entity from a snapshot and
 * collecting data for send from a given entity. Binders are used to bind entities java types fields to a send action
 * 
 * @author Roi Mor
 */
public interface EntityBinder<S extends Snapshot, A extends SendAction> {

	/**
	 * populates the given entity with values from the given snapshot. Typically using metadata on {@link EntityDefinition} from
	 * the {@link EntitiesRegistry}
	 * 
	 * @param entity
	 * @param snapshot
	 */
	void populateEntity(Object entity, S snapshot);

	/**
	 * Populates the given send action with values from the entity
	 * 
	 * @param sendAction
	 *            The send action to populate
	 * @param snapshot
	 *            used for comparing current values of fields to new values from the entity
	 * @param entity
	 *            the entity from which new values are taken
	 */
	void populateSendAction(A sendAction, S snapshot, Object entity);

}
