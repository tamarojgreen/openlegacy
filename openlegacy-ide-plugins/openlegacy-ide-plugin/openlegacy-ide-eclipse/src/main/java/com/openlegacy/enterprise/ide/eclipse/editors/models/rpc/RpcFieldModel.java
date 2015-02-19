package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.IConvertedField;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.FieldType;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.modules.login.Login.ErrorField;
import org.openlegacy.modules.login.Login.PasswordField;
import org.openlegacy.modules.login.Login.UserField;
import org.openlegacy.modules.menu.Menu.MenuSelectionField;
import org.openlegacy.modules.messages.Messages.MessageField;
import org.openlegacy.rpc.RpcFieldTypes;
import org.openlegacy.rpc.RpcFieldTypes.General;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcFieldModel extends RpcNamedObject implements IConvertedField {

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

	protected String previousFieldName = Messages.getString("Field.new");//$NON-NLS-1$
	protected String javaTypeName = String.class.getSimpleName();
	private String fieldName = Messages.getString("Field.new");//$NON-NLS-1$
	private boolean initialized = false;
	private String fieldTypeName = RpcFieldTypes.General.class.getSimpleName();

	// annotation attributes
	private String originalName = "";//$NON-NLS-1$
	private boolean key = false;
	private Direction direction = Direction.INPUT_OUTPUT;
	private Integer length;
	private Class<? extends FieldType> fieldType = RpcFieldTypes.General.class;
	private String displayName = "";//$NON-NLS-1$
	private String sampleValue = "";//$NON-NLS-1$
	private String helpText = "";//$NON-NLS-1$
	private boolean editable = true;
	private String defaultValue = "";//$NON-NLS-1$
	private String expression = "";//$NON-NLS-1$

	protected Map<String, Object> fieldsMap = new HashMap<String, Object>();

	public RpcFieldModel(RpcNamedObject parent) {
		super(RpcField.class.getSimpleName(), parent);
	}

	public RpcFieldModel(String name, RpcNamedObject parent) {
		super(name, parent);
	}

	public RpcFieldModel(UUID uuid, RpcNamedObject parent) {
		super(RpcField.class.getSimpleName(), parent);
		this.uuid = uuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.rpc.models.NamedObject#init(org.openlegacy.rpc.definitions.RpcFieldDefinition
	 * )
	 */
	@Override
	public void init(RpcFieldDefinition rpcFieldDefinition) {
		if (rpcFieldDefinition == null) {
			return;
		}
		this.fieldName = Utils.getFieldNameWithoutPrefix(rpcFieldDefinition.getName());
		this.previousFieldName = this.fieldName;
		this.fieldTypeName = (((SimpleRpcFieldDefinition)rpcFieldDefinition).getFieldTypeName() != null) ? ((SimpleRpcFieldDefinition)rpcFieldDefinition).getFieldTypeName()
				: this.fieldTypeName;
		this.javaTypeName = ((SimpleRpcFieldDefinition)rpcFieldDefinition).getJavaTypeName();

		// annotation attributes
		this.originalName = (rpcFieldDefinition.getOriginalName() != null) ? rpcFieldDefinition.getOriginalName() : "";//$NON-NLS-1$
		this.key = rpcFieldDefinition.isKey();
		this.direction = (rpcFieldDefinition.getDirection() != null) ? rpcFieldDefinition.getDirection() : Direction.INPUT_OUTPUT;
		this.length = rpcFieldDefinition.getLength();
		this.fieldType = (rpcFieldDefinition.getType() != null) ? rpcFieldDefinition.getType() : RpcFieldTypes.General.class;
		this.displayName = (rpcFieldDefinition.getDisplayName() != null) ? rpcFieldDefinition.getDisplayName() : "";//$NON-NLS-1$
		this.sampleValue = (rpcFieldDefinition.getSampleValue() != null) ? rpcFieldDefinition.getSampleValue() : "";//$NON-NLS-1$
		this.helpText = (rpcFieldDefinition.getHelpText() != null) ? rpcFieldDefinition.getHelpText() : "";//$NON-NLS-1$
		this.editable = ((SimpleRpcFieldDefinition)rpcFieldDefinition).isEditable();
		this.defaultValue = (rpcFieldDefinition.getDefaultValue() != null) ? rpcFieldDefinition.getDefaultValue() : "";//$NON-NLS-1$
		this.expression = StringUtils.isEmpty(rpcFieldDefinition.getExpression()) ? "" : rpcFieldDefinition.getExpression();//$NON-NLS-1$

		this.initialized = true;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RpcFieldModel)) {
			return false;
		}
		RpcFieldModel model = (RpcFieldModel)obj;
		return this.modelName.equals(model.getModelName()) && this.fieldName.equals(model.getFieldName())
				&& this.fieldTypeName.equals(model.getFieldTypeName()) && this.originalName.equals(model.getOriginalName())
				&& (this.key == model.isKey()) && this.direction.equals(model.getDirection())
				&& (this.length == model.getLength()) && (this.fieldType == model.getFieldType())
				&& this.displayName.equals(model.getDisplayName()) && this.sampleValue.equals(model.getSampleValue())
				&& this.helpText.equals(model.getHelpText()) && (this.editable == model.isEditable())
				&& this.defaultValue.equals(model.getDefaultValue()) && StringUtils.equals(expression, model.getExpression());
	}

	@Override
	public RpcFieldModel clone() {
		// when cloning, innerBranchesCount should not be modified in parent
		int count = ((RpcNamedObject)this.parent).getInnerBranchesCount();

		RpcFieldModel model = new RpcFieldModel(this.uuid, (RpcNamedObject)this.parent);
		fillModel(model);
		((RpcNamedObject)this.parent).setInnerBranchesCount(count);
		return model;
	}

	protected void fillModel(RpcFieldModel model) {
		if (model == null) {
			return;
		}
		model.setTreeLevel(this.getTreeLevel());
		model.setTreeBranch(this.getTreeBranch());
		model.setModelName(this.modelName);
		model.setFieldName(this.fieldName);
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setFieldTypeName(this.fieldTypeName);
		model.setOriginalName(this.originalName);
		model.setKey(this.key);
		model.setDirection(this.direction);
		model.setLength(this.length);
		model.setFieldType(this.fieldType);
		model.setDisplayName(this.displayName);
		model.setSampleValue(this.sampleValue);
		model.setHelpText(this.helpText);
		model.setEditable(this.editable);
		model.setDefaultValue(this.defaultValue);
		model.setExpression(this.expression);
		model.initialized = this.initialized;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getJavaTypeName() {
		return javaTypeName;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public String getFieldTypeName() {
		return fieldTypeName;
	}

	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	public String getPreviousFieldName() {
		return previousFieldName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Class<? extends FieldType> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<? extends FieldType> fieldType) {
		this.fieldType = fieldType;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@SuppressWarnings("unchecked")
	public NamedObject convertFrom(NamedObject model) {
		if (!(model instanceof ScreenFieldModel)) {
			return null;
		}
		RpcFieldModel rpcModel = (RpcFieldModel)model;
		// initialized = rpcModel.getFieldValue("initialized") != null ? (Boolean)rpcModel.getFieldValue("initialized") : false;
		// javaTypeName = rpcModel.getFieldValue("javaTypeName") != null ? (String)rpcModel.getFieldValue("javaTypeName")
		// : String.class.getSimpleName();
		previousFieldName = rpcModel.getFieldValue("previousFieldName") != null ? (String)rpcModel.getFieldValue("previousFieldName")
				: Messages.getString("Field.new");
		fieldName = rpcModel.getFieldValue("fieldName") != null ? (String)rpcModel.getFieldValue("fieldName")
				: Messages.getString("Field.new");
		fieldTypeName = rpcModel.getFieldValue("fieldTypeName") != null ? (String)rpcModel.getFieldValue("fieldTypeName")
				: RpcFieldTypes.General.class.getSimpleName();
		originalName = rpcModel.getFieldValue("originalName") != null ? (String)rpcModel.getFieldValue("originalName") : "";
		key = rpcModel.getFieldValue("key") != null ? (Boolean)rpcModel.getFieldValue("key") : false;
		direction = rpcModel.getFieldValue("direction") != null ? (Direction)rpcModel.getFieldValue("direction")
				: Direction.INPUT_OUTPUT;
		length = (Integer)rpcModel.getFieldValue("length");
		fieldType = rpcModel.getFieldValue("fieldType") != null ? (Class<? extends FieldType>)rpcModel.getFieldValue("fieldType")
				: RpcFieldTypes.General.class;
		displayName = rpcModel.getFieldValue("displayName") != null ? (String)rpcModel.getFieldValue("displayName") : "";
		sampleValue = rpcModel.getFieldValue("sampleValue") != null ? (String)rpcModel.getFieldValue("sampleValue") : "";
		helpText = rpcModel.getFieldValue("helpText") != null ? (String)rpcModel.getFieldValue("helpText") : "";
		editable = rpcModel.getFieldValue("editable") != null ? (Boolean)rpcModel.getFieldValue("editable") : false;
		defaultValue = rpcModel.getFieldValue("defaultValue") != null ? (String)rpcModel.getFieldValue("defaultValue") : "";
		expression = rpcModel.getFieldValue("expression") != null ? (String)rpcModel.getFieldValue("expression") : "";
		fieldsMap.putAll(rpcModel.fieldsMap);
		return this;
	}

	private Object getFieldValue(String key) {
		return fieldsMap.get(key);
	}

	@Override
	public void fillFieldsMap() {
		fieldsMap.clear();
		fieldsMap.put("previousFieldName", previousFieldName);
		fieldsMap.put("fieldName", fieldName);
		fieldsMap.put("fieldTypeName", fieldTypeName);
		fieldsMap.put("originalName", originalName);
		fieldsMap.put("key", key);
		fieldsMap.put("direction", direction);
		fieldsMap.put("length", length);
		fieldsMap.put("fieldType", fieldType);
		fieldsMap.put("displayName", displayName);
		fieldsMap.put("sampleValue", sampleValue);
		fieldsMap.put("helpText", helpText);
		fieldsMap.put("editable", editable);
		fieldsMap.put("defaultValue", defaultValue);
		fieldsMap.put("expression", expression);
		fieldsMap.put("javaTypeName", javaTypeName);
		// fieldsMap.put("initialized", initialized);
	}
}
