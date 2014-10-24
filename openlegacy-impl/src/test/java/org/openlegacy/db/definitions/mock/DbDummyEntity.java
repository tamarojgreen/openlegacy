package org.openlegacy.db.definitions.mock;

import org.openlegacy.annotations.db.Action;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbNavigation;
import org.openlegacy.db.actions.DbActions.READ;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@DbNavigation(category = "dummyCategory")
@DbActions(actions = { @Action(action = READ.class, displayName = "READ") })
public class DbDummyEntity {

	@Id
	Integer id;

	String description;

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
