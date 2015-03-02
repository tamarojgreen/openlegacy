package com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.RpcEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcEntityUtils;

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
	private RpcNamedObject target = null;
	private AbstractPage page;
	private RpcNamedObject targetListObject = null;

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
			RpcEntity entity = ((RpcEntityEditor)page.getEntityEditor()).getEntity();
			Iterator<RpcNamedObject> iterator = selection.iterator();
			while (iterator.hasNext()) {
				RpcNamedObject namedObject = iterator.next();
				FieldsTreeViewerDropHelper.performDrop(entity, namedObject, target, targetListObject);
			}
			// before set new Input for viewer we must reorder fields and parts (reassign treeLevel and treeBranch)
			RpcEntityUtils.reorderFieldsAndParts(entity);

			viewer.setInput(entity);
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
		if (namedObject instanceof RpcFieldModel) {
			target = (RpcNamedObject)((RpcFieldModel)namedObject).getParent();
		} else if (namedObject instanceof RpcPartModel) {
			target = (RpcNamedObject)namedObject;
		} else {
			target = ((RpcEntityEditor)page.getEntityEditor()).getEntity().getEntityModel();
		}

		targetListObject = (RpcNamedObject)event.item.getData();

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

		RpcNamedObject transferredObject = (RpcNamedObject)selection.getFirstElement();

		RpcNamedObject targetModel = null;
		if (target instanceof RpcNamedObject) {
			targetModel = (RpcNamedObject)target;
		}
		// if we try to drop item into top level
		if (targetModel == null && target == null) {
			targetModel = ((RpcEntityEditor)page.getEntityEditor()).getEntity().getEntityModel();
		}

		if ((transferredObject == null) || (targetModel == null)) {
			return false;
		}
		// cannot be dropped to itself
		if (transferredObject.getUUID().equals(targetModel.getUUID())) {
			return false;
		}

		RpcNamedObject top = (RpcNamedObject)transferredObject.getParent();
		RpcNamedObject tmp = (RpcNamedObject)targetModel.getParent();

		if ((targetModel instanceof RpcEntityModel) && (tmp == null)) {
			tmp = targetModel;
		}

		// if parent of transferred object equal to target model
		if (top.getUUID().equals(targetModel.getUUID())) {
			// return false;
		}
		// if we are dragging a field
		if (transferredObject instanceof RpcFieldModel) {
			// if tree level the same && parent branch the same && targetModel is field
			if ((transferredObject.getTreeLevel() == targetModel.getTreeLevel()) && (top.getTreeBranch() == tmp.getTreeBranch())
					&& (targetModel instanceof RpcFieldModel)) {
				// return false;
			}
		}
		// if we are dragging a part
		if (transferredObject instanceof RpcPartModel) {
			// target model branch
			String tmb = String.valueOf(targetModel.getTreeBranch());
			// transferred object branch
			String tob = String.valueOf(transferredObject.getTreeBranch());
			// if tree level is less && target object starts with the same branch numbers
			if ((transferredObject.getTreeLevel() < targetModel.getTreeLevel()) && (tmb.startsWith(tob))) {
				return false;
			}
			// if parent the same && targetModel is field
			if (top.getUUID().equals(tmp.getUUID()) && (targetModel instanceof RpcFieldModel)) {
				return false;
			}
		}

		return true;
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
		Iterator<RpcNamedObject> iterator = selection.iterator();
		while (iterator.hasNext()) {
			RpcNamedObject rno = iterator.next();
			for (int i = 0; i < itemCount; i++) {
				TreeItem item = viewer.getTree().getItem(i);
				if (((NamedObject)item.getData()).getUUID().equals(rno.getUUID())) {
					items.add(item);
					break;
				}
				item = findTreeItem(item, rno.getUUID());
				if (item != null) {
					items.add(item);
					break;
				}
			}
		}

		if (!items.isEmpty()) {
			viewer.getTree().setSelection(items.toArray(new TreeItem[] {}));
			viewer.getTree().showSelection();
			viewer.setSelection(viewer.getSelection());
		}
	}

	private TreeItem findTreeItem(TreeItem parent, UUID uuid) {
		TreeItem item = null;
		int count = parent.getItemCount();
		if (count > 0) {
			for (int j = 0; j < count; j++) {
				TreeItem childItem = parent.getItem(j);
				if (((NamedObject)childItem.getData()).getUUID().equals(uuid)) {
					return childItem;
				}
				item = findTreeItem(childItem, uuid);
				if (item != null) {
					return item;
				}
			}
		}
		return item;
	}

	@SuppressWarnings("unchecked")
	private static boolean isAllOnOneLevel(IStructuredSelection selection) {
		if (selection.size() > 1) {
			UUID uuid = null;
			Iterator<RpcNamedObject> iterator = selection.iterator();
			while (iterator.hasNext()) {
				RpcNamedObject next = iterator.next();
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
