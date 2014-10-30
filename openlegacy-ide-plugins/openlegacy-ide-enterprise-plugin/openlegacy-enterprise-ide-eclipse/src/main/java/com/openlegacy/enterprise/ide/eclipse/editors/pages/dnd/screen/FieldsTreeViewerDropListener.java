package com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsTreeViewerDropListener extends ViewerDropAdapter {

	private TreeViewer viewer;
	private NamedObject target = null;
	private AbstractPage page;

	/**
	 * @param viewer
	 */
	public FieldsTreeViewerDropListener(TreeViewer viewer, AbstractPage page) {
		super(viewer);
		this.viewer = viewer;
		this.page = page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean performDrop(Object data) {
		IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
		if (!selection.isEmpty() && (target != null)) {
			ScreenEntity entity = ((ScreenEntityEditor)page.getEntityEditor()).getEntity();
			Iterator<NamedObject> iterator = selection.iterator();
			while (iterator.hasNext()) {
				NamedObject namedObject = iterator.next();
				FieldsTreeViewerDropHelper.performDrop(entity, namedObject, target);
			}
			viewer.setInput(((ScreenEntityEditor)page.getEntityEditor()).getEntity());
			viewer.expandAll();
			treeViewerSetSelection(selection);
			if (page != null) {
				page.getEditor().editorDirtyStateChanged();
			}
		}
		return true;
	}

	@Override
	public void drop(DropTargetEvent event) {
		NamedObject namedObject = (NamedObject)determineTarget(event);
		if (namedObject instanceof ScreenFieldModel) {
			target = ((ScreenFieldModel)namedObject).getParent();
		} else if (namedObject instanceof ScreenPartModel) {
			target = namedObject;
		}
		super.drop(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
		if (!isAllOnOneLevel(selection)) {
			return true;
		}

		ScreenFieldModel transferedField = (ScreenFieldModel)selection.getFirstElement();
		NamedObject targetModel = null;
		if (target instanceof NamedObject) {
			targetModel = (NamedObject)target;
		}
		if ((transferedField == null) || (targetModel == null)) {
			return false;
		}
		boolean fromEntityToPart = false;
		boolean fromEntityToPartField = false;
		boolean fromPartToEntity = false;
		boolean fromPartToPart = false;
		boolean fromPartToPartField = false;
		// from entity to part
		if ((transferedField.getParent() instanceof ScreenEntityModel) && (targetModel instanceof ScreenPartModel)) {
			fromEntityToPart = true;
		}
		// from entity to field inside part
		if ((transferedField.getParent() instanceof ScreenEntityModel) && (targetModel instanceof ScreenFieldModel)
				&& (((ScreenFieldModel)targetModel).getParent() instanceof ScreenPartModel)) {
			fromEntityToPartField = true;
		}
		// from part to field inside entity
		if ((transferedField.getParent() instanceof ScreenPartModel) && (targetModel instanceof ScreenFieldModel)
				&& (((ScreenFieldModel)targetModel).getParent() instanceof ScreenEntityModel)) {
			fromPartToEntity = true;
		}
		// from part to part
		if ((transferedField.getParent() instanceof ScreenPartModel) && (targetModel instanceof ScreenPartModel)
				&& (!transferedField.getParent().getUUID().equals(targetModel.getUUID()))) {
			fromPartToPart = true;
		}
		// from part to field inside part
		if ((transferedField.getParent() instanceof ScreenPartModel) && (targetModel instanceof ScreenFieldModel)
				&& (((ScreenFieldModel)targetModel).getParent() instanceof ScreenPartModel)
				&& (!transferedField.getParent().getUUID().equals(targetModel.getParent().getUUID()))) {
			fromPartToPartField = true;
		}
		return fromEntityToPart || fromEntityToPartField || fromPartToEntity || fromPartToPart || fromPartToPartField;
	}

	public void dragFinished() {
		target = null;
	}

	@SuppressWarnings("unchecked")
	private void treeViewerSetSelection(IStructuredSelection selection) {
		int itemCount = viewer.getTree().getItemCount();
		if (selection.isEmpty() || itemCount == 0) {
			return;
		}

		List<TreeItem> items = new ArrayList<TreeItem>();
		Iterator<NamedObject> iterator = selection.iterator();
		while (iterator.hasNext()) {
			NamedObject no = iterator.next();
			for (int i = 0; i < itemCount; i++) {
				TreeItem item = viewer.getTree().getItem(i);
				// for entity elements
				if (((NamedObject)item.getData()).getUUID().equals(no.getUUID())) {
					items.add(item);
					break;
				}
				// for part elements
				int count = item.getItemCount();
				if (count > 0) {
					for (int j = 0; j < count; j++) {
						TreeItem item2 = item.getItem(j);
						if (((NamedObject)item2.getData()).getUUID().equals(no.getUUID())) {
							items.add(item2);
							break;
						}
					}
				}
			}
		}
		if (!items.isEmpty()) {
			viewer.getTree().setSelection(items.toArray(new TreeItem[] {}));
			viewer.getTree().showSelection();
			viewer.setSelection(viewer.getSelection());
		}
	}

	@SuppressWarnings("unchecked")
	private static boolean isAllOnOneLevel(IStructuredSelection selection) {
		if (selection.size() > 1) {
			UUID uuid = null;
			Iterator<NamedObject> iterator = selection.iterator();
			while (iterator.hasNext()) {
				NamedObject next = iterator.next();
				if (next instanceof ScreenPartModel) {
					return false;
				}
				if (uuid == null) {
					uuid = next.getParent().getUUID();
				}
				if (!uuid.equals(next.getParent().getUUID())) {
					return false;
				}
			}
		}
		return true;
	}

}
