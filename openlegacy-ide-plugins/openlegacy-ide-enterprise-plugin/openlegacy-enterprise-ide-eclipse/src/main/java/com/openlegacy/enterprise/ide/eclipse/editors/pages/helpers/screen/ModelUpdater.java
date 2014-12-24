package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ChildEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIdentifiersModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.screen.ScreenEntityUtils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenRecordsProvider;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.table.ScreenTableCollector;
import org.openlegacy.terminal.table.TerminalDrilldownAction;

import java.net.MalformedURLException;

/**
 * @author Ivan Bort
 * 
 */
public class ModelUpdater {

	/**
	 * @param model
	 * @param fullyQualifiedName
	 * @throws CoreException
	 * @throws MalformedURLException
	 */
	public static void updateChildEntityModel(ChildEntityModel model, String fullyQualifiedName) throws MalformedURLException,
			CoreException {

		if (fullyQualifiedName != null) {
			Class<?> clazz = Utils.getClazz(fullyQualifiedName);
			model.setClazz(clazz);
			model.setClassName(clazz.getSimpleName());
		}
	}

	public static void updateScreenActionsModel(ScreenEntity entity, ScreenActionsModel model) {
		ScreenEntityUtils.ActionGenerator.generateScreenActionsActions(entity, model);
	}

	public static void updateScreenBooleanFieldModel(ScreenEntity entity, ScreenBooleanFieldModel model, String key, String text,
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
		ScreenEntityUtils.ActionGenerator.generateScreenBooleanFieldActions(entity, model);
	}

