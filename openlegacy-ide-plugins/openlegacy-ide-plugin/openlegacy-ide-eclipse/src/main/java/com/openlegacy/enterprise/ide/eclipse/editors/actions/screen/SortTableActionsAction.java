package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;

import org.eclipse.jdt.core.dom.ASTNode;
import org.openlegacy.annotations.screen.ScreenTableActions;

import java.util.UUID;

/**
 * @author Sergey Bezkostnyi
 *
 */
public class SortTableActionsAction extends AbstractAction {

	public static final UUID ACTION_UUID = UUID.randomUUID();

	public SortTableActionsAction(ScreenTableModel tableModel) {
		super(ACTION_UUID, tableModel, ActionType.MODIFY, ASTNode.NORMAL_ANNOTATION, null, null);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ScreenTableActions.class;
	}

}
