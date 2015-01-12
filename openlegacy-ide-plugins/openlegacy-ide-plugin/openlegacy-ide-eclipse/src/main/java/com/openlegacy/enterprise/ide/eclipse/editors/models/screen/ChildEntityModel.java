package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ChildEntityModel extends ScreenEntityModel {

	private Class<?> clazz = null;
	private String previousClassName = "";

	public ChildEntityModel() {
		super();
		this.className = "";
	}

	public ChildEntityModel(UUID uuid) {
		super();
		this.uuid = uuid;
		this.className = "";
	}

	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		super.init(entityDefinition);
		this.previousClassName = this.className;
	}

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		super.init(screenFieldDefinition);
	}

	@Override
	public ChildEntityModel clone() {
		ChildEntityModel model = new ChildEntityModel(this.uuid);
		model.setModelName(this.modelName);
		model.setClassName(this.className);
		model.setClazz(this.clazz);
		model.previousClassName = this.previousClassName;
		return model;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getPreviousClassName() {
		return previousClassName;
	}

}