	/**
	 * @param entity
	 * @param model
	 * @param key
	 * @param text
	 * 
	 * @param selection
	 * @param fullyQualifiedName
	 * @throws CoreException
	 * @throws MalformedURLException
	 */
	public static void updateScreenColumnModel(ScreenEntity entity, ScreenColumnModel model, String key, String text,
			Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {

		if (text != null) {
			if (key.equals(Constants.FIELD_NAME)) {
				model.setFieldName(StringUtils.uncapitalize(text));
			} else if (key.equals(ScreenAnnotationConstants.START_COLUMN)) {
				model.setStartColumn(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.END_COLUMN)) {
				model.setEndColumn(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(AnnotationConstants.SAMPLE_VALUE)) {
				model.setSampleValue(text);
			} else if (key.equals(ScreenAnnotationConstants.ROWS_OFFSET)) {
				model.setRowsOffset(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.HELP_TEXT)) {
				model.setHelpText(text);
			} else if (key.equals(ScreenAnnotationConstants.COL_SPAN)) {
				model.setColSpan(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.SORT_INDEX)) {
				model.setSortIndex((text.isEmpty() || text.equals("-")) ? -1 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.ATTRIBUTE)) {
				if (!StringUtils.isEmpty(text)) {
					model.setAttribute(FieldAttributeType.valueOf(text));
				} else {
					model.setAttributeDefaultValue();
				}
			} else if (key.equals(AnnotationConstants.TARGET_ENTITY)) {
				model.setTargetEntityClassName(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setTargetEntity(clazz);
				}
			} else if (key.equals(ScreenAnnotationConstants.EXPRESSION)) {
				model.setExpression(StringUtils.isEmpty(text) ? "" : text);
			}
		}
		if (selection != null) {
			if (key.equals(AnnotationConstants.KEY)) {
				model.setKey(selection);
			} else if (key.equals(ScreenAnnotationConstants.SELECTION_FIELD)) {
				model.setSelectionField(selection);
			} else if (key.equals(AnnotationConstants.MAIN_DISPLAY_FIELD)) {
				model.setMainDisplayField(selection);
			} else if (key.equals(AnnotationConstants.EDITABLE)) {
				model.setEditable(selection);
			}
		}

		ScreenEntityUtils.ActionGenerator.generateScreenColumnActions(entity, model);
	}

	public static void updateScreenDateFieldModel(ScreenEntity entity, ScreenDateFieldModel model, String key, String text) {

		if (text != null) {
			if (key.equals(AnnotationConstants.YEAR_COLUMN)) {
				model.setYear(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.MONTH_COLUMN)) {
				model.setMonth(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.DAY_COLUMN)) {
				model.setDay(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.PATTERN)) {
				model.setPattern(text.trim());
			}
		}
		ScreenEntityUtils.ActionGenerator.generateScreenDateFieldActions(entity, model);
	}

	@SuppressWarnings("unchecked")
	public static void updateScreenEntityModel(ScreenEntity entity, ScreenEntityModel model, String key, String text,
			Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {

		if (selection != null) {
			if (key.equals(ScreenAnnotationConstants.CHILD)) {
				model.setChild(selection);
			} else if (key.equals(ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA)) {
				model.setSupportTerminalData(selection);
			} else if (key.equals(ScreenAnnotationConstants.WINDOW)) {
				model.setWindow(selection);
			} else if (key.equals(ScreenAnnotationConstants.VALIDATE_KEYS)) {
				model.setValidateKeys(selection);
			} else if (key.equals(ScreenAnnotationConstants.RIGHT_TO_LEFT)) {
				model.setRightToLeft(selection);
			} else if (key.equals(ScreenAnnotationConstants.AUTO_MAP_KEYBOARD_ACTIONS)) {
				model.setAutoMapKeyboardActions(selection);
			}
		}
		if (text != null) {
			if (key.equals(ScreenAnnotationConstants.COLUMNS)) {
				model.setColumns(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.ROWS)) {
				model.setRows(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(AnnotationConstants.NAME)) {
				model.setName(text);
			} else if (key.equals(ScreenAnnotationConstants.SCREEN_TYPE)) {
				if (ScreenEntityModel.mapScreenTypes.get(text) != null) {
					model.setScreenType(ScreenEntityModel.mapScreenTypes.get(text));
				} else if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setScreenType((Class<? extends EntityType>)clazz);
				} else {
					model.setScreenTypeDefaultValue();
				}
			}
		}
		ScreenEntityUtils.ActionGenerator.generateScreenEntityActions(entity, model);
	}

	public static void updateScreenEnumFieldModel(ScreenEntity entity, ScreenEnumFieldModel model, String key, String text,
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
		ScreenEntityUtils.ActionGenerator.generateScreenEnumFieldActions(entity, model);
	}

	@SuppressWarnings("unchecked")
	public static void updateScreenFieldModel(ScreenEntity entity, ScreenFieldModel model, String key, String text,
			Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {
		if (selection != null) {
			if (key.equals(ScreenAnnotationConstants.RECTANGLE)) {
				model.setRectangle(selection);
			} else if (key.equals(AnnotationConstants.EDITABLE)) {
				model.setEditable(selection);
			} else if (key.equals(AnnotationConstants.PASSWORD)) {
				model.setPassword(selection);
			} else if (key.equals(AnnotationConstants.KEY)) {
				model.setKey(selection);
			} else if (key.equals(AnnotationConstants.RIGHT_TO_LEFT)) {
				model.setRightToLeft(selection);
			} else if (key.equals(ScreenAnnotationConstants.INTERNAL)) {
				model.setInternal(selection);
			} else if (key.equals(ScreenAnnotationConstants.GLOBAL)) {
				model.setGlobal(selection);
			} else if (key.equals(ScreenAnnotationConstants.TABLE_KEY)) {
				model.setTableKey(selection);
			} else if (key.equals(ScreenAnnotationConstants.FORCE_UPDATE)) {
				model.setForceUpdate(selection);
			} else if (key.equals(ScreenAnnotationConstants.ENABLE_LOOKUP)) {
				model.setEnableLookup(selection);
			}
		}
		if (text != null) {
			if (key.equals(ScreenAnnotationConstants.ROW)) {
				model.setRow(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.COLUMN)) {
				model.setColumn(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.END_ROW)) {
				model.setEndRow(text.isEmpty() ? null : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.END_COLUMN)) {
				model.setEndColumn(text.isEmpty() ? null : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(AnnotationConstants.SAMPLE_VALUE)) {
				model.setSampleValue(text);
			} else if (key.equals(AnnotationConstants.DEFAULT_VALUE)) {
				model.setDefaultValue(text);
			} else if (key.equals(ScreenAnnotationConstants.LABEL_COLUMN)) {
				model.setLabelColumn(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(AnnotationConstants.FIELD_TYPE)) {
				if (!StringUtils.isEmpty(text)) {
					if (ScreenFieldModel.mapFieldTypes.get(text) != null) {
						model.setFieldType(ScreenFieldModel.mapFieldTypes.get(text));
						model.setFieldTypeName(ScreenFieldModel.mapFieldTypes.get(text).getSimpleName());
					} else if (fullyQualifiedName != null) {
						Class<?> clazz = Utils.getClazz(fullyQualifiedName);
						model.setFieldType((Class<? extends FieldType>)clazz);
						model.setFieldTypeName(clazz.getSimpleName());
					}
				} else {
					model.setFieldTypeDefaultValue();
				}
			} else if (key.equals(Constants.FIELD_NAME)) {
				model.setFieldName(StringUtils.uncapitalize(text));
			} else if (key.equals(AnnotationConstants.HELP_TEXT)) {
				model.setHelpText(text);
			} else if (key.equals(ScreenAnnotationConstants.WHEN)) {
				model.setWhen(text);
			} else if (key.equals(ScreenAnnotationConstants.UNLESS)) {
				model.setUnless(text);
			} else if (key.equals(AnnotationConstants.ATTRIBUTE)) {
				if (!StringUtils.isEmpty(text)) {
					model.setAttribute(FieldAttributeType.valueOf(text));
				} else {
					model.setAttributeDefaultValue();
				}
			} else if (key.equals(ScreenAnnotationConstants.KEY_INDEX)) {
				model.setKeyIndex((text.isEmpty() || text.equals("-")) ? -1 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.NULL_VALUE)) {
				model.setNullValue(text);
			} else if (key.equals(ScreenAnnotationConstants.EXPRESSION)) {
				model.setExpression(text);
			}
		}
		ScreenEntityUtils.ActionGenerator.generateScreenFieldActions(entity, model);
	}

	public static void updateScreenDescriptionFieldModel(ScreenEntity entity, ScreenFieldModel model, String key, String text) {
		if (text != null) {
			if (key.equals(Constants.DESC_ROW)) {
				model.getDescriptionFieldModel().setRow(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(Constants.DESC_COLUMN)) {
				model.getDescriptionFieldModel().setColumn(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(Constants.DESC_END_COLUMN)) {
				model.getDescriptionFieldModel().setEndColumn(text.isEmpty() ? 0 : Integer.valueOf(text));
			}
		}
		ScreenEntityUtils.ActionGenerator.generateScreenDescriptionFieldActions(entity, model);
	}

	@SuppressWarnings("unchecked")
	public static void updateScreenFieldValuesModel(ScreenEntity entity, ScreenFieldValuesModel model, String key, String text,
			Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {

		if (text != null) {
			if (key.equals(ScreenAnnotationConstants.SOURCE_SCREEN_ENTITY)) {
				model.setSourceScreenEntityName(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setSourceScreenEntity(clazz);
				}
			} else if (key.equals(AnnotationConstants.PROVIDER)) {
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setProvider((Class<? extends ScreenRecordsProvider>)clazz);
				}
			} else if (key.equals(ScreenAnnotationConstants.DISPLAY_FIELD_NAME)) {
				model.setDisplayFieldName(text);
			} else if (key.equals(ScreenAnnotationConstants.SEARCH_FIELD)) {
				model.setSearchField(text);
			} else if (key.equals(ScreenAnnotationConstants.AUTO_SUBMIT_ACTION)) {
				if (!StringUtils.isEmpty(text)) {
					model.setAutoSubmitActionName(text);
					TerminalAction action = TerminalActions.newAction(text.toUpperCase());
					if (action != null) {
						model.setAutoSubmitAction(action.getClass());
					} else if (fullyQualifiedName != null) {
						Class<?> clazz = Utils.getClazz(fullyQualifiedName);
						model.setAutoSubmitAction((Class<? extends TerminalAction>)clazz);
					}
				} else {
					model.setAutoSubmitActionDefaultValue();
				}
			}
		}
		if (selection != null) {
			if (key.equals(ScreenAnnotationConstants.COLLECT_ALL)) {
				model.setCollectAll(selection);
			} else if (key.equals(ScreenAnnotationConstants.AS_WINDOW)) {
				model.setAsWindow(selection);
			}
		}
		ScreenEntityUtils.ActionGenerator.generateScreenFieldValuesActions(entity, model);
	}

	public static void updateScreenIdentifiersModel(ScreenEntity entity, ScreenIdentifiersModel model) {
		ScreenEntityUtils.ActionGenerator.generateScreenIdentifiersActions(entity, model);
	}

	@SuppressWarnings("unchecked")
	public static void updateScreenNavigationModel(ScreenEntity entity, ScreenNavigationModel model, String key, String text,
			Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {
		if (selection != null) {
			if (key.equals(ScreenAnnotationConstants.REQUIRES_PARAMETERS)) {
				model.setRequiresParameters(selection);
			}
		}
		if (text != null) {
			if (key.equals(ScreenAnnotationConstants.ACCESSED_FROM)) {
				model.setAccessedFromEntityName(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setAccessedFrom(clazz);
				}
			} else if (key.equals(ScreenAnnotationConstants.TERMINAL_ACTION)) {
				if (!StringUtils.isEmpty(text)) {
					model.setTerminalActionName(text);
					TerminalAction action = TerminalActions.newAction(text.toUpperCase());
					if (action != null) {
						model.setTerminalAction(action.getClass());
					} else if (fullyQualifiedName != null) {
						Class<?> clazz = Utils.getClazz(fullyQualifiedName);
						model.setTerminalAction((Class<? extends TerminalAction>)clazz);
					}
				} else {
					model.setTerminalActionDefaultValue();
				}
			} else if (key.equals(ScreenAnnotationConstants.EXIT_ACTION)) {
				if (!StringUtils.isEmpty(text)) {
					model.setExitActionName(text);
					TerminalAction action = TerminalActions.newAction(text.toUpperCase());
					if (action != null) {
						model.setExitAction(action.getClass());
					} else if (fullyQualifiedName != null) {
						Class<?> clazz = Utils.getClazz(fullyQualifiedName);
						model.setExitAction((Class<? extends TerminalAction>)clazz);
					}
				} else {
					model.setExitActionDefaultValue();
				}
			} else if (key.equals(ScreenAnnotationConstants.ADDITIONAL_KEY)) {
				if (!StringUtils.isEmpty(text)) {
					model.setAdditionalKey(AdditionalKey.valueOf(text.toUpperCase()));
				} else {
					model.setAdditionalKeyDefaultValue();
				}
			} else if (key.equals(ScreenAnnotationConstants.EXIT_ADDITIONAL_KEY)) {
				if (!StringUtils.isEmpty(text)) {
					model.setExitAdditionalKey(AdditionalKey.valueOf(text.toUpperCase()));
				} else {
					model.setExitAdditionalKeyDefaultValue();
				}
			} else if (key.equals(ScreenAnnotationConstants.DRILLDOWN_VALUE)) {
				model.setDrilldownValue(text);
			}
		}
		ScreenEntityUtils.ActionGenerator.generateScreenNavigationActions(entity, model);
	}

	/**
	 * @param entity
	 * @param model
	 * @param key
	 * @param text
	 * @param selection
	 */
	public static void updateScreenPartModel(ScreenEntity entity, ScreenPartModel model, String key, String text,
			Boolean selection) {
		if (text != null) {
			if (key.equals(Constants.JAVA_TYPE_NAME)) {
				model.setClassName(StringUtils.capitalize(text.replaceAll("\\s+", "")));
			} else if (key.equals(AnnotationConstants.NAME)) {
				model.setName(text);
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(ScreenAnnotationConstants.ROW)) {
				model.getPartPosition().setRow(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.COLUMN)) {
				model.getPartPosition().setColumn(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.WIDTH)) {
				model.getPartPosition().setWidth(text.isEmpty() ? 0 : Integer.valueOf(text));
			}
		}
		if (selection != null) {
			if (key.equals(ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA)) {
				model.setSupportTerminalData(selection);
			}
		}
		ScreenEntityUtils.ActionGenerator.generateScreenPartActions(entity, model);
	}

	/**
	 * @param entity
	 * @param model
	 * @param key
	 * @param text
	 * @param selection
	 * @param fullyQualifiedName
	 * @throws CoreException
	 * @throws MalformedURLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void updateScreenTableModel(ScreenEntity entity, ScreenTableModel model, String key, String text,
			Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {

		if (text != null) {
			if (key.equals(Constants.JAVA_TYPE_NAME)) {
				model.setClassName(StringUtils.capitalize(text.replaceAll("\\s+", "")));
			} else if (key.equals(AnnotationConstants.NAME)) {
				model.setName(text);
			} else if (key.equals(ScreenAnnotationConstants.TABLE_COLLECTOR)) {
				model.setTableCollectorName(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setTableCollector((Class<? extends ScreenTableCollector>)clazz);
				}
			} else if (key.equals(ScreenAnnotationConstants.ROW_GAPS)) {
				model.setRowGaps(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.END_ROW)) {
				model.setEndRow(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.START_ROW)) {
				model.setStartRow(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.NEXT_SCREEN_ACTION)) {
				if (!StringUtils.isEmpty(text)) {
					model.setNextScreenActionName(text);
					TerminalAction action = TerminalActions.newAction(text.toUpperCase());
					if (action != null) {
						model.setNextScreenAction(action.getClass());
					} else if (fullyQualifiedName != null) {
						Class<?> clazz = Utils.getClazz(fullyQualifiedName);
						model.setNextScreenAction((Class<? extends TerminalAction>)clazz);
					}
				} else {
					model.setNextScreenActionDefaultValue();
				}
			} else if (key.equals(ScreenAnnotationConstants.PREV_SCREEN_ACTION)) {
				if (!StringUtils.isEmpty(text)) {
					model.setPreviousScreenActionName(text);
					TerminalAction action = TerminalActions.newAction(text.toUpperCase());
					if (action != null) {
						model.setPreviousScreenAction(action.getClass());
					} else if (fullyQualifiedName != null) {
						Class<?> clazz = Utils.getClazz(fullyQualifiedName);
						model.setPreviousScreenAction((Class<? extends TerminalAction>)clazz);
					}
				} else {
					model.setPreviousScreenActionDefaultValue();
				}
			} else if (key.equals(ScreenAnnotationConstants.SCREENS_COUNT)) {
				model.setScreensCount(text.isEmpty() ? 0 : Integer.valueOf(text));
			} else if (key.equals(ScreenAnnotationConstants.FILTER_EXPRESSION)) {
				model.setFilterExpression(StringUtils.isEmpty(text) ? "" : text);
			} else if (key.equals(ScreenAnnotationConstants.STOP_EXPRESSION)) {
				model.setStopExpression(StringUtils.isEmpty(text) ? "" : text);
			}
		}
		if (selection != null) {
			if (key.equals(ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA)) {
				model.setSupportTerminalData(selection);
			} else if (key.equals(ScreenAnnotationConstants.SCROLLABLE)) {
				model.setScrollable(selection);
			} else if (key.equals(ScreenAnnotationConstants.RIGHT_TO_LEFT)) {
				model.setRightToLeft(selection);
			}
		}

		ScreenEntityUtils.ActionGenerator.generateScreenTableActions(entity, model);
	}

	/**
	 * @param entity
	 * @param model
	 * @param key
	 * @param text
	 * @param selection
	 * @param fullyQualifiedName
	 * @throws CoreException
	 * @throws MalformedURLException
	 */
	@SuppressWarnings("unchecked")
	public static void updateTableActionModel(ScreenEntity entity, TableActionModel model, String key, String text,
			Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {

		if (text != null) {
			if (key.equals(AnnotationConstants.ACTION)) {
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setAction((Class<? extends TerminalDrilldownAction>)clazz);
				}
			} else if (key.equals(AnnotationConstants.ACTION_VALUE)) {
				model.setActionValue(text);
			} else if (key.equals(AnnotationConstants.DISPLAY_NAME)) {
				model.setDisplayName(text);
			} else if (key.equals(AnnotationConstants.ALIAS)) {
				model.setAlias(text);
			} else if (key.equals(AnnotationConstants.TARGET_ENTITY)) {
				model.setTargetEntityName(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setTargetEntity(clazz);
				}
			} else if (key.equals(ScreenAnnotationConstants.ROW)) {
				model.setRow(StringUtils.isEmpty(text) ? 0 : Integer.parseInt(text));
			} else if (key.equals(ScreenAnnotationConstants.COLUMN)) {
				model.setColumn(StringUtils.isEmpty(text) ? 0 : Integer.parseInt(text));
			} else if (key.equals(ScreenAnnotationConstants.LENGTH)) {
				model.setLength(StringUtils.isEmpty(text) ? 0 : Integer.parseInt(text));
			} else if (key.equals(ScreenAnnotationConstants.WHEN)) {
				model.setWhen(text);
			}
		}
		if (selection != null) {
			if (key.equals(AnnotationConstants.DEFAULT_ACTION)) {
				model.setDefaultAction(selection);
			}
		}
		ScreenEntityUtils.ActionGenerator.generateTableActionActions(entity, model);
	}

}
