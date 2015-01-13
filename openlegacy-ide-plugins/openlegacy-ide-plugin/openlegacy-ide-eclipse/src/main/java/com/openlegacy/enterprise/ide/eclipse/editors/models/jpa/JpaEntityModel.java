package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;

import java.util.UUID;

import javax.persistence.Entity;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntityModel extends JpaNamedObject {

	// @Entity annoation attributes
	private String name = "";
	// @DbEntity annotation attributes
	private String displayName = "";
	private boolean window = false;
	private boolean child = false;

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
		displayName = dbEntityDefinition.getDisplayName();
		window = dbEntityDefinition.isWindow();
		child = dbEntityDefinition.isChild();
	}

	@Override
	public JpaEntityModel clone() {
		JpaEntityModel model = new JpaEntityModel(this.uuid);
		model.setName(name);
		model.setDisplayName(displayName);
		model.setWindow(window);
		model.setChild(child);
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isWindow() {
		return window;
	}

	public void setWindow(boolean window) {
		this.window = window;
	}

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public boolean isDefaultDbEntityAttrs() {
		return StringUtils.isEmpty(displayName) && !window && !child;
	}

	public boolean equalsDbEntityAttrs(JpaEntityModel model) {
		return StringUtils.equals(displayName, model.getDisplayName()) && window == model.isWindow() && child == model.isChild();
	}

}
