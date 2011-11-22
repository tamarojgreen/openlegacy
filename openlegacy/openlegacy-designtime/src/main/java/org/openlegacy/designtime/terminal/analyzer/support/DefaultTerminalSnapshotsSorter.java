package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.FieldFormatter;
import org.openlegacy.designtime.analyzer.support.AbstractSnapshotsSorter;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.utils.StringUtil;

import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

public class DefaultTerminalSnapshotsSorter extends AbstractSnapshotsSorter<TerminalSnapshot> {

	@Inject
	private FieldFormatter fieldFormatter;

	@Override
	/**
	 * Pick the snapshot which has the longest field values
	 */
	protected TerminalSnapshot pickRepresenter(Set<TerminalSnapshot> group) {
		TerminalSnapshot[] snapshots = group.toArray(new TerminalSnapshot[group.size()]);

		int maxScore = 0;
		TerminalSnapshot mostPoulatedSnapshot = null;

		for (TerminalSnapshot terminalSnapshot : snapshots) {

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
