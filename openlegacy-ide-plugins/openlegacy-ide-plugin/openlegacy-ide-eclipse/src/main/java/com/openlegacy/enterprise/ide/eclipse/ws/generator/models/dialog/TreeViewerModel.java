package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class TreeViewerModel {

	protected List<AbstractEntityModel> entities = new ArrayList<AbstractEntityModel>();

	public TreeViewerModel(List<AbstractEntityModel> entities) {
		this.entities = entities;
	}

	public List<AbstractEntityModel> getEntities() {
		return entities;
	}

	public List<AbstractEntityModel> getVisibleEntities() {
		List<AbstractEntityModel> list = new ArrayList<AbstractEntityModel>();
		for (AbstractEntityModel model : entities) {
			if (model.isVisible()) {
				list.add(model);
			}
		}
		return list;
	}
}
