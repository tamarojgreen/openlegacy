package org.openlegacy.db.definitions.mock;

import org.openlegacy.annotations.db.DbNavigation;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@DbNavigation(category = "dummyCategory")
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
