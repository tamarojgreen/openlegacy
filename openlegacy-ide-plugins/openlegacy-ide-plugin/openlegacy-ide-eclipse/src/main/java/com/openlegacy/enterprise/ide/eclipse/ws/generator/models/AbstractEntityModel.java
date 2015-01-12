package com.openlegacy.enterprise.ide.eclipse.ws.generator.models;

import org.eclipse.core.resources.IFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractEntityModel extends AbstractNamedModel {

	private IFile resourceFile = null;
	protected List<AbstractNamedModel> children = new ArrayList<AbstractNamedModel>();

	public AbstractEntityModel(String name, IFile resourceFile) {
		super(name);
		this.resourceFile = resourceFile;
	}

	public IFile getResourceFile() {
		return resourceFile;
	}

	public List<AbstractNamedModel> getChildren() {
		return children;
	}

	public List<AbstractNamedModel> getVisibleChildren() {
		List<AbstractNamedModel> list = new ArrayList<AbstractNamedModel>();
		for (AbstractNamedModel model : children) {
			if (model.isVisible()) {
				list.add(model);
			}
		}
		return list;
	}

}
