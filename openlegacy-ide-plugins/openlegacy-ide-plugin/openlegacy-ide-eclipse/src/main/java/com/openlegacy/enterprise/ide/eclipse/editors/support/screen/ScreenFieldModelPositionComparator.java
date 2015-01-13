package com.openlegacy.enterprise.ide.eclipse.editors.support.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;

import java.util.Comparator;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenFieldModelPositionComparator implements Comparator<ScreenFieldModel> {

	public static ScreenFieldModelPositionComparator INSTANCE = new ScreenFieldModelPositionComparator();

	private ScreenFieldModelPositionComparator() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ScreenFieldModel o1, ScreenFieldModel o2) {
		if (o1.getRow() != o2.getRow()) {
			return o1.getRow() - o2.getRow();
		}
		return o1.getColumn() - o2.getColumn();
	}

}
