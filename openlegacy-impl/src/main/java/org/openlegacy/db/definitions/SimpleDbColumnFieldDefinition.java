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
	private DbOneToManyDefinition oneToManyDefinition = null;
	private String fieldTypeArgs = null;

	public SimpleDbColumnFieldDefinition(String name, Class<? extends FieldType> type) {
		super(name, type);
	}

	public SimpleDbColumnFieldDefinition(String name, String fieldTypeArgs) {
		super(name, null);
		this.fieldTypeArgs = fieldTypeArgs;
	}

	public int compareTo(FieldDefinition o) {
		return 0;
	}

	public String getNameAttr() {
		return nameAttr;
	}

	public void setNameAttr(String nameAttr) {
		this.nameAttr = nameAttr;
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

	public DbOneToManyDefinition getOneToManyDefinition() {
		return oneToManyDefinition;
	}

	public void setOneToManyDefinition(DbOneToManyDefinition OneToManyDefinition) {
		this.oneToManyDefinition = OneToManyDefinition;
	}

	public String getFieldTypeArgs() {
		return fieldTypeArgs;
	}

	public String getExpression() {
		return null;
	}
	
	public int getKeyIndex() {
		return 1;
	}
}
