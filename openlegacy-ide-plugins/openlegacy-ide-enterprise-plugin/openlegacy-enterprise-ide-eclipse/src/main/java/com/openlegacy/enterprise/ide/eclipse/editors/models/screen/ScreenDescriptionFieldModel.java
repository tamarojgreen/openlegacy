package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import org.openlegacy.annotations.screen.ScreenDescriptionField;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.UUID;

public class ScreenDescriptionFieldModel extends ScreenNamedObject {

	private Integer row = 0;
	private Integer column = 0;
	private Integer endColumn = 0;

	public ScreenDescriptionFieldModel() {
		super(ScreenDescriptionField.class.getSimpleName());
	}

	public ScreenDescriptionFieldModel(UUID uuid) {
		super(ScreenDescriptionField.class.getSimpleName());
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

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		if (screenFieldDefinition != null) {
			this.row = screenFieldDefinition.getPosition().getRow();
			this.column = screenFieldDefinition.getPosition().getColumn();
			this.endColumn = screenFieldDefinition.getEndPosition().getColumn();
		}
	}

	@Override
	public ScreenDescriptionFieldModel clone() {
		ScreenDescriptionFieldModel model = new ScreenDescriptionFieldModel(this.uuid);
		model.setRow(this.row);
		model.setColumn(this.column);
		model.setEndColumn(this.endColumn);
		return model;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ScreenDescriptionFieldModel)) {
			return false;
		}
		ScreenDescriptionFieldModel model = (ScreenDescriptionFieldModel)obj;
		return this.modelName.equals(model.getModelName()) && this.row == model.getRow() && this.column == model.getColumn()
				&& this.endColumn == model.getEndColumn();
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
