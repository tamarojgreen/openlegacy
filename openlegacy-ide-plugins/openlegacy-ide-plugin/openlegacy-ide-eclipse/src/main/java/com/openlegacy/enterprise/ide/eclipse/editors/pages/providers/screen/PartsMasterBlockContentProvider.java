package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class PartsMasterBlockContentProvider implements ITreeContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (!(inputElement instanceof ScreenEntity)) {
			return new Object[] {};
		}
		List<Object> list = new ArrayList<Object>();
		ScreenEntity entity = (ScreenEntity)inputElement;
		list.addAll(entity.getSortedFields());
		list.addAll(entity.getSortedParts());
		return list.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (!(parentElement instanceof ScreenPartModel)) {
			return new Object[] {};
		}
		ScreenPartModel partModel = (ScreenPartModel)parentElement;
		return partModel.getSortedFields().toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ScreenPartModel) {
			return !((ScreenPartModel)element).getFields().isEmpty();
		}
		return false;
	}

}
