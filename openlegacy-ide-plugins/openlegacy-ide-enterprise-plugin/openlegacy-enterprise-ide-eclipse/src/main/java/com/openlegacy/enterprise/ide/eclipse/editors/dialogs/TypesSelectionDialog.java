package com.openlegacy.enterprise.ide.eclipse.editors.dialogs;

import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.AbstractViewerFilter;

import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Ivan Bort
 * 
 */
public class TypesSelectionDialog extends FilteredTypesSelectionDialog {

	/**
	 * @param parent
	 * @param multi
	 * @param context
	 * @param scope
	 * @param elementKinds
	 */
	public TypesSelectionDialog(Shell parent, boolean multi, IRunnableContext context, IJavaSearchScope scope,
			int elementKinds) {
		super(parent, multi, context, scope, elementKinds);
	}

	public void addListFilter(AbstractViewerFilter filter) {
		super.addListFilter(filter);
	}
}
