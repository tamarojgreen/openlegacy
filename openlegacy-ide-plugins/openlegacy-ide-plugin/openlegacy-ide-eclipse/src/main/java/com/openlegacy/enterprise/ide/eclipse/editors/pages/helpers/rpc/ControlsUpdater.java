package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

import java.util.Map;
import java.util.Set;

/**
 * @author Ivan Bort
 * 
 */
public class ControlsUpdater {

	public static void updateRpcEntityDetailsControls(RpcEntityModel entityModel, Map<String, Text> mapTexts,
			Map<String, CCombo> mapCombos) {
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
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			if (key.equals(RpcAnnotationConstants.LANGUAGE)) {
				mapCombos.get(key).setText(entityModel.getLanguage().toString());
			}
		}
	}

	public static void updateRpcNavigationDetailsControls(RpcNavigationModel model, Map<String, Text> mapTexts) {
		if (model == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(RpcAnnotationConstants.CATEGORY)) {
				text.setText(model.getCategory());
			}
		}
	}

	public static void updateRpcFieldDetailsControls(RpcFieldModel fieldModel, Map<String, Text> mapTexts,
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
			} else if (key.equals(RpcAnnotationConstants.ORIGINAL_NAME)) {
				text.setText(fieldModel.getOriginalName());
			} else if (key.equals(AnnotationConstants.LENGTH)) {
				text.setText(((fieldModel.getLength() != null) && (fieldModel.getLength() != 0)) ? Integer.toString(fieldModel.getLength())
						: "");//$NON-NLS-1$
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				text.setText(fieldModel.getDisplayName());
			} else if (key.equals(AnnotationConstants.SAMPLE_VALUE)) {
				text.setText(fieldModel.getSampleValue());
			} else if (key.equals(AnnotationConstants.DEFAULT_VALUE)) {
				text.setText(fieldModel.getDefaultValue());
			} else if (key.equals(AnnotationConstants.HELP_TEXT)) {
				text.setText(fieldModel.getHelpText());
			} else if (key.equals(RpcAnnotationConstants.DEFAULT_VALUE)) {
				text.setText(fieldModel.getDefaultValue());
			} else if (key.equals(RpcAnnotationConstants.EXPRESSION)) {
				text.setText(fieldModel.getExpression());
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			if (key.equals(AnnotationConstants.FIELD_TYPE)) {
				mapCombos.get(key).setText(fieldModel.getFieldTypeName());
			} else if (key.equals(RpcAnnotationConstants.DIRECTION)) {
				mapCombos.get(key).setText(fieldModel.getDirection().toString());
			}
		}
		// update check boxes
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(AnnotationConstants.EDITABLE)) {
				button.setSelection(fieldModel.isEditable());
			} else if (key.equals(AnnotationConstants.KEY)) {
				button.setSelection(fieldModel.isKey());
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

	public static void updateRpcBooleanFieldDetailsControls(RpcBooleanFieldModel fieldModel, Map<String, Text> mapTexts,
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

	public static void updateRpcIntegerFieldDetailsControls(RpcIntegerFieldModel fieldModel, Map<String, Text> mapTexts) {

		if (fieldModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(RpcAnnotationConstants.MINIMUM_VALUE)) {
				text.setText((fieldModel.getMinimumValue() != 0) ? String.valueOf(fieldModel.getMinimumValue()) : "");//$NON-NLS-1$
			} else if (key.equals(RpcAnnotationConstants.MAXIMUM_VALUE)) {
				text.setText((fieldModel.getMaximumValue() != 0) ? String.valueOf(fieldModel.getMaximumValue()) : "");//$NON-NLS-1$
			} else if (key.equals(RpcAnnotationConstants.DECIMAL_PLACES)) {
				text.setText((fieldModel.getDecimalPlaces() != 0) ? String.valueOf(fieldModel.getDecimalPlaces()) : "");//$NON-NLS-1$
			}
		}
	}

	public static void updateRpcPartDetailsControls(RpcPartModel partModel, Map<String, Text> mapTexts) {
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
			} else if (key.equals(Constants.JAVA_TYPE_NAME)) {
				text.setText(partModel.getClassName());
			} else if (key.equals(RpcAnnotationConstants.COUNT)) {
				text.setText((partModel.getCount() != 0) ? String.valueOf(partModel.getCount()) : "");//$NON-NLS-1$
			}
		}
	}

	public static void updateRpcDateFieldDetailsControls(RpcDateFieldModel fieldModel, Map<String, Text> mapTexts) {
		if (fieldModel == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(AnnotationConstants.PATTERN)) {
				text.setText(fieldModel.getPattern());
			}
		}
	}

	public static void updateRpcEnumFieldDetailsControls(RpcEnumFieldModel fieldModel, Map<String, Text> mapTexts,
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

}
