package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;

import java.util.UUID;

import javax.persistence.Entity;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntityModel extends JpaNamedObject {

	// annoation attributes
	private String name = "";

	protected String className;

	public JpaEntityModel() {
		super(Entity.class.getSimpleName());
	}

	public JpaEntityModel(UUID uuid) {
		super(Entity.class.getSimpleName());
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedDbEntityDefinition dbEntityDefinition) {
		name = dbEntityDefinition.getName();
		className = dbEntityDefinition.getEntityClassName();
	}

	@Override
	public JpaEntityModel clone() {
		JpaEntityModel model = new JpaEntityModel(this.uuid);
		model.setName(name);
		return model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return this.className;
	}

}
