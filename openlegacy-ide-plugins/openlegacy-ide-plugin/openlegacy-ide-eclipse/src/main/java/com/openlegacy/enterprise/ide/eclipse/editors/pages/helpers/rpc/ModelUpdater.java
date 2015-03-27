package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcEntityUtils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.openlegacy.FieldType;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.actions.RpcActions;

import java.net.MalformedURLException;

/**
 * @author Ivan Bort
 * 
 */
public class ModelUpdater {

	public static void updateRpcEntityModel(RpcEntity entity, RpcEntityModel model, String key, String text) {

		if (text != null) {
			if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(AnnotationConstants.NAME)) {
				model.setName(text);
			} else if (key.equals(RpcAnnotationConstants.LANGUAGE)) {
				model.setLanguage(Languages.valueOf(text.toUpperCase()));
			}
		}
		RpcEntityUtils.ActionGenerator.generateRpcEntityActions(entity, model);
	}

	public static void updateRpcNavigationModel(RpcEntity entity, RpcNavigationModel model, String key, String text) {
		if (text != null) {
			if (key.equals(RpcAnnotationConstants.CATEGORY)) {
				model.setCategory(text);
			}
		}
		RpcEntityUtils.ActionGenerator.generateRpcNavigationActions(entity, model);
	}

	@SuppressWarnings("unchecked")
	public static void updateRpcFieldModel(RpcEntity entity, RpcFieldModel model, String key, String text, Boolean selection,
			String fullyQualifiedName) throws MalformedURLException, CoreException {
		if (selection != null) {
			if (key.equals(AnnotationConstants.EDITABLE)) {
				model.setEditable(selection);
			} else if (key.equals(AnnotationConstants.KEY)) {
				model.setKey(selection);
			}
		}
		if (text != null) {
			if (key.equals(Constants.FIELD_NAME)) {
				model.setFieldName(StringUtils.uncapitalize(text));
			} else if (key.equals(RpcAnnotationConstants.ORIGINAL_NAME)) {
				model.setOriginalName(text);
			} else if (key.equals(RpcAnnotationConstants.DIRECTION)) {
				model.setDirection(Direction.valueOf(text.toUpperCase()));
			} else if (key.equals(AnnotationConstants.LENGTH)) {
				model.setLength(text.isEmpty() ? null : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.FIELD_TYPE)) {
				if (RpcFieldModel.mapFieldTypes.get(text) != null) {
					model.setFieldType(RpcFieldModel.mapFieldTypes.get(text));
					model.setFieldTypeName(RpcFieldModel.mapFieldTypes.get(text).getSimpleName());
				} else if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setFieldType((Class<? extends FieldType>) clazz);
					model.setFieldTypeName(clazz.getSimpleName());
				}
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(AnnotationConstants.SAMPLE_VALUE)) {
				model.setSampleValue(text);
			} else if (key.equals(AnnotationConstants.DEFAULT_ACTION)) {
				model.setDefaultValue(text);
			} else if (key.equals(AnnotationConstants.HELP_TEXT)) {
				model.setHelpText(text);
			} else if (key.equals(RpcAnnotationConstants.DEFAULT_VALUE)) {
				model.setDefaultValue(text);
			} else if (key.equals(RpcAnnotationConstants.EXPRESSION)) {
				model.setExpression(text);
			}
		}
		RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, model, true);
	}

	public static void updateRpcBooleanFieldModel(RpcEntity entity, RpcBooleanFieldModel model, String key, String text,
			Boolean selection) {
		if (selection != null) {
			if (key.equals(AnnotationConstants.TREAT_EMPTY_AS_NULL)) {
				model.setTreatEmptyAsNull(selection);
			}
		}
		if (text != null) {
			if (key.equals(AnnotationConstants.TRUE_VALUE)) {
				model.setTrueValue(text);
			} else if (key.equals(AnnotationConstants.FALSE_VALUE)) {
				model.setFalseValue(text);
			}
		}
		RpcEntityUtils.ActionGenerator.generateRpcBooleanFieldActions(entity, model, true);
	}

	public static void updateRpcIntegerFieldModel(RpcEntity entity, RpcIntegerFieldModel model, String key, String text) {
		if (text != null) {
			if (key.equals(RpcAnnotationConstants.MINIMUM_VALUE)) {
				model.setMinimumValue((text.isEmpty() || text.equals("-")) ? 0.0 : Double.valueOf(text));
			} else if (key.equals(RpcAnnotationConstants.MAXIMUM_VALUE)) {
				model.setMaximumValue((text.isEmpty() || text.equals("-")) ? 0.0 : Double.valueOf(text));
			} else if (key.equals(RpcAnnotationConstants.DECIMAL_PLACES)) {
				model.setDecimalPlaces(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(RpcAnnotationConstants.NUMERIC_PATTERN)) {
				model.setPattern(text.isEmpty() ? "#" : text.replaceAll("^[\\.,]|[\\.,]$", "").trim());
			}
		}
		RpcEntityUtils.ActionGenerator.generateRpcNumericFieldActions(entity, model, true);
	}

	public static void updateRpcPartModel(RpcEntity entity, RpcPartModel model, String key, String text) {
		if (text != null) {
			if (key.equals(Constants.JAVA_TYPE_NAME)) {
				model.setClassName(StringUtils.capitalize(text.replaceAll("\\s+", "")));
				// in accordance to RpcCodeBasedDefinitionUtils->getEntityDefinition(...);
				// can be removed later when user will have ability to change "name" attribute
				model.setName(model.getClassName());
			} else if (key.equals(AnnotationConstants.NAME)) {
				model.setName(text);
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(RpcAnnotationConstants.COUNT)) {
				model.setCount(text.isEmpty() ? 1 : Integer.valueOf(text).intValue());
			}
		}
		RpcEntityUtils.ActionGenerator.generateRpcPartAction(entity, model);
		RpcEntityUtils.ActionGenerator.generateRpcPartListAction(entity, model);
		if (model.getCount() > 1) {
			model.restorePartActions();
		} else {
			model.resetPartActions();
		}
		RpcEntityUtils.ActionGenerator.generateRpcPartActionsAction(entity, model.getActionsModel());
	}

	public static void updateRpcActionsModel(RpcEntity entity, RpcActionsModel actionsModel) {
		RpcEntityUtils.ActionGenerator.generateRpcActionsAction(entity, actionsModel);
	}

	public static void updateRpcPartActionsModel(RpcEntity entity, RpcActionsModel actionsModel) {
		RpcEntityUtils.ActionGenerator.generateRpcPartActionsAction(entity, actionsModel);
	}

	public static void updateRpcDateFieldModel(RpcEntity entity, RpcDateFieldModel model, String key, String text) {
		if (text != null) {
			if (key.equals(AnnotationConstants.PATTERN)) {
				model.setPattern(text);
			}
		}
		RpcEntityUtils.ActionGenerator.generateRpcDateFieldActions(entity, model, true);
	}

	public static void updateRpcEnumFieldModel(RpcEntity entity, RpcEnumFieldModel model, String key, String text,
			String fullyQualifiedName) throws MalformedURLException, CoreException {
		if (text != null) {
			if (key.equals(Constants.JAVA_TYPE)) {
				model.setJavaTypeName(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setType(clazz);
				}
			}
		}
		RpcEntityUtils.ActionGenerator.generateRpcEnumFieldActions(entity, model, true);
	}

	public static void updateRpcActionModel(RpcEntity entity, ActionModel model, RpcActionsModel actionsModel, String key,
			String text, Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {

		if (selection != null) {
			if (key.equals(AnnotationConstants.GLOBAL)) {
				model.setGlobal(selection);
			}
		}
		if (text != null) {
			if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(AnnotationConstants.ALIAS)) {
				model.setAlias(text);
			} else if (key.equals(RpcAnnotationConstants.PATH)) {
				model.setProgramPath(text);
			} else if (key.equals(AnnotationConstants.TARGET_ENTITY)) {
				model.setTargetEntityClassName(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setTargetEntity(clazz);
				}
			} else if (key.equals(AnnotationConstants.ACTION)) {
				if (!StringUtils.isEmpty(text)) {
					model.setActionName(text);
					RpcAction action = RpcActions.newAction(text.toUpperCase());
					if (action != null) {
						model.setAction(action);
					}
				} else {
					model.setActionName("");
					model.setAction(null);
				}
			}
		}

		RpcEntityUtils.ActionGenerator.generateRpcActionsAction(entity, actionsModel);
	}

}
