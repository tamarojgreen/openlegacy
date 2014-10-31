package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaBooleanFieldModel extends JpaFieldModel {

	public JpaBooleanFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.boolean");//$NON-NLS-1$
	}

	public JpaBooleanFieldModel(UUID uuid, NamedObject parent) {
		super(uuid, parent);
		this.javaTypeName = Messages.getString("type.boolean");//$NON-NLS-1$
	}

	@Override
	public JpaFieldModel clone() {
		JpaBooleanFieldModel model = new JpaBooleanFieldModel(uuid, parent);
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
