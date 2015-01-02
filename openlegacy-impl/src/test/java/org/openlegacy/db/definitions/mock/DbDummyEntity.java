package org.openlegacy.db.definitions.mock;

import org.openlegacy.annotations.db.Action;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.annotations.db.DbEntity;
import org.openlegacy.annotations.db.DbNavigation;
import org.openlegacy.db.actions.DbActions.CREATE;
import org.openlegacy.db.actions.DbActions.DELETE;
import org.openlegacy.db.actions.DbActions.READ;
import org.openlegacy.db.actions.DbActions.UPDATE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@DbEntity(displayName = "Dummy display name", child = true, window = true)
@DbNavigation(category = "dummyCategory")
@DbActions(actions = { @Action(action = READ.class, displayName = "READ"),
		@Action(action = CREATE.class, displayName = "CREATE"), @Action(action = UPDATE.class, displayName = "UPDATE"),
		@Action(action = DELETE.class, displayName = "DELETE") })
public class DbDummyEntity implements org.openlegacy.db.DbEntity {

	@Id
	Integer id;

	// insertable = true for CREATE action
	@Column(name = "description", unique = true, nullable = false, insertable = true, updatable = false, columnDefinition = "VARCHAR(15)", length = 15, precision = 1, scale = 1)
	@DbColumn(displayName = "Dummy db column name", password = true, sampleValue = "Sample value", defaultValue = "Default value", helpText = "Help text", rightToLeft = true, internal = true, mainDisplayField = true)
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
