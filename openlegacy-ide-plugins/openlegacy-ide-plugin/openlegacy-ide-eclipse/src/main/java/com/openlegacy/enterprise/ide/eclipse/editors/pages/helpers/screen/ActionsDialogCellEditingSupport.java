package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.AbstractViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.SimpleTerminalMappedActionViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.TerminalActionViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.AbstractActionsDialogCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.SimpleTerminalMappedAction;

import java.net.MalformedURLException;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsDialogCellEditingSupport extends AbstractActionsDialogCellEditingSupport {

	public ActionsDialogCellEditingSupport(TableViewer viewer, String fieldName) {
		super(viewer, fieldName);
	}

	@Override
	public AbstractViewerFilter getViewerFilter() {
		if (fieldName.equals(AnnotationConstants.ACTION)) {
			return new TerminalActionViewerFilter();
		} else if (fieldName.equals(AnnotationConstants.KEYBOARD_KEY)) {
			return new SimpleTerminalMappedActionViewerFilter();
		}
		return null;
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
		if (this.fieldName.equals(AnnotationConstants.ACTION)) {
			return ((ActionModel)element).getActionName();
		} else if (fieldName.equals(ScreenAnnotationConstants.TARGET_ENTITY)) {
			return ((ActionModel)element).getTargetEntityClassName();
		} else if (fieldName.equals(AnnotationConstants.KEYBOARD_KEY)) {
			return ((ActionModel)element).getKeyboardKeyName();
		}
		return Messages.getString("unknown.field");//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object element, Object value) {
		if (value instanceof IType) {
			IType res = (IType)value;
			ActionModel model = (ActionModel)element;
			try {
				if (res.isClass()) {
					if (fieldName.equals(AnnotationConstants.ACTION)) {
						model.setActionName(res.getElementName());
						TerminalAction action = TerminalActions.newAction(model.getActionName().toUpperCase());
						if (action != null) {
							model.setAction(action);
						} else {
							// Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName('.'));
							Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName());
							model.setAction((TerminalAction)clazz.newInstance());
						}
						((ActionModel)element).setActionName(((IType)value).getElementName());
					} else if (fieldName.equals(ScreenAnnotationConstants.TARGET_ENTITY)) {
						model.setTargetEntityClassName(res.getElementName());
						Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName());
						model.setTargetEntity(clazz);
					} else if (fieldName.equals(AnnotationConstants.KEYBOARD_KEY)) {
						model.setKeyboardKeyName(res.getElementName());
						TerminalAction action = TerminalActions.newAction(model.getKeyboardKeyName().toUpperCase());
						if (action != null && action instanceof SimpleTerminalMappedAction) {
							model.setKeyboardKey((Class<? extends SimpleTerminalMappedAction>)action.getClass());
						} else {
							Class<?> clazz = Utils.getClazz(res.getFullyQualifiedName());
							model.setKeyboardKey((Class<? extends SimpleTerminalMappedAction>)clazz);
						}
					}
				}
			} catch (JavaModelException e) {
			} catch (MalformedURLException e) {
			} catch (CoreException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
		this.viewer.update(element, null);
	}

}
