package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.annotations.screen.PartPosition;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class PartPositionModel extends ScreenNamedObject {

	private static final int DEFAULT_ROW = 0;
	private static final int DEFAULT_COLUMN = 0;
	private static final int DEFAULT_WIDTH = 80;

	// annotation attributes
	private int row = 0;
	private int column = 0;
	private int width = 80;

	// other
	private String className = "";

	public PartPositionModel(NamedObject parent) {
		super(PartPosition.class.getSimpleName());
		this.parent = parent;
	}

	public PartPositionModel(UUID uuid, NamedObject parent) {
		super(PartPosition.class.getSimpleName());
		this.uuid = uuid;
		this.parent = parent;
	}

	@Override
	public void init(CodeBasedScreenPartDefinition partDefinition) {
		this.row = partDefinition.getPartPosition().getRow();
		this.column = partDefinition.getPartPosition().getColumn();
		this.width = partDefinition.getWidth();

		this.className = partDefinition.getClassName();
	}

	/**
	 * Method <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> is not supported for this model. Use
	 * <code>init(CodeBasedScreenPartDefinition partDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedScreenEntityDefinition entityDefinition) is not supported for this model. Use init(CodeBasedScreenPartDefinition partDefinition) instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Use
	 * <code>init(CodeBasedScreenPartDefinition partDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Use init(CodeBasedScreenPartDefinition partDefinition) instead.");//$NON-NLS-1$
	}

	@Override
	public PartPositionModel clone() {
		PartPositionModel model = new PartPositionModel(this.uuid, this.parent);
		model.setColumn(this.column);
		model.setModelName(this.modelName);
		model.setRow(this.row);
		model.setWidth(this.width);
		model.className = this.className;
		return model;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PartPositionModel)) {
			return false;
		}
		PartPositionModel model = (PartPositionModel)o;
		return (model.getRow() == this.row) && (model.getColumn() == this.column) && (model.getWidth() == this.width);
	}

	public boolean isDefaultValues() {
		return (this.row == DEFAULT_ROW) && (this.column == DEFAULT_COLUMN) && (this.width == DEFAULT_WIDTH);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getClassName() {
		return className;
	}

}
