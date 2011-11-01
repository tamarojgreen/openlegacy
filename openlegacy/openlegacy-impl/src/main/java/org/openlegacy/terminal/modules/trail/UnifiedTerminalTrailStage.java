package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.TrailStage;
import org.openlegacy.terminal.TerminalRow;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class UnifiedTerminalTrailStage implements TrailStage {

	@XmlAttribute(name = "type")
	private StageType stageType;

	@XmlElement(name = "row", type = TerminalTrailRow.class)
	private List<TerminalRow> rows = new ArrayList<TerminalRow>();

	public StageType getStageType() {
		return stageType;
	}

	public void setStageType(StageType stageType) {
		this.stageType = stageType;
	}

	public List<TerminalRow> getRows() {
		return rows;
	}
}
