package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaJoinColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaManyToOneModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaTableModel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;
import org.openlegacy.designtime.generators.AnnotationConstants;

import java.util.Map;
import java.util.Set;

/**
 * @author Ivan Bort
 * 
 */
public class ControlsUpdater {

	public static void updateJpaEntityDetailsControls(JpaEntityModel model, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes) {
		if (model == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(DbAnnotationConstants.NAME)) {
				text.setText(model.getName());
			}
			if (key.equals(DbAnnotationConstants.DISPLAY_NAME)) {
				text.setText(model.getDisplayName());
			}
		}

		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(DbAnnotationConstants.WINDOW)) {
				button.setSelection(model.isWindow());
			} else if (key.equals(DbAnnotationConstants.CHILD)) {
				button.setSelection(model.isChild());
			}
		}
	}

	public static void updateJpaTableDetailsControls(JpaTableModel model, Map<String, Text> mapTexts, TableViewer tableViewer) {
		if (model == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(DbAnnotationConstants.NAME)) {
				text.setText(model.getName());
			} else if (key.equals(DbAnnotationConstants.CATALOG)) {
				text.setText(model.getCatalog());
			} else if (key.equals(DbAnnotationConstants.SCHEMA)) {
				text.setText(model.getSchema());
			}
		}
		if (tableViewer != null) {
			tableViewer.setInput(model);
		}
	}

	public static void updateJpaFieldDetailsControls(JpaFieldModel model, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes, Map<String, Label> mapLabels) {
		if (model == null) {
			return;
		}

		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(Constants.FIELD_NAME)) {
				text.setText(model.getFieldName());
			} else if (key.equals(DbAnnotationConstants.NAME)) {
				text.setText(model.getName());
			} else if (key.equals(DbAnnotationConstants.COLUMN_DEFINITION)) {
				text.setText(model.getColumnDefinition());
			} else if (key.equals(DbAnnotationConstants.TABLE)) {
				text.setText(model.getTable());
			} else if (key.equals(DbAnnotationConstants.LENGTH)) {
				text.setText(String.valueOf(model.getLength()));
			} else if (key.equals(DbAnnotationConstants.PRECISION)) {
				text.setText(String.valueOf(model.getPrecision()));
			} else if (key.equals(DbAnnotationConstants.SCALE)) {
				text.setText(String.valueOf(model.getScale()));
			} else if (key.equals(DbAnnotationConstants.DISPLAY_NAME)) {
				text.setText(model.getDisplayName());
			} else if (key.equals(DbAnnotationConstants.SAMPLE_VALUE)) {
				text.setText(model.getSampleValue());
			} else if (key.equals(DbAnnotationConstants.DEFAULT_VALUE)) {
				text.setText(model.getDefaultValue());
			} else if (key.equals(DbAnnotationConstants.HELP_TEXT)) {
				text.setText(model.getHelpText());
			}
		}
		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(DbAnnotationConstants.UNIQUE)) {
				button.setSelection(model.isUnique());
			} else if (key.equals(DbAnnotationConstants.NULLABLE)) {
				button.setSelection(model.isNullable());
			} else if (key.equals(DbAnnotationConstants.INSERTABLE)) {
				button.setSelection(model.isInsertable());
			} else if (key.equals(DbAnnotationConstants.UPDATABLE)) {
				button.setSelection(model.isUpdatable());
			} else if (key.equals(DbAnnotationConstants.DB_ID_ANNOTATION)) {
				button.setSelection(model.isKey());
				button.notifyListeners(SWT.Selection, new Event());
			} else if (key.equals(DbAnnotationConstants.DB_GENERATED_VALUE_ANNOTATION)) {
				button.setSelection(model.isGeneratedValue());
			} else if (key.equals(DbAnnotationConstants.PASSWORD)) {
				button.setSelection(model.isPassword());
			} else if (key.equals(DbAnnotationConstants.RIGHT_TO_LEFT)) {
				button.setSelection(model.isRightToLeft());
			} else if (key.equals(DbAnnotationConstants.INTERNAL)) {
				button.setSelection(model.isInternal());
			} else if (key.equals(DbAnnotationConstants.MAIN_DISPLAY_FIELD)) {
				button.setSelection(model.isMainDisplayFiled());
			}
		}
		// update Label controls
		mapKeys = mapLabels.keySet();
		for (String key : mapKeys) {
			if (key.equals(Constants.JAVA_TYPE_NAME)) {
				mapLabels.get(key).setText(model.getJavaTypeName());
			}
		}
	}

	public static void updateJpaListFieldDetailsControls(JpaListFieldModel model, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes, Map<String, CCombo> mapCombos) {
		if (model == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			if (key.equals(DbAnnotationConstants.MAPPED_BY)) {
				mapTexts.get(key).setText(model.getMappedBy());
			} else if (key.equals(DbAnnotationConstants.TARGET_ENTITY)) {
				mapTexts.get(key).setText(model.getTargetEntityClassName());
			} else if (key.equals(Constants.LIST_TYPE_ARG)) {
				mapTexts.get(key).setText(
						!StringUtils.equals(model.getFieldTypeArgs(), Object.class.getSimpleName()) ? model.getFieldTypeArgs()
								: "");
			}
		}
		// update check boxes
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(DbAnnotationConstants.ORPHAN_REMOVAL)) {
				mapCheckBoxes.get(key).setSelection(model.isOrphanRemoval());
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			if (key.equals(DbAnnotationConstants.CASCADE)) {
				mapCombos.get(key).setText(StringUtils.join(model.getCascade(), ","));
			} else if (key.equals(DbAnnotationConstants.FETCH)) {
				mapCombos.get(key).setText(model.getFetch().toString());
			}
		}
	}

	public static void updateJpaNavigationDetailsControls(JpaNavigationModel model, Map<String, Text> mapTexts) {
		if (model == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			if (key.equals(DbAnnotationConstants.CATEGORY)) {
				mapTexts.get(key).setText(model.getCategory());
			}
		}
	}

	public static void updateJpaManyToOneDetailsControls(JpaManyToOneModel model, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes, Map<String, CCombo> mapCombos) {
		if (model == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(DbAnnotationConstants.TARGET_ENTITY)) {
				text.setText(model.getTargetEntityClassName());
			} else if (key.equals(Constants.JAVA_TYPE)) {
				String javaTypeName = ((JpaFieldModel) model.getParent()).getJavaTypeName();
				text.setText(!StringUtils.equals(javaTypeName, void.class.getSimpleName()) ? javaTypeName : "");
			}
		}
		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(DbAnnotationConstants.OPTIONAL)) {
				button.setSelection(model.isOptional());
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			if (key.equals(DbAnnotationConstants.CASCADE)) {
				mapCombos.get(key).setText(StringUtils.join(model.getCascade(), ","));
			} else if (key.equals(DbAnnotationConstants.FETCH)) {
				mapCombos.get(key).setText(model.getFetch().toString());
			}
		}
	}

	public static void updateJpaJoinColumnDetailsControls(JpaJoinColumnModel model, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes) {
		if (model == null) {
			return;
		}
		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(Constants.JC_NAME)) {
				text.setText(model.getName());
			} else if (key.equals(Constants.JC_REFERENCED_COLUMN_NAME)) {
				text.setText(model.getReferencedColumnName());
			} else if (key.equals(Constants.JC_COLUMN_DEFINITION)) {
				text.setText(model.getColumnDefinition());
			} else if (key.equals(Constants.JC_TABLE)) {
				text.setText(model.getTable());
			}
		}
		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(Constants.JC_UNIQUE)) {
				button.setSelection(model.isUnique());
			} else if (key.equals(Constants.JC_NULLABLE)) {
				button.setSelection(model.isNullable());
			} else if (key.equals(Constants.JC_INSERTABLE)) {
				button.setSelection(model.isInsertable());
			} else if (key.equals(Constants.JC_UPDATABLE)) {
				button.setSelection(model.isUpdatable());
			}
		}
	}

	public static void updateJpaActionDetailsControl(ActionModel model, Map<String, Text> mapTexts,
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
			} else if (key.equals(AnnotationConstants.TARGET_ENTITY)) {
				text.setText(model.getTargetEntityClassName());
			}
		}

		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(AnnotationConstants.GLOBAL)) {
				mapCheckBoxes.get(key).setSelection(model.isGlobal());
			}
		}

		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			CCombo combo = mapCombos.get(key);
			if (key.equals(AnnotationConstants.ACTION)) {
				combo.setText(model.getActionName());
			}
		}
	}
}
