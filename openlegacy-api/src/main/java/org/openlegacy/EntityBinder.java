package org.openlegacy;

import org.openlegacy.SendAction;
import org.openlegacy.Snapshot;

/**
 * Define a binder API for building a entity from a snapshot and collecting data from a given entity
 * 
 */
public interface EntityBinder<S extends Snapshot, A extends SendAction> {

	void populateEntity(Object entity, S snapshot);

	void populateSendAction(A sendAction, S snapshot, Object entity);

}
