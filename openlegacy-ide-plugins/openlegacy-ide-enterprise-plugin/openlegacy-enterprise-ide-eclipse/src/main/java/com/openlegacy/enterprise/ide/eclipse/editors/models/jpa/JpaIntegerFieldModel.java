package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaIntegerFieldModel extends JpaFieldModel {

	public JpaIntegerFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.integer");//$NON-NLS-1$
	}

	public JpaIntegerFieldModel(UUID uuid, NamedObject parent) {
		super(uuid, parent);
		this.javaTypeName = Messages.getString("type.integer");//$NON-NLS-1$
	}

	@Override
	public JpaFieldModel clone() {
		JpaIntegerFieldModel model = new JpaIntegerFieldModel(uuid, parent);
		model.setModelName(this.getModelName());
		model.setFieldName(this.getFieldName());
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setName(getName());
		model.setUnique(isUnique());
		model.setNullable(isNullable());
		model.setInsertable(isInsertable());
		model.setUpdatable(isUpdatable());
		model.setColumnDefinition(getColumnDefinition());
		model.setTable(getTable());
		model.setLength(getLength());
		model.setPrecision(getPrecision());
		model.setScale(getScale());
		model.setKey(isKey());
		model.initialized = isInitialized();
		return model;
	}

}
