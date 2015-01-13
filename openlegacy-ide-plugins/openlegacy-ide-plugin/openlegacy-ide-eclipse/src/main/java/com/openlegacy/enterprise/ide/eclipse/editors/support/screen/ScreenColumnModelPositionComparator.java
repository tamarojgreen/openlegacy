package com.openlegacy.enterprise.ide.eclipse.editors.support.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;

import java.util.Comparator;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenColumnModelPositionComparator implements Comparator<ScreenColumnModel> {

	public static ScreenColumnModelPositionComparator INSTANCE = new ScreenColumnModelPositionComparator();

	private ScreenColumnModelPositionComparator() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ScreenColumnModel o1, ScreenColumnModel o2) {
		if (o1.getStartColumn() != o2.getStartColumn()) {
			return o1.getStartColumn() - o2.getStartColumn();
		}
		return o1.getEndColumn() - o2.getEndColumn();
	}

}
