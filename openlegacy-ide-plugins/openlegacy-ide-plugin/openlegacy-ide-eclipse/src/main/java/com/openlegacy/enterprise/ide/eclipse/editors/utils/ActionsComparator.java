package com.openlegacy.enterprise.ide.eclipse.editors.utils;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;

import java.util.Comparator;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsComparator implements Comparator<AbstractAction> {

	private static final ActionsComparator INSTANCE = new ActionsComparator();

	private List<String> order = null;

	private ActionsComparator() {}

	public static ActionsComparator getInstance(List<String> order) {
		INSTANCE.order = order;
		return INSTANCE;
	}

	@Override
	public int compare(AbstractAction o1, AbstractAction o2) {
		if (this.order == null) {
			return 0;
		}
		Integer i1 = null;
		if (order.contains(o1.getKey())) {
			i1 = order.indexOf(o1.getKey());
		}
		Integer i2 = null;
		if (order.contains(o2.getKey())) {
			i2 = order.indexOf(o2.getKey());
		}
		if ((i1 != null) && (i2 != null)) {
			return Integer.signum(i1 - i2);
		}
		return 0;
	}

}
