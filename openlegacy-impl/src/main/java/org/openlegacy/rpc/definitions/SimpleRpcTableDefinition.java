package org.openlegacy.rpc.definitions;

import org.openlegacy.definitions.AbstractTableDefinition;
import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.definitions.TableDefinition.ColumnDefinition;

public class SimpleRpcTableDefinition extends AbstractTableDefinition<ColumnDefinition> implements TableDefinition<ColumnDefinition> {

	private int maxRowsCount;

	public SimpleRpcTableDefinition(String name) {
		super(null);
		setTableEntityName(name);
	}

	private static final long serialVersionUID = 1L;

	public int getMaxRowsCount() {
		return maxRowsCount;
	}

	public void setMaxRowsCount(int maxRowsCount) {
		this.maxRowsCount = maxRowsCount;
	}

	public static class SimpleRpcColumnDefinition implements ColumnDefinition {

		private String name;
		private String displayName;

		public int compareTo(org.openlegacy.definitions.TableDefinition.ColumnDefinition o) {
			return 0;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isKey() {
			return false;
		}

		public boolean isEditable() {
			return false;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getSampleValue() {
			return null;
		}

		public Class<?> getJavaType() {
			return String.class;
		}

		public int getRowsOffset() {
			return 0;
		}

		public String getHelpText() {
			return null;
		}

		public int getColSpan() {
			return 0;
		}

		public int getSortIndex() {
			return 0;
		}

		public Class<?> getTargetEntity() {
			return null;
		}

	}
}
