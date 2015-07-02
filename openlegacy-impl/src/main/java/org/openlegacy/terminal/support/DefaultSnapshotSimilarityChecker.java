/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.openlegacy.SnapshotsSimilarityChecker;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.List;

/**
 * Terminal snapshot similarity checker. Check how much of the "space" on of the snapshot are similar in percentage.
 * 
 */
public class DefaultSnapshotSimilarityChecker implements SnapshotsSimilarityChecker<TerminalSnapshot> {

	private double charDifferenceFactor = 0.15;

	@Override
	public int similarityPercent(TerminalSnapshot snapshot1, TerminalSnapshot snapshot2) {
		if (snapshot1 == null || snapshot2 == null) {
			return 0;
		}

		ScreenSize size = snapshot1.getSize();
		// start from 100% - the full screen size
		int screenSize = size.getRows() * size.getColumns();

		double totalScore = screenSize;

		for (int i = 1; i <= size.getRows(); i++) {
			TerminalRow row1 = snapshot1.getRow(i);
			TerminalRow row2 = snapshot2.getRow(i);

			double rowSpaceMatch = rowSpaceSimilarity(row1, row2, size.getColumns());
			// reduce from the total
			double matchedAttributesScore = size.getColumns() - rowSpaceMatch;
			totalScore = totalScore - matchedAttributesScore;
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
	private double rowSpaceSimilarity(TerminalRow row1, TerminalRow row2, int rowLength) {
		List<TerminalField> fields1 = row1.getFields();
		double rowSpaceMatch = rowLength;
		if (fields1.size() == 0 && row2.getFields().size() > 0) {
//			 return 0;
		}
		for (TerminalField field : fields1) {
			TerminalField secondField = row2.getField(field.getPosition().getColumn());
			if (secondField == null) {
				rowSpaceMatch = rowSpaceMatch - field.getLength();
			} else {
				double fieldSimilarity = fieldDifference(field, secondField);
				rowSpaceMatch = rowSpaceMatch - fieldSimilarity;
			}

		}
		return rowSpaceMatch;
	}

	public double fieldDifference(TerminalField field1, TerminalField field2) {
		boolean equals = new EqualsBuilder().append(field1.getPosition(), field2.getPosition()).append(field1.getLength(),
				field2.getLength()).append(field1.isEditable(), field2.isEditable()).isEquals();
		if (!equals) {
			return field1.getLength();
		}
		if (field1.isEditable()) {
			return 0;
		} else {
			char[] field1Chars = field1.getValue().toCharArray();
			char[] field2Chars = field2.getValue().toCharArray();
			int fieldTextDiff = 0;
			for (int ii = 0; ii < field1Chars.length; ii++) {
				if (field2Chars.length <= ii || (field1Chars[ii] == ' ' && field1Chars[ii] != field2Chars[ii])) {
					fieldTextDiff += charDifferenceFactor;
				}
			}
			return fieldTextDiff;
		}
	}

	public void setCharDifferenceFactor(double charDifferenceFactor) {
		this.charDifferenceFactor = charDifferenceFactor;
	}
}
