package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDescriptionFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDynamicFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.utils.StringUtil;

import java.util.Map;
import java.util.Set;

/**
 * @author Ivan Bort
 * 
 */
public class ControlsUpdater {

	public static void updateScreenBooleanFieldDetailsControls(ScreenBooleanFieldModel fieldModel, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes) {

		if (fieldModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(AnnotationConstants.TRUE_VALUE)) {
				text.setText(fieldModel.getTrueValue() == null ? "" : fieldModel.getTrueValue());//$NON-NLS-1$
			} else if (key.equals(AnnotationConstants.FALSE_VALUE)) {
				text.setText(fieldModel.getFalseValue() == null ? "" : fieldModel.getFalseValue());//$NON-NLS-1$
			}
		}
		// update check boxes
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(AnnotationConstants.TREAT_EMPTY_AS_NULL)) {
				mapCheckBoxes.get(key).setSelection(fieldModel.isTreatEmptyAsNull());
			}
		}
	}

	/**
	 * @param columnModel
	 * @param mapTexts
	 * @param mapCheckBoxes
	 * @param mapLabels
	 */
	public static void updateScreenColumnDetailsControls(ScreenColumnModel columnModel, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes, Map<String, Label> mapLabels, Map<String, CCombo> mapCombos) {

		if (columnModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(Constants.FIELD_NAME)) {
				text.setText(columnModel.getFieldName());
			} else if (key.equals(ScreenAnnotationConstants.START_COLUMN)) {
				text.setText(Integer.toString(columnModel.getStartColumn()));
			} else if (key.equals(ScreenAnnotationConstants.END_COLUMN)) {
				text.setText(Integer.toString(columnModel.getEndColumn()));
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				text.setText(columnModel.getDisplayName());
			} else if (key.equals(AnnotationConstants.SAMPLE_VALUE)) {
				text.setText(columnModel.getSampleValue());
			} else if (key.equals(ScreenAnnotationConstants.ROWS_OFFSET)) {
				text.setText(Integer.toString(columnModel.getRowsOffset()));
			} else if (key.equals(AnnotationConstants.HELP_TEXT)) {
				text.setText(columnModel.getHelpText());
			} else if (key.equals(ScreenAnnotationConstants.COL_SPAN)) {
				text.setText(Integer.toString(columnModel.getColSpan()));
			} else if (key.equals(ScreenAnnotationConstants.SORT_INDEX)) {
				text.setText(Integer.toString(columnModel.getSortIndex()));
			} else if (key.equals(AnnotationConstants.TARGET_ENTITY)) {
				text.setText(columnModel.getTargetEntityClassName());
			} else if (key.equals(ScreenAnnotationConstants.EXPRESSION)) {
				text.setText(columnModel.getExpression());
			}
		}
		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(AnnotationConstants.KEY)) {
				button.setSelection(columnModel.isKey());
			} else if (key.equals(ScreenAnnotationConstants.SELECTION_FIELD)) {
				button.setSelection(columnModel.isSelectionField());
			} else if (key.equals(AnnotationConstants.MAIN_DISPLAY_FIELD)) {
				button.setSelection(columnModel.isMainDisplayField());
			} else if (key.equals(AnnotationConstants.EDITABLE)) {
				button.setSelection(columnModel.isEditable());
			}
		}
		// update Label controls
		mapKeys = mapLabels.keySet();
		for (String key : mapKeys) {
			if (key.equals(Constants.JAVA_TYPE_NAME)) {
				mapLabels.get(key).setText(columnModel.getJavaTypeName());
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			if (key.equals(AnnotationConstants.ATTRIBUTE)) {
				mapCombos.get(key).setText(columnModel.getAttribute().toString());
			}
		}
	}

	/**
	 * @param fieldModel
	 * @param mapTexts
	 * @param mapCheckBoxes
	 */
	public static void updateScreenDateFieldDetailsControls(ScreenDateFieldModel fieldModel, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes) {

		if (fieldModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(AnnotationConstants.YEAR_COLUMN)) {
				text.setText(fieldModel.getYear() != null ? fieldModel.getYear().toString() : "");
			} else if (key.equals(AnnotationConstants.MONTH_COLUMN)) {
				text.setText(fieldModel.getMonth() != null ? fieldModel.getMonth().toString() : "");
			} else if (key.equals(AnnotationConstants.DAY_COLUMN)) {
				text.setText(fieldModel.getDay() != null ? fieldModel.getDay().toString() : "");
			} else if (key.equals(AnnotationConstants.PATTERN)) {
				text.setText(fieldModel.getPattern());
			}
		}
	}

	public static void updateScreenEntityDetailsControls(ScreenEntityModel entityModel, Map<String, Text> mapTexts,
			Map<String, CCombo> mapCombos, Map<String, Button> mapCheckBoxes, TableViewer tableViewer) {

		if (entityModel == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(AnnotationConstants.NAME)) {
				text.setText(entityModel.getName());
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				text.setText(entityModel.getDisplayName());
			} else if (key.equals(ScreenAnnotationConstants.COLUMNS)) {
				text.setText(new Integer(entityModel.getColumns()).toString());
			} else if (key.equals(ScreenAnnotationConstants.ROWS)) {
				text.setText(new Integer(entityModel.getRows()).toString());
			}
		}
		// update check boxes
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA)) {
				button.setSelection(entityModel.isSupportTerminalData());
			} else if (key.equals(ScreenAnnotationConstants.WINDOW)) {
				button.setSelection(entityModel.isWindow());
			} else if (key.equals(ScreenAnnotationConstants.CHILD)) {
				button.setSelection(entityModel.isChild());
			} else if (key.equals(ScreenAnnotationConstants.VALIDATE_KEYS)) {
				button.setSelection(entityModel.isValidateKeys());
			} else if (key.equals(ScreenAnnotationConstants.RIGHT_TO_LEFT)) {
				button.setSelection(entityModel.isRightToLeft());
			} else if (key.equals(ScreenAnnotationConstants.AUTO_MAP_KEYBOARD_ACTIONS)) {
				button.setSelection(entityModel.isAutoMapKeyboardActions());
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			if (key.equals(ScreenAnnotationConstants.SCREEN_TYPE)) {
				mapCombos.get(key).setText(entityModel.getScreenType().getSimpleName());
			}
		}
		if (tableViewer != null) {
			tableViewer.setInput(entityModel);
		}
	}

	public static void updateScreenEnumFieldDetailsControls(ScreenEnumFieldModel fieldModel, Map<String, Text> mapTexts,
			TableViewer tableViewer) {
		if (fieldModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			if (key.equals(Constants.JAVA_TYPE)) {
				mapTexts.get(key).setText(fieldModel.getPrevJavaTypeName().isEmpty() ? "" : fieldModel.getJavaTypeName());
			}
		}
		if (tableViewer != null) {
			tableViewer.setInput(fieldModel);
		}
	}

	public static void updateScreenFieldDetailsControls(ScreenFieldModel fieldModel, Map<String, Text> mapTexts,
			Map<String, CCombo> mapCombos, Map<String, Button> mapCheckBoxes, Map<String, Label> mapLabels) {

		if (fieldModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(Constants.FIELD_NAME)) {
				text.setText(fieldModel.getFieldName());
			} else if (key.equals(ScreenAnnotationConstants.ROW)) {
				text.setText(new Integer(fieldModel.getRow()).toString());
			} else if (key.equals(ScreenAnnotationConstants.COLUMN)) {
				text.setText(new Integer(fieldModel.getColumn()).toString());
			} else if (key.equals(ScreenAnnotationConstants.END_COLUMN)) {
				text.setText(((fieldModel.getEndColumn() != null) && (fieldModel.getEndColumn() != 0)) ? fieldModel
						.getEndColumn().toString() : "");
			} else if (key.equals(ScreenAnnotationConstants.END_ROW)) {
				text.setText(((fieldModel.getEndRow() != null) && (fieldModel.getEndRow() != 0)) ? fieldModel.getEndRow()
						.toString() : "");
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				text.setText(fieldModel.getDisplayName());
			} else if (key.equals(AnnotationConstants.SAMPLE_VALUE)) {
				text.setText(fieldModel.getSampleValue());
			} else if (key.equals(AnnotationConstants.DEFAULT_VALUE)) {
				text.setText(fieldModel.getDefaultValue());
			} else if (key.equals(ScreenAnnotationConstants.LABEL_COLUMN)) {
				text.setText(new Integer(fieldModel.getLabelColumn()).toString());
			} else if (key.equals(AnnotationConstants.HELP_TEXT)) {
				text.setText(fieldModel.getHelpText());
			} else if (key.equals(ScreenAnnotationConstants.WHEN)) {
				text.setText(fieldModel.getWhen());
			} else if (key.equals(ScreenAnnotationConstants.UNLESS)) {
				text.setText(fieldModel.getUnless());
			} else if (key.equals(ScreenAnnotationConstants.KEY_INDEX)) {
				text.setText(String.valueOf(fieldModel.getKeyIndex()));
			} else if (key.equals(ScreenAnnotationConstants.NULL_VALUE)) {
				text.setText(fieldModel.getNullValue());
			} else if (key.equals(ScreenAnnotationConstants.EXPRESSION)) {
				text.setText(fieldModel.getExpression());
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			if (key.equals(AnnotationConstants.FIELD_TYPE)) {
				mapCombos.get(key).setText(fieldModel.getFieldTypeName());
			} else if (key.equals(AnnotationConstants.ATTRIBUTE)) {
				mapCombos.get(key).setText(fieldModel.getAttribute().toString());
			}
		}
		// update check boxes
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(ScreenAnnotationConstants.RECTANGLE)) {
				button.setSelection(fieldModel.isRectangle());
			} else if (key.equals(AnnotationConstants.EDITABLE)) {
				button.setSelection(fieldModel.isEditable());
			} else if (key.equals(AnnotationConstants.PASSWORD)) {
				button.setSelection(fieldModel.isPassword());
			} else if (key.equals(AnnotationConstants.KEY)) {
				button.setSelection(fieldModel.isKey());
			} else if (key.equals(AnnotationConstants.RIGHT_TO_LEFT)) {
				button.setSelection(fieldModel.isRightToLeft());
			} else if (key.equals(ScreenAnnotationConstants.INTERNAL)) {
				button.setSelection(fieldModel.isInternal());
			} else if (key.equals(ScreenAnnotationConstants.GLOBAL)) {
				button.setSelection(fieldModel.isGlobal());
			} else if (key.equals(ScreenAnnotationConstants.TABLE_KEY)) {
				button.setSelection(fieldModel.isTableKey());
			} else if (key.equals(ScreenAnnotationConstants.FORCE_UPDATE)) {
				button.setSelection(fieldModel.isForceUpdate());
			} else if (key.equals(ScreenAnnotationConstants.ENABLE_LOOKUP)) {
				button.setSelection(fieldModel.isEnableLookup());
			}
		}
		// update labels
		mapKeys = mapLabels.keySet();
		for (String key : mapKeys) {
			if (key.equals(Constants.JAVA_TYPE_NAME)) {
				mapLabels.get(key).setText(fieldModel.getJavaTypeName());
			}
		}
	}

	public static void updateScreenDescriptionFieldDetailsControls(ScreenDescriptionFieldModel fieldModel,
			Map<String, Text> mapTexts) {
		if (fieldModel == null) {
			return;
		}
		// update Text controls
		mapTexts.get(Constants.DESC_ROW).setText("");//$NON-NLS-1$
		mapTexts.get(Constants.DESC_END_COLUMN).setText("");//$NON-NLS-1$
		// first of all we need to update column control, because row and
		// endColumn controls are depends on it
		mapTexts.get(Constants.DESC_COLUMN).setText(
				(fieldModel.getColumn() != null && fieldModel.getColumn() != 0) ? fieldModel.getColumn().toString() : "");//$NON-NLS-1$
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(Constants.DESC_ROW)) {
				text.setText((fieldModel.getRow() != null && fieldModel.getRow() != 0) ? fieldModel.getRow().toString() : "");//$NON-NLS-1$
			} else if (key.equals(Constants.DESC_END_COLUMN)) {
				text.setText((fieldModel.getEndColumn() != null & fieldModel.getEndColumn() != 0) ? fieldModel.getEndColumn()
						.toString() : "");//$NON-NLS-1$
			}
		}
	}

	public static void updateScreenDynamicFieldDetailsControls(ScreenDynamicFieldModel fieldModel, Map<String, Text> mapTexts) {
		if (fieldModel == null) {
			return;
		}
		// update Text controls
		mapTexts.get(Constants.DYNAMIC_ROW).setText("");//$NON-NLS-1$
		mapTexts.get(Constants.DYNAMIC_COLUMN).setText("");//$NON-NLS-1$
		mapTexts.get(Constants.DYNAMIC_END_ROW).setText("");//$NON-NLS-1$
		mapTexts.get(Constants.DYNAMIC_END_COLUMN).setText("");//$NON-NLS-1$
		mapTexts.get(Constants.DYNAMIC_OFFSET).setText("");//$NON-NLS-1$
		mapTexts.get(Constants.DYNAMIC_TEXT).setText("");//$NON-NLS-1$

		// first of all we need to update column control, because row and
		// endColumn controls are depends on it
		mapTexts.get(Constants.DYNAMIC_TEXT).setText(
				(!StringUtil.isEmpty(fieldModel.getText())) ? fieldModel.getText().toString() : "");//$NON-NLS-1$
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(Constants.DYNAMIC_ROW)) {
				text.setText((fieldModel.getRow() != null && fieldModel.getRow() != 0) ? fieldModel.getRow().toString() : "");//$NON-NLS-1$
			} else if (key.equals(Constants.DYNAMIC_COLUMN)) {
				text.setText((fieldModel.getColumn() != null & fieldModel.getColumn() != 0) ? fieldModel.getColumn().toString()
						: "");//$NON-NLS-1$
			} else if (key.equals(Constants.DYNAMIC_END_ROW)) {
				text.setText((fieldModel.getEndRow() != null & fieldModel.getEndRow() != 0) ? fieldModel.getEndRow().toString()
						: "");//$NON-NLS-1$
			} else if (key.equals(Constants.DYNAMIC_END_COLUMN)) {
				text.setText((fieldModel.getEndColumn() != null & fieldModel.getEndColumn() != 0) ? fieldModel.getEndColumn()
						.toString() : "");//$NON-NLS-1$
			} else if (key.equals(Constants.DYNAMIC_OFFSET)) {
				text.setText((fieldModel.getFieldOffset() != null & fieldModel.getFieldOffset() != 0) ? fieldModel
						.getFieldOffset().toString() : "");//$NON-NLS-1$
			}
		}
	}

	/**
	 * @param fieldModel
	 * @param mapTexts
	 * @param mapCheckBoxes
	 */
	public static void updateScreenFieldValuesDetailsControls(ScreenFieldValuesModel fieldModel, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes, Map<String, CCombo> mapCombos) {
		if (fieldModel == null) {
			return;
		}
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(ScreenAnnotationConstants.SOURCE_SCREEN_ENTITY)) {
				text.setText(fieldModel.getSourceScreenEntityName());
			} else if (key.equals(AnnotationConstants.PROVIDER)) {
				text.setText(fieldModel.getProvider().getSimpleName());
			} else if (key.equals(ScreenAnnotationConstants.DISPLAY_FIELD_NAME)) {
				text.setText(fieldModel.getDisplayFieldName());
			} else if (key.equals(ScreenAnnotationConstants.SEARCH_FIELD)) {
				text.setText(fieldModel.getSearchField());
			}
		}

		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(ScreenAnnotationConstants.COLLECT_ALL)) {
				mapCheckBoxes.get(key).setSelection(fieldModel.isCollectAll());
			} else if (key.equals(ScreenAnnotationConstants.AS_WINDOW)) {
				mapCheckBoxes.get(key).setSelection(fieldModel.isAsWindow());
			}
		}

		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			CCombo combo = mapCombos.get(key);
			if (key.equals(ScreenAnnotationConstants.AUTO_SUBMIT_ACTION)) {
				combo.setText(fieldModel.getAutoSubmitActionName());
			}
		}
	}

	public static void updateScreenNavigationDetailsControls(ScreenNavigationModel navigationModel, Map<String, Text> mapTexts,
			Map<String, CCombo> mapCombos, Map<String, Button> mapCheckBoxes, TableViewer tableViewer) {

		if (navigationModel == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			if (key.equals(ScreenAnnotationConstants.ACCESSED_FROM)) {
				mapTexts.get(key).setText(navigationModel.getAccessedFromEntityName());
			} else if (key.equals(ScreenAnnotationConstants.DRILLDOWN_VALUE)) {
				mapTexts.get(key).setText(navigationModel.getDrilldownValue());
			}
		}
		// update check boxes
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(ScreenAnnotationConstants.REQUIRES_PARAMETERS)) {
				mapCheckBoxes.get(key).setSelection(navigationModel.isRequiresParameters());
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			CCombo combo = mapCombos.get(key);
			if (key.equals(ScreenAnnotationConstants.TERMINAL_ACTION)) {
				combo.setText(navigationModel.getTerminalActionName());
			} else if (key.equals(ScreenAnnotationConstants.ADDITIONAL_KEY)) {
				combo.setText(navigationModel.getAdditionalKey().name());
			} else if (key.equals(ScreenAnnotationConstants.EXIT_ACTION)) {
				combo.setText(navigationModel.getExitActionName());
			} else if (key.equals(ScreenAnnotationConstants.EXIT_ADDITIONAL_KEY)) {
				combo.setText(navigationModel.getExitAdditionalKey().name());
			}
		}
		if (tableViewer != null) {
			tableViewer.setInput(navigationModel);
		}
	}

	/**
	 * @param partModel
	 * @param mapTexts
	 * @param mapCheckBoxes
	 */
	public static void updateScreenPartDetailsControls(ScreenPartModel partModel, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes) {

		if (partModel == null) {
			return;
		}

		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(AnnotationConstants.NAME)) {
				text.setText(partModel.getName());
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				text.setText(partModel.getDisplayName());
			} else if (key.equals(ScreenAnnotationConstants.ROW)) {
				text.setText(Integer.toString(partModel.getPartPosition().getRow()));
			} else if (key.equals(ScreenAnnotationConstants.COLUMN)) {
				text.setText(Integer.toString(partModel.getPartPosition().getColumn()));
			} else if (key.equals(ScreenAnnotationConstants.WIDTH)) {
				text.setText(Integer.toString(partModel.getPartPosition().getWidth()));
			} else if (key.equals(Constants.JAVA_TYPE_NAME)) {
				text.setText(partModel.getClassName());
			}
		}
		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA)) {
				mapCheckBoxes.get(key).setSelection(partModel.isSupportTerminalData());
			}
		}
	}

	/**
	 * @param tableModel
	 * @param mapTexts
	 * @param mapCombos
	 * @param mapCheckBoxes
	 */
	public static void updateScreenTableDetailsControls(ScreenTableModel tableModel, Map<String, Text> mapTexts,
			Map<String, CCombo> mapCombos, Map<String, Button> mapCheckBoxes) {

		if (tableModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(AnnotationConstants.NAME)) {
				text.setText(tableModel.getName());
			} else if (key.equals(ScreenAnnotationConstants.TABLE_COLLECTOR)) {
				text.setText(tableModel.getTableCollectorName());
			} else if (key.equals(ScreenAnnotationConstants.ROW_GAPS)) {
				text.setText(Integer.toString(tableModel.getRowGaps()));
			} else if (key.equals(ScreenAnnotationConstants.END_ROW)) {
				text.setText(Integer.toString(tableModel.getEndRow()));
			} else if (key.equals(ScreenAnnotationConstants.START_ROW)) {
				text.setText(Integer.toString(tableModel.getStartRow()));
			} else if (key.equals(Constants.JAVA_TYPE_NAME)) {
				text.setText(tableModel.getClassName());
			} else if (key.equals(ScreenAnnotationConstants.SCREENS_COUNT)) {
				text.setText(Integer.toString(tableModel.getScreensCount()));
			} else if (key.equals(ScreenAnnotationConstants.FILTER_EXPRESSION)) {
				text.setText(tableModel.getFilterExpression());
			} else if (key.equals(ScreenAnnotationConstants.STOP_EXPRESSION)) {
				text.setText(tableModel.getStopExpression());
			}
		}
		// update Combo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			CCombo combo = mapCombos.get(key);
			if (key.equals(ScreenAnnotationConstants.NEXT_SCREEN_ACTION)) {
				combo.setText(tableModel.getNextScreenActionName());
			} else if (key.equals(ScreenAnnotationConstants.PREV_SCREEN_ACTION)) {
				combo.setText(tableModel.getPreviousScreenActionName());
			}
		}
		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA)) {
				button.setSelection(tableModel.isSupportTerminalData());
			} else if (key.equals(ScreenAnnotationConstants.SCROLLABLE)) {
				button.setSelection(tableModel.isScrollable());
			} else if (key.equals(ScreenAnnotationConstants.RIGHT_TO_LEFT)) {
				button.setSelection(tableModel.isRightToLeft());
			}
		}
	}

	/**
	 * @param actionModel
	 * @param mapTexts
	 * @param mapCheckBoxes
	 */
	public static void updateTableActionDetailsPage(TableActionModel actionModel, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes, Map<String, CCombo> mapCombos) {

		if (actionModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(AnnotationConstants.ACTION)) {
				text.setText(actionModel.getAction().getSimpleName());
			} else if (key.equals(AnnotationConstants.ACTION_VALUE)) {
				text.setText(actionModel.getActionValue());
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				text.setText(actionModel.getDisplayName());
			} else if (key.equals(AnnotationConstants.ALIAS)) {
				text.setText(actionModel.getAlias());
			} else if (key.equals(AnnotationConstants.TARGET_ENTITY)) {
				text.setText(actionModel.getTargetEntityName());
			} else if (key.equals(ScreenAnnotationConstants.ROW)) {
				text.setText(String.valueOf(actionModel.getRow()));
			} else if (key.equals(ScreenAnnotationConstants.COLUMN)) {
				text.setText(String.valueOf(actionModel.getColumn()));
			} else if (key.equals(ScreenAnnotationConstants.LENGTH)) {
				text.setText(String.valueOf(actionModel.getLength()));
			} else if (key.equals(ScreenAnnotationConstants.WHEN)) {
				text.setText(actionModel.getWhen());
			}
		}
		
		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(AnnotationConstants.DEFAULT_ACTION)) {
				mapCheckBoxes.get(key).setSelection(actionModel.isDefaultAction());
			}
		}
		
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			CCombo combo = mapCombos.get(key);
			if (key.equals(ScreenAnnotationConstants.ADDITIONAL_KEY)) {
				combo.setText(actionModel.getAdditionalKey().toString());
			}
		}
	}

	/**
	 * @param fieldModel
	 * @param mapTexts
	 * @param mapCheckBoxes
	 */
	public static void updateScreenIntegerFieldDetailsControls(ScreenIntegerFieldModel fieldModel, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes) {

		if (fieldModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(ScreenAnnotationConstants.NUMERIC_PATTERN)) {
				text.setText(fieldModel.getPattern());
			}
		}
	}

	/**
	 * @param model
	 * @param mapTexts
	 * @param mapCombos
	 * @param mapCheckBoxes
	 */
	public static void updateScreenActionDetailsControls(ActionModel model, Map<String, Text> mapTexts,
			Map<String, CCombo> mapCombos, Map<String, Button> mapCheckBoxes) {

		if (model == null) {
			return;
		}

		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				text.setText(model.getDisplayName());
			} else if (key.equals(AnnotationConstants.ALIAS)) {
				text.setText(model.getAlias());
			} else if (key.equals(ScreenAnnotationConstants.ROW)) {
				text.setText(String.valueOf(model.getRow()));
			} else if (key.equals(ScreenAnnotationConstants.COLUMN)) {
				text.setText(String.valueOf(model.getColumn()));
			} else if (key.equals(ScreenAnnotationConstants.LENGTH)) {
				text.setText(String.valueOf(model.getLength()));
			} else if (key.equals(AnnotationConstants.WHEN)) {
				text.setText(model.getWhen() == null ? "" : model.getWhen());
			} else if (key.equals(ScreenAnnotationConstants.FOCUS_FIELD)) {
				text.setText(model.getFocusField() == null ? "" : model.getFocusField());
			} else if (key.equals(AnnotationConstants.TARGET_ENTITY)) {
				text.setText(model.getTargetEntityClassName());
			} else if (key.equals(ScreenAnnotationConstants.SLEEP)) {
				text.setText(String.valueOf(model.getSleep()));
			}
		}

		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(ScreenAnnotationConstants.GLOBAL)) {
				mapCheckBoxes.get(key).setSelection(model.isGlobal());
			}
		}

		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			CCombo combo = mapCombos.get(key);
			if (key.equals(ScreenAnnotationConstants.ACTION)) {
				combo.setText(model.getActionName());
			} else if (key.equals(ScreenAnnotationConstants.ADDITIONAL_KEY)) {
				combo.setText(model.getAdditionalKey().toString());
			} else if (key.equals(ScreenAnnotationConstants.TYPE)) {
				combo.setText(model.getType().toString());
			} else if (key.equals(AnnotationConstants.KEYBOARD_KEY)) {
				combo.setText(model.getKeyboardKeyName());
			}
		}
	}
}
