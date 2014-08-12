package org.openlegacy.ide.eclipse.components.support;

import org.eclipse.core.runtime.CoreException;
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
import org.openlegacy.FieldType;
import org.openlegacy.FieldType.General;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.dialogs.TypesSelectionDialog;
import org.openlegacy.ide.eclipse.dialogs.filters.AbstractViewerFilter;
import org.openlegacy.ide.eclipse.dialogs.filters.FieldTypeViewerFilter;
import org.openlegacy.ide.eclipse.util.Utils;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;

import java.net.MalformedURLException;

/**
 * @author Ivan Bort
 * 
 */
public class DialogEditingSupport extends EditingSupport {

	private TableViewer viewer;

	public DialogEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new DialogCellEditor(viewer.getTable()) {

			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				IRunnableContext context = new BusyIndicatorRunnableContext();
				IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
				TypesSelectionDialog dialog = new TypesSelectionDialog(cellEditorWindow.getDisplay().getActiveShell(), false,
						context, scope, IJavaSearchConstants.CLASS);
				AbstractViewerFilter viewerFilter = new FieldTypeViewerFilter();
				if (viewerFilter != null) {
					dialog.addListFilter(viewerFilter);
				}
				dialog.setTitle(Messages.getString("dialog_select_type"));//$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					IType res = (IType)dialog.getResult()[0];
					return res;
				}
				return null;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		if (element instanceof ScreenFieldDefinition && ((ScreenFieldDefinition)element).getType() != null) {
			return ((ScreenFieldDefinition)element).getType().getSimpleName();
		}
		return General.class.getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object element, Object value) {
		if (value instanceof IType && element instanceof SimpleScreenFieldDefinition) {
			IType res = (IType)value;
			try {
				if (res.isClass()) {
					Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName());
					SimpleScreenFieldDefinition definition = (SimpleScreenFieldDefinition)element;
					definition.setType((Class<? extends FieldType>)clazz);
				}
			} catch (MalformedURLException e) {
			} catch (CoreException e) {

			}
		}
		viewer.update(element, null);
	}

}
