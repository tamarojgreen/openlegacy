package com.openlegacy.enterprise.ide.eclipse.ws.generator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractPartModel extends AbstractNamedModel {

	protected List<AbstractNamedModel> children = new ArrayList<AbstractNamedModel>();

	public AbstractPartModel(String name, AbstractNamedModel parent) {
		super(name, parent);
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
