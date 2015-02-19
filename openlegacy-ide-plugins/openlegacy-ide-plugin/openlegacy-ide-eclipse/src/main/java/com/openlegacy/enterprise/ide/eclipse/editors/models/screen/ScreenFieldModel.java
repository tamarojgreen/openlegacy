package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.IConvertedField;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.FieldType;
import org.openlegacy.FieldType.General;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.modules.login.Login.ErrorField;
import org.openlegacy.modules.login.Login.PasswordField;
import org.openlegacy.modules.login.Login.UserField;
import org.openlegacy.modules.menu.Menu.MenuSelectionField;
import org.openlegacy.modules.messages.Messages.MessageField;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents @ScreenField annotation. As default field has a String type.
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenFieldModel extends ScreenNamedObject implements IConvertedField {

	// map of field types
	public static Map<String, Class<? extends FieldType>> mapFieldTypes = Collections.unmodifiableMap(new HashMap<String, Class<? extends FieldType>>() {

		private static final long serialVersionUID = 7980474565011128563L;

		{
			put(General.class.getSimpleName(), General.class);
			put(ErrorField.class.getSimpleName(), ErrorField.class);
			put(MenuSelectionField.class.getSimpleName(), MenuSelectionField.class);
			put(MessageField.class.getSimpleName(), MessageField.class);
			put(PasswordField.class.getSimpleName(), PasswordField.class);
			put(UserField.class.getSimpleName(), UserField.class);
		}
	});

	// annotation attributes
	private int row = 0;
	private int column = 0;
	private Integer endColumn = 0;
	private Integer endRow = 0;
	private boolean rectangle = false;
	private boolean editable = false;
	private boolean password = false;
	private Class<? extends FieldType> fieldType = FieldType.General.class;
	private String displayName = "";//$NON-NLS-1$
	private String sampleValue = "";//$NON-NLS-1$
	private String defaultValue = "";//$NON-NLS-1$
	private int labelColumn = 0;
	private boolean key = false;
	private String helpText = "";//$NON-NLS-1$
	private boolean rightToLeft = false;
	private FieldAttributeType attribute = FieldAttributeType.Value;
	private String when = "";//$NON-NLS-1$
	private String unless = "";//$NON-NLS-1$
	private int keyIndex = -1;
	private boolean internal = false;
	private boolean global = false;
	private String nullValue = "";//$NON-NLS-1$
	private boolean tableKey = false;
	private boolean forceUpdate = false;
	private String expression = "";//$NON-NLS-1$
	private boolean enableLookup = false;
	// other
	private String fieldName = Messages.getString("Field.new");//$NON-NLS-1$
	private FieldTypeDefinition fieldTypeDefinition = null;
	private String fieldTypeName = FieldType.General.class.getSimpleName();
	protected boolean initialized = false;
	protected String javaTypeName = String.class.getSimpleName();
	private ScreenDescriptionFieldModel descriptionFieldModel = new ScreenDescriptionFieldModel();
	private ScreenDynamicFieldModel dynamicFieldModel = new ScreenDynamicFieldModel();

	protected String previousFieldName = Messages.getString("Field.new");//$NON-NLS-1$

	protected Map<String, Object> fieldsMap = new HashMap<String, Object>();

	public ScreenFieldModel(NamedObject parent) {
		super(ScreenField.class.getSimpleName());
		this.parent = parent;
	}

	public ScreenFieldModel(String name, NamedObject parent) {
		super(name);
		this.parent = parent;
	}

	/**
	 * @param uuid
	 */
	public ScreenFieldModel(UUID uuid, NamedObject parent) {
		super(ScreenField.class.getSimpleName());
		this.uuid = uuid;
		this.parent = parent;
	}

	@Override
	public ScreenFieldModel clone() {
		ScreenFieldModel model = new ScreenFieldModel(this.uuid, this.parent);
		fillModel(model);
		return model;
	}

	protected void fillModel(ScreenFieldModel model) {
		if (model == null) {
			return;
		}
		model.setModelName(this.modelName);
		model.setFieldName(this.fieldName);
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setRow(this.row);
		model.setColumn(this.column);
		model.setEndRow(this.endRow);
		model.setEndColumn(this.endColumn);
		model.setRectangle(this.rectangle);
		model.setEditable(this.editable);
		model.setPassword(this.password);
		model.setFieldType(this.fieldType);
		model.setDisplayName(this.displayName);
		model.setSampleValue(this.sampleValue);
		model.setDefaultValue(this.defaultValue);
		model.setLabelColumn(this.labelColumn);
		model.setKey(this.key);
		model.setFieldTypeName(this.fieldTypeName);
		model.setFieldTypeDefinition(this.fieldTypeDefinition);
		model.setHelpText(this.helpText);
		model.setRightToLeft(this.rightToLeft);
		model.setAttribute(this.attribute);
		model.setWhen(this.when);
		model.setUnless(this.unless);
		model.setDescriptionFieldModel(this.descriptionFieldModel.clone());
		model.setDynamicFieldModel(this.dynamicFieldModel.clone());
		model.setKeyIndex(keyIndex);
		model.setInternal(internal);
		model.setGlobal(global);
		model.setNullValue(nullValue);
		model.setTableKey(tableKey);
		model.setForceUpdate(forceUpdate);
		model.setExpression(expression);
		model.setEnableLookup(enableLookup);
		model.initialized = this.initialized;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ScreenFieldModel)) {
			return false;
		}
		ScreenFieldModel model = (ScreenFieldModel)obj;
		return this.modelName.equals(model.getModelName()) && this.fieldName.equals(model.getFieldName())
				&& this.row == model.getRow() && this.column == model.getColumn() && this.endRow == model.getEndRow()
				&& this.endColumn == model.getEndColumn() && this.rectangle == model.isRectangle()
				&& this.editable == model.isEditable() && this.password == model.isPassword()
				&& this.fieldType == model.getFieldType() && this.displayName.equals(model.getDisplayName())
				&& this.sampleValue.equals(model.getSampleValue()) && this.labelColumn == model.getLabelColumn()
				&& this.key == model.isKey() && this.fieldTypeName.equals(model.getFieldTypeName())
				&& this.parent == model.getParent() && this.helpText.equals(model.getHelpText())
				&& this.rightToLeft == model.isRightToLeft() && this.attribute.equals(model.getAttribute())
				&& this.when.equals(model.getWhen()) && this.unless.equals(model.getUnless())
				&& this.descriptionFieldModel.equals(model.getDescriptionFieldModel()) && keyIndex == model.getKeyIndex()
				&& this.dynamicFieldModel.equals(model.getDynamicFieldModel()) && internal == model.isInternal()
				&& global == model.isGlobal() && nullValue.equals(model.getNullValue()) && tableKey == model.isTableKey()
				&& forceUpdate == model.isForceUpdate() && StringUtils.equals(expression, model.getExpression())
				&& enableLookup == model.isEnableLookup();
	}

	public FieldAttributeType getAttribute() {
		return attribute;
	}

	public int getColumn() {
		return column;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Integer getEndColumn() {
		return endColumn;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Class<? extends FieldType> getFieldType() {
		return fieldType;
	}

	public FieldTypeDefinition getFieldTypeDefinition() {
		return fieldTypeDefinition;
	}

	public String getFieldTypeName() {
		return fieldTypeName;
	}

	public String getHelpText() {
		return helpText;
	}

	public String getJavaTypeName() {
		return javaTypeName;
	}

	public int getLabelColumn() {
		return labelColumn;
	}

	public String getPreviousFieldName() {
		return previousFieldName;
	}

	public int getRow() {
		return row;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getUnless() {
		return unless;
	}

	public String getWhen() {
		return when;
	}

	/**
	 * Method <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> is not supported for this model. Use
	 * <code>init(ScreenFieldDefinition screenFieldDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedScreenEntityDefinition entityDefinition) is not supported for this model. Use init(ScreenFieldDefinition screenFieldDefinition) instead.");//$NON-NLS-1$
	}

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		if (screenFieldDefinition == null) {
			return;
		}
		this.fieldName = Utils.getFieldNameWithoutPrefix(screenFieldDefinition.getName());
		this.previousFieldName = this.fieldName;
		this.row = (screenFieldDefinition.getPosition() != null) ? screenFieldDefinition.getPosition().getRow() : 0;
		this.column = (screenFieldDefinition.getPosition() != null) ? screenFieldDefinition.getPosition().getColumn() : 0;
		this.endRow = (screenFieldDefinition.getEndPosition() != null) ? screenFieldDefinition.getEndPosition().getRow() : 0;
		this.endColumn = (screenFieldDefinition.getEndPosition() != null) ? screenFieldDefinition.getEndPosition().getColumn()
				: 0;
		this.rectangle = screenFieldDefinition.isRectangle();
		this.editable = screenFieldDefinition.isEditable();
		this.password = screenFieldDefinition.isPassword();
		this.fieldType = screenFieldDefinition.getType() == null ? General.class : screenFieldDefinition.getType();
		this.displayName = (screenFieldDefinition.getDisplayName() == null) ? "" : screenFieldDefinition.getDisplayName();//$NON-NLS-1$
		this.sampleValue = (screenFieldDefinition.getSampleValue() == null) ? "" : screenFieldDefinition.getSampleValue();//$NON-NLS-1$
		this.defaultValue = (screenFieldDefinition.getDefaultValue() == null) ? "" : screenFieldDefinition.getDefaultValue();//$NON-NLS-1$
		this.labelColumn = (screenFieldDefinition.getLabelPosition() != null) ? screenFieldDefinition.getLabelPosition().getColumn()
				: 0;
		this.key = screenFieldDefinition.isKey();
		this.fieldTypeName = screenFieldDefinition.getFieldTypeName() != null ? screenFieldDefinition.getFieldTypeName()
				: this.fieldTypeName;

		this.fieldTypeDefinition = screenFieldDefinition.getFieldTypeDefinition();

		if (screenFieldDefinition instanceof SimpleScreenFieldDefinition) {
			this.javaTypeName = ((SimpleScreenFieldDefinition)screenFieldDefinition).getJavaTypeName();
		}
		this.helpText = screenFieldDefinition.getHelpText() == null ? "" : screenFieldDefinition.getHelpText();//$NON-NLS-1$
		this.rightToLeft = screenFieldDefinition.isRightToLeft();
		this.attribute = screenFieldDefinition.getAttribute();
		this.when = screenFieldDefinition.getWhenFilter() == null ? "" : screenFieldDefinition.getWhenFilter();//$NON-NLS-1$
		this.unless = screenFieldDefinition.getUnlessFilter() == null ? "" : screenFieldDefinition.getUnlessFilter();//$NON-NLS-1$

		this.keyIndex = screenFieldDefinition.getKeyIndex();
		this.internal = screenFieldDefinition.isInternal();
		this.global = screenFieldDefinition.isGlobal();
		this.nullValue = screenFieldDefinition.getNullValue() == null ? "" : screenFieldDefinition.getNullValue();//$NON-NLS-1$

		this.tableKey = screenFieldDefinition.isTableKey();
		this.forceUpdate = screenFieldDefinition.isForceUpdate();

		expression = screenFieldDefinition.getExpression() == null ? "" : screenFieldDefinition.getExpression();//$NON-NLS-1$

		enableLookup = screenFieldDefinition.isEnableLookup();

		this.descriptionFieldModel.setUUID(this.uuid);
		this.descriptionFieldModel.init(screenFieldDefinition.getDescriptionFieldDefinition());

		this.dynamicFieldModel.setUUID(this.uuid);
		this.dynamicFieldModel.init(screenFieldDefinition);

		initialized = true;
	}

	public boolean isEditable() {
		return editable;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public boolean isKey() {
		return key;
	}

	public boolean isPassword() {
		return password;
	}

	public boolean isRectangle() {
		return rectangle;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setAttribute(FieldAttributeType attribute) {
		this.attribute = attribute;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setEndColumn(Integer endColumn) {
		this.endColumn = endColumn;
	}

	public void setEndRow(Integer endRow) {
		this.endRow = endRow;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setFieldType(Class<? extends FieldType> fieldType) {
		this.fieldType = fieldType;
	}

	public void setFieldTypeDefinition(FieldTypeDefinition fieldTypeDefinition) {
		this.fieldTypeDefinition = fieldTypeDefinition;
	}

	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public void setLabelColumn(int labelColumn) {
		this.labelColumn = labelColumn;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public void setRectangle(boolean rectangle) {
		this.rectangle = rectangle;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setUnless(String unless) {
		this.unless = unless;
	}

	public void setWhen(String when) {
		this.when = when;
	}

	public ScreenDescriptionFieldModel getDescriptionFieldModel() {
		return descriptionFieldModel;
	}

	public void setDescriptionFieldModel(ScreenDescriptionFieldModel descriptionFieldModel) {
		this.descriptionFieldModel = descriptionFieldModel;
	}

	public ScreenDynamicFieldModel getDynamicFieldModel() {
		return dynamicFieldModel;
	}

	public void setDynamicFieldModel(ScreenDynamicFieldModel dynamicFieldModel) {
		this.dynamicFieldModel = dynamicFieldModel;
	}

	public int getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}

	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public void setFieldTypeDefaultValue() {
		fieldType = FieldType.General.class;
		fieldTypeName = fieldType.getSimpleName();
	}

	public void setAttributeDefaultValue() {
		attribute = FieldAttributeType.Value;
	}

	public boolean isTableKey() {
		return tableKey;
	}

	public void setTableKey(boolean tableKey) {
		this.tableKey = tableKey;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean isEnableLookup() {
		return enableLookup;
	}

	public void setEnableLookup(boolean enableLookup) {
		this.enableLookup = enableLookup;
	}

	@Override
	@SuppressWarnings("unchecked")
	public NamedObject convertFrom(NamedObject model) {
		if (!(model instanceof ScreenFieldModel)) {
			return null;
		}
		ScreenFieldModel screenModel = (ScreenFieldModel)model;
		screenModel.fillFieldsMap();
		row = screenModel.getFieldValue("row") != null ? (Integer)screenModel.getFieldValue("row") : 0;
		column = screenModel.getFieldValue("column") != null ? (Integer)screenModel.getFieldValue("column") : 0;
		endColumn = screenModel.getFieldValue("endColumn") != null ? (Integer)screenModel.getFieldValue("endColumn") : 0;
		endRow = screenModel.getFieldValue("endRow") != null ? (Integer)screenModel.getFieldValue("endRow") : 0;
		rectangle = screenModel.getFieldValue("rectangle") != null ? (Boolean)screenModel.getFieldValue("rectangle") : false;
		editable = screenModel.getFieldValue("editable") != null ? (Boolean)screenModel.getFieldValue("editable") : false;
		password = screenModel.getFieldValue("password") != null ? (Boolean)screenModel.getFieldValue("password") : false;
		fieldType = screenModel.getFieldValue("fieldType") != null ? (Class<? extends FieldType>)screenModel.getFieldValue("fieldType")
				: FieldType.General.class;
		displayName = screenModel.getFieldValue("displayName") != null ? (String)screenModel.getFieldValue("displayName") : "";
		sampleValue = screenModel.getFieldValue("sampleValue") != null ? (String)screenModel.getFieldValue("sampleValue") : "";
		defaultValue = screenModel.getFieldValue("defaultValue") != null ? (String)screenModel.getFieldValue("defaultValue") : "";
		labelColumn = screenModel.getFieldValue("labelColumn") != null ? (Integer)screenModel.getFieldValue("labelColumn") : 0;
		key = screenModel.getFieldValue("key") != null ? (Boolean)screenModel.getFieldValue("key") : false;
		helpText = screenModel.getFieldValue("helpText") != null ? (String)screenModel.getFieldValue("helpText") : "";
		rightToLeft = screenModel.getFieldValue("rightToLeft") != null ? (Boolean)screenModel.getFieldValue("rightToLeft")
				: false;
		attribute = screenModel.getFieldValue("attribute") != null ? (FieldAttributeType)screenModel.getFieldValue("attribute")
				: FieldAttributeType.Value;
		when = screenModel.getFieldValue("when") != null ? (String)screenModel.getFieldValue("when") : "";
		unless = screenModel.getFieldValue("unless") != null ? (String)screenModel.getFieldValue("unless") : "";
		keyIndex = screenModel.getFieldValue("keyIndex") != null ? (Integer)screenModel.getFieldValue("keyIndex") : -1;
		internal = screenModel.getFieldValue("internal") != null ? (Boolean)screenModel.getFieldValue("internal") : false;
		global = screenModel.getFieldValue("global") != null ? (Boolean)screenModel.getFieldValue("global") : false;
		nullValue = screenModel.getFieldValue("nullValue") != null ? (String)screenModel.getFieldValue("nullValue") : "";
		tableKey = screenModel.getFieldValue("tableKey") != null ? (Boolean)screenModel.getFieldValue("tableKey") : false;
		forceUpdate = screenModel.getFieldValue("forceUpdate") != null ? (Boolean)screenModel.getFieldValue("forceUpdate")
				: false;
		expression = screenModel.getFieldValue("expression") != null ? (String)screenModel.getFieldValue("expression") : "";
		enableLookup = screenModel.getFieldValue("enableLookup") != null ? (Boolean)screenModel.getFieldValue("enableLookup")
				: false;
		fieldName = screenModel.getFieldValue("fieldName") != null ? (String)screenModel.getFieldValue("fieldName")
				: Messages.getString("Field.new");
		fieldTypeDefinition = screenModel.getFieldValue("fieldTypeDefinition") != null ? (FieldTypeDefinition)screenModel.getFieldValue("fieldTypeDefinition")
				: null;
		fieldTypeName = screenModel.getFieldValue("fieldTypeName") != null ? (String)screenModel.getFieldValue("fieldTypeName")
				: FieldType.General.class.getSimpleName();
		// initialized = screenModel.getFieldValue("initialized") != null ? (Boolean)screenModel.getFieldValue("initialized") :
		// false;
		// javaTypeName = screenModel.getFieldValue("javaTypeName") != null ? (String)screenModel.getFieldValue("javaTypeName")
		// : String.class.getSimpleName();
		descriptionFieldModel = screenModel.getFieldValue("descriptionFieldModel") != null ? ((ScreenDescriptionFieldModel)screenModel.getFieldValue("descriptionFieldModel")).clone()
				: new ScreenDescriptionFieldModel();

		dynamicFieldModel = screenModel.getFieldValue("dynamicFieldModel") != null ? ((ScreenDynamicFieldModel)screenModel.getFieldValue("dynamicFieldModel")).clone()
				: new ScreenDynamicFieldModel();

		previousFieldName = screenModel.getFieldValue("previousFieldName") != null ? (String)screenModel.getFieldValue("previousFieldName")
				: Messages.getString("Field.new");
		fieldsMap.putAll(screenModel.fieldsMap);
		return this;
	}

	@Override
	public void fillFieldsMap() {
		fieldsMap.clear();
		fieldsMap.put("row", row);
		fieldsMap.put("column", column);
		fieldsMap.put("endColumn", endColumn);
		fieldsMap.put("endRow", endRow);
		fieldsMap.put("rectangle", rectangle);
		fieldsMap.put("editable", editable);
		fieldsMap.put("password", password);
		fieldsMap.put("fieldType", fieldType);
		fieldsMap.put("displayName", displayName);
		fieldsMap.put("sampleValue", sampleValue);
		fieldsMap.put("defaultValue", defaultValue);
		fieldsMap.put("labelColumn", labelColumn);
		fieldsMap.put("key", key);
		fieldsMap.put("helpText", helpText);
		fieldsMap.put("rightToLeft", rightToLeft);
		fieldsMap.put("attribute", attribute);
		fieldsMap.put("when", when);
		fieldsMap.put("unless", unless);
		fieldsMap.put("keyIndex", keyIndex);
		fieldsMap.put("internal", internal);
		fieldsMap.put("global", global);
		fieldsMap.put("nullValue", nullValue);
		fieldsMap.put("tableKey", tableKey);
		fieldsMap.put("forceUpdate", forceUpdate);
		fieldsMap.put("expression", expression);
		fieldsMap.put("enableLookup", enableLookup);
		fieldsMap.put("fieldName", fieldName);
		fieldsMap.put("fieldTypeDefinition", fieldTypeDefinition);
		fieldsMap.put("fieldTypeName", fieldTypeName);
		// fieldsMap.put("initialized", initialized);
		fieldsMap.put("javaTypeName", javaTypeName);
		fieldsMap.put("descriptionFieldModel", descriptionFieldModel);
		fieldsMap.put("dynamicFieldModel", dynamicFieldModel);
		fieldsMap.put("previousFieldName", previousFieldName);
	}

	protected Object getFieldValue(String key) {
		return fieldsMap.get(key);
	}

}
