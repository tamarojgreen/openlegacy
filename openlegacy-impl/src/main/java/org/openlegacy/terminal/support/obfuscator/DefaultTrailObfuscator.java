package org.openlegacy.terminal.support.obfuscator;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;
import org.openlegacy.terminal.TrailObfuscator;
import org.openlegacy.terminal.module.TerminalSessionTrail;

import java.util.List;

public class DefaultTrailObfuscator implements TrailObfuscator {

	private List<String> regExps;

	private List<CharObfuscator> charObfuscators;

	@Override
	public void obfuscate(TerminalSessionTrail trail) {
		List<TerminalSnapshot> snahpshots = trail.getSnapshots();
		TerminalSnapshot lastIncomingSnapshot = null;
		for (TerminalSnapshot terminalSnapshot : snahpshots) {
			List<TerminalRow> rows = terminalSnapshot.getRows();
			for (TerminalRow terminalRow : rows) {
				List<TerminalField> fields = terminalRow.getFields();
				for (TerminalField terminalField : fields) {
					if (terminalSnapshot.getSnapshotType() == SnapshotType.OUTGOING) {
						// do not blend typed content
						if (terminalField.isModified()) {
							continue;
						} else {
							// assign same value to outgoing as incoming
							if (lastIncomingSnapshot != null) {
								TerminalField incomingField = lastIncomingSnapshot.getField(terminalField.getPosition());
								if (incomingField != null) {
									terminalField.setValue(incomingField.getValue());
								}
							}
						}
					} else {
						mixValue(terminalField);
					}

				}
			}
			if (terminalSnapshot.getSnapshotType() == SnapshotType.INCOMING) {
				lastIncomingSnapshot = terminalSnapshot;
			}
		}
	}

	private void mixValue(TerminalField terminalField) {
		String value = terminalField.getValue();
		boolean found = false;
		// always mix editable fields
		if (!terminalField.isEditable()) {
			for (String regexToMix : regExps) {
				if (value.matches(regexToMix)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return;
			}
		}
		StringBuilder newValue = new StringBuilder(terminalField.getValue());
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			for (CharObfuscator charMixer : charObfuscators) {
				if (charMixer.isInRange(ch)) {
					newValue.setCharAt(i, charMixer.obfuscate(ch));
				}
			}
		}
		terminalField.setValue(newValue.toString());
	}

	public void setRegExps(List<String> regExps) {
		this.regExps = regExps;
	}

	public void setCharObfuscators(List<CharObfuscator> charObfuscators) {
		this.charObfuscators = charObfuscators;
	}
}
