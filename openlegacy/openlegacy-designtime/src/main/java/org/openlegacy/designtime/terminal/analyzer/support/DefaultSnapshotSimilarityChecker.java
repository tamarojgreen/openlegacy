package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.openlegacy.designtime.analyzer.SnapshotsSimilarityChecker;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Terminal snapshot similarity checker. Check how much of the "space" on of the snapshot are similar in percentage.
 * 
 */
@Component
public class DefaultSnapshotSimilarityChecker implements SnapshotsSimilarityChecker<TerminalSnapshot> {

	public int similarityPercent(TerminalSnapshot snapshot1, TerminalSnapshot snapshot2) {
		ScreenSize size = snapshot1.getSize();
		// start from 100% - the full screen size
		int screenSize = size.getRows() * size.getColumns();

		double totalScore = screenSize;
		// some
		for (int i = 1; i <= size.getRows(); i++) {
			TerminalRow row1 = snapshot1.getRowByRowNumber(i);
			TerminalRow row2 = snapshot2.getRowByRowNumber(i);
			int rowSpaceMatch = rowSpaceSimilarity(row1, row2, size.getColumns());
			// reduce from the total
			totalScore = totalScore - (size.getColumns() - rowSpaceMatch);
		}
		int percentageMatch = (int)(100 * (totalScore / screenSize));
		return percentageMatch;
	}

	/**
	 * calculates the matches row size of 2 given rows. Start from the entire row length (100%) and reduce the length of non
	 * matched fields
	 * 
	 * @param row1
	 * @param row2
	 * @param rowLength
	 * @return the row space which is match between the 2 rows
	 */
	private static int rowSpaceSimilarity(TerminalRow row1, TerminalRow row2, int rowLength) {
		List<TerminalField> fields1 = row1.getFields();
		int rowSpaceMatch = rowLength;
		for (TerminalField field : fields1) {
			TerminalField secondField = row2.getField(field.getPosition().getColumn());
			if (secondField == null || !fieldSimilar(field, secondField)) {
				rowSpaceMatch = rowSpaceMatch - field.getLength();
			}
		}
		return rowSpaceMatch;
	}

	public static boolean fieldSimilar(TerminalField field1, TerminalField field2) {
		boolean equals = new EqualsBuilder().append(field1.getPosition(), field2.getPosition()).append(field1.getLength(),
				field2.getLength()).append(field1.isEditable(), field2.isEditable()).isEquals();
		return equals;
	}

}
