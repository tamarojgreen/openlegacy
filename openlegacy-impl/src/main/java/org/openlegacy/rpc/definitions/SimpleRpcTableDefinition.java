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

	@Override
	public int getMaxRowsCount() {
		return maxRowsCount;
	}

	public void setMaxRowsCount(int maxRowsCount) {
		this.maxRowsCount = maxRowsCount;
	}

	public static class SimpleRpcColumnDefinition implements ColumnDefinition {

		private String name;
		private String displayName;
		private String sampleValue;

		@Override
		public int compareTo(org.openlegacy.definitions.TableDefinition.ColumnDefinition o) {
			return 0;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public boolean isKey() {
			return false;
		}

		@Override
		public boolean isEditable() {
			return false;
		}

		@Override
		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		@Override
		public String getSampleValue() {
			return sampleValue;
		}

		public void setSampleValue(String sampleValue) {
			this.sampleValue = sampleValue;
		}

		@Override
		public Class<?> getJavaType() {
			return String.class;
		}

		@Override
		public int getRowsOffset() {
			return 0;
		}

		@Override
		public String getHelpText() {
			return null;
		}

		@Override
		public int getColSpan() {
			return 0;
		}

		@Override
		public int getSortIndex() {
			return 0;
		}

		@Override
		public Class<?> getTargetEntity() {
			return null;
		}

	}
}
