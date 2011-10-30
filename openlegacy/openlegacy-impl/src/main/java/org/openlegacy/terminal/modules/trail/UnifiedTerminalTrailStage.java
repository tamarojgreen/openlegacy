package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.TrailStage;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "terminal-stage")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnifiedTerminalTrailStage implements TrailStage {

	private StageType stageType;

	@XmlElement(name = "row")
	private List<TerminalTrailRow> rows = new ArrayList<TerminalTrailRow>();

	public StageType getStageType() {
		return stageType;
	}

	public void setStageType(StageType stageType) {
		this.stageType = stageType;
	}

	public List<TerminalTrailRow> getRows() {
		return rows;
	}
}
