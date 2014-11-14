package org.openlegacy.db.definitions.mock;

import org.openlegacy.annotations.db.Action;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbNavigation;
import org.openlegacy.db.DbEntity;
import org.openlegacy.db.actions.DbActions.CREATE;
import org.openlegacy.db.actions.DbActions.DELETE;
import org.openlegacy.db.actions.DbActions.READ;
import org.openlegacy.db.actions.DbActions.UPDATE;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@DbNavigation(category = "dummyCategory")
@DbActions(actions = { @Action(action = READ.class, displayName = "READ"),
		@Action(action = CREATE.class, displayName = "CREATE"), @Action(action = UPDATE.class, displayName = "UPDATE"),
		@Action(action = DELETE.class, displayName = "DELETE") })
public class DbDummyEntity implements DbEntity {

	@Id
	Integer id;

	String description;

	public DbDummyEntity() {}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
