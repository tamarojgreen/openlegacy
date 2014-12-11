package org.openlegacy.db.definitions;

import org.openlegacy.FieldType;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.support.AbstractFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class SimpleDbColumnFieldDefinition extends AbstractFieldDefinition<DbFieldDefinition> implements DbFieldDefinition {

	private static final long serialVersionUID = 1L;

	// @Column annotation attributes
	private String nameAttr = "";
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updatable = true;
	private String columnDefinition = "";
	private String table = "";
	private int length = 255;
	private int precision = 0;
	private int scale = 0;
	// @DbColumn annotation attributes
	private boolean mainDisplayField = false;

	private DbOneToManyDefinition oneToManyDefinition = null;
	private String fieldTypeArgs = null;

	public SimpleDbColumnFieldDefinition(String name, Class<? extends FieldType> type) {
		super(name, type);
	}

	public SimpleDbColumnFieldDefinition(String name, String fieldTypeArgs) {
		super(name, null);
		this.fieldTypeArgs = fieldTypeArgs;
	}

	@Override
	public int compareTo(FieldDefinition o) {
		return 0;
	}

	public String getNameAttr() {
		return nameAttr;
	}

	public void setNameAttr(String nameAttr) {
		this.nameAttr = nameAttr;
	}

	@Override
	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	@Override
	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	@Override
	public String getColumnDefinition() {
		return columnDefinition;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	@Override
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@Override
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	@Override
	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	@Override
	public DbOneToManyDefinition getOneToManyDefinition() {
		return oneToManyDefinition;
	}

	public void setOneToManyDefinition(DbOneToManyDefinition OneToManyDefinition) {
		this.oneToManyDefinition = OneToManyDefinition;
	}

	public String getFieldTypeArgs() {
		return fieldTypeArgs;
	}

	@Override
	public String getExpression() {
		return null;
	}

	@Override
	public int getKeyIndex() {
		return 1;
	}

	@Override
	public boolean isMainDisplayField() {
		return mainDisplayField;
	}

	public void setMainDisplayField(boolean mainDisplayField) {
		this.mainDisplayField = mainDisplayField;
	}
}
