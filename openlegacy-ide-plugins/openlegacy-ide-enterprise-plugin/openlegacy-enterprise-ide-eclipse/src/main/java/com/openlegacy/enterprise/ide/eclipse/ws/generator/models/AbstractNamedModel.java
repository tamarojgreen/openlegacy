package com.openlegacy.enterprise.ide.eclipse.ws.generator.models;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractNamedModel {

	private String name = null;
	private AbstractNamedModel parent = null;
	private UUID uuid = UUID.randomUUID();
	// determines a visible state in treeview
	private boolean isVisible = true;

	public AbstractNamedModel(String name) {
		this.name = name;
		this.parent = null;
	}

	public AbstractNamedModel(String name, AbstractNamedModel parent) {
		this.name = name;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public AbstractNamedModel getParent() {
		return parent;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public UUID getUUID() {
		return uuid;
	}

	public boolean compareUUID(AbstractNamedModel model) {
		if (model == null) {
			return false;
		}
		return uuid.compareTo(model.getUUID()) == 0;
	}

}
