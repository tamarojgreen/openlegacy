package com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.IEntityActionsSorter;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntityActionsSorter implements IEntityActionsSorter {

	public static final JpaEntityActionsSorter INSTANCE = new JpaEntityActionsSorter();

	private JpaEntityActionsSorter() {}

	public List<AbstractAction> sort(List<AbstractAction> actions) {
		if ((actions == null) || actions.isEmpty()) {
			return actions;
		}
		// add sorting if needed
		return actions;
	}

	@Override
	public int getOrderIndex(AbstractAction action, int defaultIndex) {
		return defaultIndex;
	}

}
