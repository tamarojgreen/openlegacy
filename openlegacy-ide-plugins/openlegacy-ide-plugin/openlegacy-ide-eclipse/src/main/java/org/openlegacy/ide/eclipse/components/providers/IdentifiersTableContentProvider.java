package org.openlegacy.ide.eclipse.components.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class IdentifiersTableContentProvider implements IStructuredContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement != null && inputElement instanceof ScreenEntityDefinition) {
			ScreenEntityDefinition definition = (ScreenEntityDefinition)inputElement;
			if (!definition.getScreenIdentification().getScreenIdentifiers().isEmpty()) {
				return definition.getScreenIdentification().getScreenIdentifiers().toArray();
			}
		}
		return new Object[] {};
	}

}
