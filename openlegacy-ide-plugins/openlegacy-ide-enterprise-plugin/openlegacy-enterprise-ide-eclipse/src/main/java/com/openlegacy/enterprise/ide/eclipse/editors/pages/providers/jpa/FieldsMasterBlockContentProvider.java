package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsMasterBlockContentProvider implements IStructuredContentProvider {

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
		if (!(inputElement instanceof JpaEntity)) {
			return new Object[] {};
		}
		JpaEntity entity = (JpaEntity)inputElement;
		List<Object> list = new ArrayList<Object>();
		list.addAll(entity.getSortedFields());
		return list.toArray();
	}

}
