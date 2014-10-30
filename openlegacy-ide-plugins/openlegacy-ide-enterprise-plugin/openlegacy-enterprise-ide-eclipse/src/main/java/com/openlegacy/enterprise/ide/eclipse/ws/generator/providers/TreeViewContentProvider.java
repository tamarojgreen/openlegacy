package com.openlegacy.enterprise.ide.eclipse.ws.generator.providers;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog.TreeViewerModel;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Ivan Bort
 * 
 */
public class TreeViewContentProvider implements ITreeContentProvider {

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
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof TreeViewerModel) {
			return ((TreeViewerModel)inputElement).getVisibleEntities().toArray();
		}
		return new Object[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof AbstractEntityModel) {
			return ((AbstractEntityModel)parentElement).getVisibleChildren().toArray();
		} else if (parentElement instanceof AbstractPartModel) {
			return ((AbstractPartModel)parentElement).getVisibleChildren().toArray();
		}
		return new Object[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof AbstractEntityModel) {
			AbstractEntityModel entity = (AbstractEntityModel)element;
			return !entity.getVisibleChildren().isEmpty();
		} else if (element instanceof AbstractPartModel) {
			return !((AbstractPartModel)element).getVisibleChildren().isEmpty();
		}
		return false;
	}

}
