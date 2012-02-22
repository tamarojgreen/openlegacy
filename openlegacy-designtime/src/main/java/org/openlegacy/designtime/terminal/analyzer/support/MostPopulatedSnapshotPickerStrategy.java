package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.FieldFormatter;
import org.openlegacy.designtime.terminal.analyzer.SnapshotPickerStrategy;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;
import org.openlegacy.utils.StringUtil;

import java.util.Collection;
import java.util.Set;

public class MostPopulatedSnapshotPickerStrategy implements SnapshotPickerStrategy<TerminalSnapshot> {

	private FieldFormatter fieldFormatter;

	public MostPopulatedSnapshotPickerStrategy(FieldFormatter fieldFormatter) {
		this.fieldFormatter = fieldFormatter;
	}

	public TerminalSnapshot pickRepresenter(Set<TerminalSnapshot> group) {
		TerminalSnapshot[] snapshots = group.toArray(new TerminalSnapshot[group.size()]);

		int maxScore = 0;
		TerminalSnapshot mostPoulatedSnapshot = null;

		for (TerminalSnapshot terminalSnapshot : snapshots) {

			// pick only incoming snapshot as representer
			if (terminalSnapshot.getSnapshotType() == SnapshotType.OUTGOING) {
				continue;
			}

			int score = 0;
			Collection<TerminalField> fields = terminalSnapshot.getFields();
			for (TerminalField field : fields) {
				String value = fieldFormatter.format(field.getValue());
				score += StringUtil.getLength(value);
			}
			if (score > maxScore) {
				maxScore = score;
				mostPoulatedSnapshot = terminalSnapshot;
			}
		}
		return mostPoulatedSnapshot;
	}

}
