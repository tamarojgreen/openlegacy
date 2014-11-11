package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsMasterBlockContentProvider implements ITreeContentProvider {

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
		if (!(inputElement instanceof RpcEntity)) {
			return new Object[] {};
		}
		RpcEntity entity = (RpcEntity)inputElement;
		List<Object> list = new ArrayList<Object>();
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
		if (!(parentElement instanceof RpcPartModel)) {
			return new Object[] {};
		}
		RpcPartModel partModel = (RpcPartModel)parentElement;
		// collect fields and parts
		List<RpcNamedObject> children = new ArrayList<RpcNamedObject>();
		children.addAll(partModel.getSortedFields());
		children.addAll(partModel.getSortedParts());
		return children.toArray();
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
		if (element instanceof RpcPartModel) {
			return !(((RpcPartModel)element).getSortedFields().isEmpty() && ((RpcPartModel)element).getSortedParts().isEmpty());
		}
		return false;
	}

}
