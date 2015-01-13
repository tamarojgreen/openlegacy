package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.TypesSelectionDialog;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.AbstractViewerFilter;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractActionsDialogCellEditingSupport extends EditingSupport {

	protected TableViewer viewer;
	protected String fieldName;

	public AbstractActionsDialogCellEditingSupport(TableViewer viewer, String fieldName) {
		super(viewer);
		this.viewer = viewer;
		this.fieldName = fieldName;
	}

	public abstract AbstractViewerFilter getViewerFilter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new DialogCellEditor(this.viewer.getTable()) {

			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				IRunnableContext context = new BusyIndicatorRunnableContext();
				IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
				TypesSelectionDialog dialog = new TypesSelectionDialog(cellEditorWindow.getDisplay().getActiveShell(), false,
						context, scope, IJavaSearchConstants.CLASS);
				AbstractViewerFilter viewerFilter = getViewerFilter();
				if (viewerFilter != null) {
					dialog.addListFilter(getViewerFilter());
				}
				dialog.setTitle(Messages.getString("Dialog.selectClass.title"));//$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					IType res = (IType)dialog.getResult()[0];
					return res;
				}
				return null;
			}
		};
	}

}
