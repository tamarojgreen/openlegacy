package com.openlegacy.enterprise.ide.eclipse.editors.utils;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;

/**
 * @author Ivan Bort
 * 
 */
public interface IEntityActionsSorter {

	public int getOrderIndex(AbstractAction action, int defaultIndex);
}
