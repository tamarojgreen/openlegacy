package org.openlegacy.ide.eclipse;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.openlegacy.designtime.mains.DesignTimeExecuter;

public class OpenLegacyProjectFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object object1, Object object2) {
		if (object2 instanceof IJavaElement) {
			String name = ((IJavaElement)object2).getElementName();
			if (name.endsWith(DesignTimeExecuter.ASPECT_SUFFIX)) {
				return false;
			}
		}
		return true;
	}
}
