package com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;

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
	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
		if (selection.size() == 1) {
			NamedObject model = (NamedObject)selection.getFirstElement();
			if (model instanceof ScreenPartModel) {
				event.doit = false;
				return;
			}
		}
		LocalSelectionTransfer.getTransfer().setSelection(selection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {
		if (dropListener != null) {
			dropListener.dragFinished();
		}
	}

}
