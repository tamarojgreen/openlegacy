package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.ScreenSizeBean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "snapshot")
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedSnapshot implements TerminalSnapshot {

	@XmlAttribute(name = "type")
	private SnapshotType snapshotType;

	@XmlElement(name = "row", type = TerminalPersistedRow.class)
	private List<TerminalRow> rows = new ArrayList<TerminalRow>();

	@XmlElement(name = "size", type = ScreenSizeBean.class)
	private ScreenSize size;

	public SnapshotType getSnapshotType() {
		return snapshotType;
	}

	public void setSnapshotType(SnapshotType snapshotType) {
		this.snapshotType = snapshotType;
	}

	public List<TerminalRow> getRows() {
		return rows;
	}

	public ScreenSize getSize() {
		return size;
	}

	public void setSize(ScreenSize size) {
		ScreenSizeBean tempSize = new ScreenSizeBean();
		tempSize.setRows(size.getRows());
		tempSize.setColumns(size.getColumns());
		this.size = tempSize;
	}
}
