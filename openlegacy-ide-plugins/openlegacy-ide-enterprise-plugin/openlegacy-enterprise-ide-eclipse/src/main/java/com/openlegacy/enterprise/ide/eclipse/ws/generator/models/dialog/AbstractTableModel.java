package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenPartModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractTableModel {

	private List<AbstractNamedModel> elements = new ArrayList<AbstractNamedModel>();

	public List<AbstractNamedModel> getElements() {
		return elements;
	}

	public void moveUp(AbstractNamedModel model) {
		if (model == null || !containsElement(model)) {
			return;
		}
		int index = elements.indexOf(model);
		if (index > 0) {
			Collections.swap(elements, index, index - 1);
		}
	}

	public void moveDown(AbstractNamedModel model) {
		if (model == null || !containsElement(model)) {
			return;
		}
		int index = elements.indexOf(model);
		if (index < (elements.size() - 1)) {
			Collections.swap(elements, index, index + 1);
		}
	}

	public void addElement(AbstractNamedModel model) {
		if (containsElement(model)) {
			return;
		}
		// if we try to add field, but his parent already added
		if (containsElement(model.getParent())) {
			return;
		}
		// if we try to add field from part, but entity of this part is already added
		if (model.getParent() != null && containsElement(model.getParent().getParent())) {
			return;
		}

		// if we try to add a parent of field or screen part, we need to remove these items form elements
		if ((model instanceof ScreenEntityModel) || (model instanceof ScreenPartModel) || (model instanceof RpcEntityModel)
				|| (model instanceof RpcPartModel)) {
			List<AbstractNamedModel> toRemoveList = new ArrayList<AbstractNamedModel>();
			for (AbstractNamedModel namedModel : elements) {
				// if namedModel is field inside Entity or Part
				// or if namedModel is field inside Part inside Entity
				if (model.compareUUID(namedModel.getParent())
						|| ((namedModel.getParent() != null) && (namedModel.getParent().getParent() != null) && model.compareUUID(namedModel.getParent().getParent()))) {
					toRemoveList.add(namedModel);
				}
			}
			if (!toRemoveList.isEmpty()) {
				elements.removeAll(toRemoveList);
			}
		}
		elements.add(model);
	}

	private boolean containsElement(AbstractNamedModel element) {
		if (element == null) {
			return false;
		}
		return elements.contains(element);
	}

}
