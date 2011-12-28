package org.openlegacy.terminal.support;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.TerminalSnapshotTextRenderer;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.io.Serializable;

public abstract class AbstractSnapshot implements TerminalSnapshot, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TerminalSnapshotTextRenderer.instance().render(this, baos);
		return new String(baos.toByteArray());
	}

	@Override
	public int hashCode() {
		// return HashCodeBuilder.reflectionHashCode(this);
		return TerminalEqualsHashcodeUtil.snapshotHashcode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalSnapshot)) {
			return false;
		}
		return TerminalEqualsHashcodeUtil.snapshotsEquals(this, (TerminalSnapshot)obj);
	}

}
