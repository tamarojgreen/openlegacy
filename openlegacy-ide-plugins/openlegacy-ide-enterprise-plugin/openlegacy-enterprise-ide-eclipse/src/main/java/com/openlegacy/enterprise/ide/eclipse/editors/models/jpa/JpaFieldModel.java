package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;

import java.util.UUID;

import javax.persistence.Column;

/**
 * Represents @Column annotation. As default field has a String type.
 * 
 * @author Ivan Bort
 * 
 */
public class JpaFieldModel extends JpaNamedObject {

	// annotation attributes
	private String name = "";
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updatable = true;
	private String columnDefinition = "";
	private String table = "";
	private int length = 255;
	private int precision = 0;
	private int scale = 0;
	// @Id annotation
	private boolean key = false;

	// other
	private String fieldName = Messages.getString("Field.new");//$NON-NLS-1$
	protected String javaTypeName = String.class.getSimpleName();
	protected String previousFieldName = Messages.getString("Field.new");//$NON-NLS-1$
	protected boolean initialized = false;

	public JpaFieldModel(NamedObject parent) {
		super(Column.class.getSimpleName());
		this.parent = parent;
	}

	public JpaFieldModel(UUID uuid, NamedObject parent) {
		super(Column.class.getSimpleName());
		this.uuid = uuid;
		this.parent = parent;
	}

	@Override
	public void init(DbFieldDefinition dbFieldDefinition) {
		if (dbFieldDefinition == null) {
			return;
		}
		fieldName = Utils.getFieldNameWithoutPrefix(dbFieldDefinition.getName());
		previousFieldName = fieldName;
		key = dbFieldDefinition.isKey();
		if (dbFieldDefinition instanceof SimpleDbColumnFieldDefinition) {
			SimpleDbColumnFieldDefinition definition = (SimpleDbColumnFieldDefinition)dbFieldDefinition;
			javaTypeName = definition.getJavaTypeName();

			name = definition.getNameAttr();
			unique = definition.isUnique();
			nullable = definition.isNullable();
			insertable = definition.isInsertable();
			updatable = definition.isUpdatable();
			columnDefinition = definition.getColumnDefinition();
			table = definition.getTable();
			length = definition.getLength();
			precision = definition.getPrecision();
			scale = definition.getScale();
		}
		initialized = true;
	}

	@Override
	public JpaFieldModel clone() {
		JpaFieldModel model = new JpaFieldModel(uuid, parent);
		model.setModelName(this.getModelName());
		model.setFieldName(this.getFieldName());
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setName(name);
		model.setUnique(unique);
		model.setNullable(nullable);
		model.setInsertable(insertable);
		model.setUpdatable(updatable);
		model.setColumnDefinition(columnDefinition);
		model.setTable(table);
		model.setLength(length);
		model.setPrecision(precision);
		model.setScale(scale);
		model.setKey(key);
		model.initialized = initialized;
		return model;
	}

	public boolean isDefaultModel() {
		return StringUtils.isEmpty(name) && !unique && nullable && insertable && updatable
				&& StringUtils.isEmpty(columnDefinition) && StringUtils.isEmpty(table) && (length == 255) && (precision == 0)
				&& (scale == 0);
	}

	public boolean isModelEqual(JpaFieldModel model) {
		return StringUtils.equals(name, model.getName()) && unique == model.isUnique() && nullable == model.isNullable()
				&& insertable == model.isInsertable() && updatable == model.isUpdatable()
				&& StringUtils.equals(columnDefinition, model.getColumnDefinition())
				&& StringUtils.equals(table, model.getTable()) && length == model.getLength()
				&& precision == model.getPrecision() && scale == model.getScale();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public String getColumnDefinition() {
		return columnDefinition;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
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

	public String getPreviousFieldName() {
		return previousFieldName;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isInitialized() {
		return initialized;
	}

}
