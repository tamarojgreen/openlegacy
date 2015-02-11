package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import java.util.UUID;

import org.openlegacy.annotations.screen.ScreenDynamicField;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

public class ScreenDynamicFieldModel extends ScreenNamedObject {

	private String text = "";
	private Integer row = 0;
	private Integer column = 0;
	private Integer endRow = 0;
	private Integer endColumn = 0;
	private Integer fieldOffset = 0;
	
	public ScreenDynamicFieldModel() {
		super(ScreenDynamicField.class.getSimpleName());
	}

	public ScreenDynamicFieldModel(UUID uuid) {
		super(ScreenDynamicField.class.getSimpleName());
		this.uuid = uuid;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public void setEndRow(Integer endRow) {
		this.endRow = endRow;
	}

	public Integer getFieldOffset() {
		return fieldOffset;
	}

	public void setFieldOffset(Integer fieldOffset) {
		this.fieldOffset = fieldOffset;
	}

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		if (screenFieldDefinition == null){
			return;
		}
		
		if (screenFieldDefinition.getDynamicFieldDefinition() == null){
			return;
		}
		this.row = screenFieldDefinition.getDynamicFieldDefinition().getRow();
		this.column = screenFieldDefinition.getDynamicFieldDefinition().getColumn();   
		this.endColumn = screenFieldDefinition.getDynamicFieldDefinition().getEndColumn();
		this.endRow = screenFieldDefinition.getDynamicFieldDefinition().getEndRow();
		this.text = screenFieldDefinition.getDynamicFieldDefinition().getText();
		this.fieldOffset= screenFieldDefinition.getDynamicFieldDefinition().getFieldOffset();
	}

	@Override
	public ScreenDynamicFieldModel clone() {
		ScreenDynamicFieldModel model = new ScreenDynamicFieldModel(this.uuid);
		model.setRow(this.row);
		model.setColumn(this.column);
		model.setEndColumn(this.endColumn);
		model.setEndRow(this.endRow);
		model.setFieldOffset(this.fieldOffset);
		model.setText(this.text);
		return model;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ScreenDynamicFieldModel)) {
			return false;
		}
		ScreenDynamicFieldModel model = (ScreenDynamicFieldModel)obj;
		return this.modelName.equals(model.getModelName()) && this.row == model.getRow() && this.column == model.getColumn()
				&& this.endColumn == model.getEndColumn() && this.endRow == model.getEndRow () && this.text.equals(model.getText()) && this.fieldOffset== model.getFieldOffset();
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public Integer getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(Integer endColumn) {
		this.endColumn = endColumn;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
}
