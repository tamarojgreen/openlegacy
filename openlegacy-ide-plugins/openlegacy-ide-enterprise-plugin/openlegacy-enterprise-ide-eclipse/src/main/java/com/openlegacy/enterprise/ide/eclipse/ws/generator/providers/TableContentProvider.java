package com.openlegacy.enterprise.ide.eclipse.ws.generator.providers;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog.InTableModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog.OutTableModel;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Ivan Bort
 * 
 */
public class TableContentProvider implements IStructuredContentProvider {

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
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof InTableModel) {
			return ((InTableModel)inputElement).getElements().toArray();
		} else if (inputElement instanceof OutTableModel) {
			return ((OutTableModel)inputElement).getElements().toArray();
		}
		return new Object[] {};
	}

}
