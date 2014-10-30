package com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.rpc;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsTreeViewerDragListener implements DragSourceListener {

	private TreeViewer viewer;

	private FieldsTreeViewerDropListener dropListener = null;

	/**
	 * @param treeViewer
	 */
	public FieldsTreeViewerDragListener(TreeViewer treeViewer, FieldsTreeViewerDropListener dropListener) {
		this.viewer = treeViewer;
		this.dropListener = dropListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
		LocalSelectionTransfer.getTransfer().setSelection(selection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		if (dropListener != null) {
			dropListener.dragFinished();
		}
	}

}
