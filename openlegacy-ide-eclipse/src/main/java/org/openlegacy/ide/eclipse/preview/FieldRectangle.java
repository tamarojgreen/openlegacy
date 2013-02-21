package org.openlegacy.ide.eclipse.preview;

/**
 * Contains position information of field in terminal and field value.
 * 
 * @author Ivan Bort
 * 
 */
public class FieldRectangle {

	private int row;
	private int endRow;
	private int column;
	private int endColumn;
	private String value;

	/**
	 * 
	 */
	public FieldRectangle(int row, int endRow, int column, int endColumn, String value) {
		this.row = row;
		this.endRow = endRow;
		this.column = column;
		this.endColumn = endColumn;
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public int getEndRow() {
		return endRow;
	}

	public int getColumn() {
		return column;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public String getValue() {
		return value;
	}

}
