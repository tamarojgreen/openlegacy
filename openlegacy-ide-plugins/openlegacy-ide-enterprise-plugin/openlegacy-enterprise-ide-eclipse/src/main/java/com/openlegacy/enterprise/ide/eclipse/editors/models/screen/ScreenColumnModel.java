package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenColumnDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenColumnModel extends ScreenNamedObject {

	// annotation attributes
	private boolean key = false;
	private boolean selectionField = false;
	private boolean mainDisplayField = false;
	private int startColumn;
	private int endColumn;
	private boolean editable = false;
	private String displayName = "";
	private String sampleValue = "";
	private int rowsOffset = 0;
	private String helpText = "";
	private int colSpan = 1;
	private int sortIndex = -1;
	private FieldAttributeType attribute = FieldAttributeType.Value;
	private Class<?> targetEntity = ScreenEntity.NONE.class;
	private String targetEntityClassName = ScreenEntity.NONE.class.getSimpleName();
	private String expression = "";

	// other
	private String defaultValue = "";

	private String fieldName = Messages.getString("Field.new");//$NON-NLS-1$
	private String previousFieldName = Messages.getString("Field.new");//$NON-NLS-1$
	private String javaTypeName = String.class.getSimpleName();

	private boolean initialized = false;

	public ScreenColumnModel(NamedObject parent) {
		super(ScreenColumn.class.getSimpleName());
		this.parent = parent;
	}

	public ScreenColumnModel(UUID modelUUID, NamedObject parent) {
		super(ScreenColumn.class.getSimpleName());
		this.uuid = modelUUID;
		this.parent = parent;
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Use
	 * <code>init(SimpleScreenColumnDefinition columnDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Use init(SimpleScreenColumnDefinition columnDefinition) instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Use
	 * <code>init(SimpleScreenColumnDefinition columnDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Use init(SimpleScreenColumnDefinition columnDefinition) instead.");//$NON-NLS-1$
	}

	/**
	 * @param columnDefinition
	 */
	public void init(SimpleScreenColumnDefinition columnDefinition) {
		this.fieldName = columnDefinition.getName();
		this.previousFieldName = this.fieldName;
		this.javaTypeName = columnDefinition.getJavaTypeName();

		this.key = columnDefinition.isKey();
		this.selectionField = columnDefinition.isSelectionField();
		this.mainDisplayField = columnDefinition.isMainDisplayField();
		this.startColumn = columnDefinition.getStartColumn();
		this.endColumn = columnDefinition.getEndColumn();
		this.editable = columnDefinition.isEditable();
		this.displayName = columnDefinition.getDisplayName();
		this.sampleValue = columnDefinition.getSampleValue();
		this.rowsOffset = columnDefinition.getRowsOffset();
		this.helpText = columnDefinition.getHelpText();
		this.colSpan = columnDefinition.getColSpan();
		this.sortIndex = columnDefinition.getSortIndex();
		this.attribute = columnDefinition.getAttribute();
		this.targetEntityClassName = columnDefinition.getTargetEntityClassName();
		this.expression = columnDefinition.getExpression();
		this.initialized = true;
	}

	@Override
	public ScreenColumnModel clone() {
		ScreenColumnModel model = new ScreenColumnModel(this.uuid, this.parent);
		model.setModelName(this.modelName);
		model.setKey(this.key);
		model.setSelectionField(this.selectionField);
		model.setMainDisplayField(this.mainDisplayField);
		model.setStartColumn(this.startColumn);
		model.setEndColumn(this.endColumn);
		model.setEditable(this.editable);
		model.setDisplayName(this.displayName);
		model.setSampleValue(this.sampleValue);
		model.setRowsOffset(this.rowsOffset);
		model.setFieldName(this.fieldName);
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setHelpText(this.helpText);
		model.setColSpan(this.colSpan);
		model.setSortIndex(this.sortIndex);
		model.setAttribute(this.attribute);
		model.setTargetEntityClassName(this.targetEntityClassName);
		model.setExpression(this.expression);
		model.initialized = this.initialized;
		return model;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isSelectionField() {
		return selectionField;
	}

	public void setSelectionField(boolean selectionField) {
		this.selectionField = selectionField;
	}

	public boolean isMainDisplayField() {
		return mainDisplayField;
	}

	public void setMainDisplayField(boolean mainDisplayField) {
		this.mainDisplayField = mainDisplayField;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
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

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getRowsOffset() {
		return rowsOffset;
	}

	public void setRowsOffset(int rowsOffset) {
		this.rowsOffset = rowsOffset;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getPreviousFieldName() {
		return previousFieldName;
	}

	public String getJavaTypeName() {
		return javaTypeName;
	}

	public void setJavaTypeName(String name) {
		this.javaTypeName = name;
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public FieldAttributeType getAttribute() {
		return attribute;
	}

	public void setAttribute(FieldAttributeType attribute) {
		this.attribute = attribute;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

	public void setAttributeDefaultValue() {
		attribute = FieldAttributeType.Value;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean isInitialized() {
		return initialized;
	}

}
