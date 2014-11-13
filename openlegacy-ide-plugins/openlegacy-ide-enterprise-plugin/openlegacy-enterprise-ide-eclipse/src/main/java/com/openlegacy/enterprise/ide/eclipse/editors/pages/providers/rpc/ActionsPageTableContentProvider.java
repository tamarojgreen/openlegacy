package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcActionsModel;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsPageTableContentProvider implements IStructuredContentProvider {

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
		if (inputElement instanceof RpcActionsModel) {
			RpcActionsModel model = (RpcActionsModel)inputElement;
			if ((model != null) && (!model.getActions().isEmpty())) {
				return model.getActions().toArray();
			}
		}
		return new Object[] {};
	}

}
