package com.openlegacy.enterprise.ide.eclipse.editors.utils.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.ActionsComparator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.IEntityActionsSorter;

import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScreenEntityActionsSorter implements IEntityActionsSorter {

	public static final ScreenEntityActionsSorter INSTANCE = new ScreenEntityActionsSorter();

	private ScreenEntityActionsSorter() {}

	private static final List<String> SCREEN_ENTITY_ORDER = Arrays.asList(ScreenAnnotationConstants.WINDOW,
			ScreenAnnotationConstants.ROWS, ScreenAnnotationConstants.COLUMNS, ScreenAnnotationConstants.SCREEN_TYPE,
			ScreenAnnotationConstants.CHILD, AnnotationConstants.DISPLAY_NAME, ScreenAnnotationConstants.VALIDATE_KEYS,
			ScreenAnnotationConstants.RIGHT_TO_LEFT, ScreenAnnotationConstants.ROLES);

	private static final List<String> SCREEN_FIELD_ORDER = Arrays.asList(AnnotationConstants.KEY, ScreenAnnotationConstants.ROW,
			ScreenAnnotationConstants.COLUMN, ScreenAnnotationConstants.END_COLUMN, ScreenAnnotationConstants.LABEL_COLUMN,
			AnnotationConstants.PASSWORD, AnnotationConstants.EDITABLE, ScreenAnnotationConstants.END_ROW,
			ScreenAnnotationConstants.RECTANGLE, AnnotationConstants.FIELD_TYPE, AnnotationConstants.DISPLAY_NAME,
			AnnotationConstants.SAMPLE_VALUE, AnnotationConstants.HELP_TEXT, AnnotationConstants.RIGHT_TO_LEFT,
			AnnotationConstants.ATTRIBUTE, ScreenAnnotationConstants.WHEN, ScreenAnnotationConstants.UNLESS,
			ScreenAnnotationConstants.KEY_INDEX, ScreenAnnotationConstants.INTERNAL, ScreenAnnotationConstants.GLOBAL,
			ScreenAnnotationConstants.NULL_VALUE, ScreenAnnotationConstants.TABLE_KEY, ScreenAnnotationConstants.FORCE_UPDATE,
			ScreenAnnotationConstants.EXPRESSION);

	private static final List<String> SCREEN_COLUMN_ORDER = Arrays.asList(ScreenAnnotationConstants.START_COLUMN,
			ScreenAnnotationConstants.END_COLUMN, AnnotationConstants.EDITABLE, AnnotationConstants.KEY,
			AnnotationConstants.MAIN_DISPLAY_FIELD, ScreenAnnotationConstants.SELECTION_FIELD, AnnotationConstants.DISPLAY_NAME,
			AnnotationConstants.SAMPLE_VALUE, ScreenAnnotationConstants.ROWS_OFFSET, AnnotationConstants.HELP_TEXT,
			ScreenAnnotationConstants.COL_SPAN, ScreenAnnotationConstants.SORT_INDEX, ScreenAnnotationConstants.ATTRIBUTE,
			ScreenAnnotationConstants.TARGET_ENTITY, ScreenAnnotationConstants.EXPRESSION);

	public List<AbstractAction> sort(List<AbstractAction> actions) {
		if ((actions == null) || actions.isEmpty()) {
			return actions;
		}
		AbstractAction abstractAction = actions.get(0);
		if (abstractAction instanceof ScreenEntityAction) {
			Collections.sort(actions, ActionsComparator.getInstance(SCREEN_ENTITY_ORDER));
		} else if (abstractAction instanceof ScreenFieldAction) {
			Collections.sort(actions, ActionsComparator.getInstance(SCREEN_FIELD_ORDER));
		} else if (abstractAction instanceof ScreenColumnAction) {
			Collections.sort(actions, ActionsComparator.getInstance(SCREEN_COLUMN_ORDER));
		}
		return actions;
	}

	public int getOrderIndex(AbstractAction action, int defaultIndex) {
		if (action instanceof ScreenEntityAction) {
			return SCREEN_ENTITY_ORDER.contains(action.getKey()) ? SCREEN_ENTITY_ORDER.indexOf(action.getKey()) : defaultIndex;
		} else if (action instanceof ScreenFieldAction) {
			return SCREEN_FIELD_ORDER.contains(action.getKey()) ? SCREEN_FIELD_ORDER.indexOf(action.getKey()) : defaultIndex;
		} else if (action instanceof ScreenColumnAction) {
			return SCREEN_COLUMN_ORDER.contains(action.getKey()) ? SCREEN_COLUMN_ORDER.indexOf(action.getKey()) : defaultIndex;
		}
		return defaultIndex;
	}
}
