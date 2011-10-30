package org.openlegacy.terminal.modules.trail;

import org.apache.commons.beanutils.BeanUtils;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.TrailStage;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.ScreenPositionBean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "terminal-trail")
public class UnifiedTerminalTrail implements SessionTrail {

	@XmlElement(name = "stage", type = UnifiedTerminalTrail.class)
	private List<TrailStage> stages = new ArrayList<TrailStage>();

	public List<TrailStage> getStages() {
		return stages;
	}

	public void appendStage(TrailStage stage) {
		stages.add(transformStage(stage));
	}

	private static UnifiedTerminalTrailStage transformStage(TrailStage stage) {
		try {
			if (stage instanceof TerminalIncomingTrailStage) {
				return transformIncomingStage((TerminalIncomingTrailStage)stage);
			}
			if (stage instanceof TerminalOutgoingTrailStage) {
				return transformOutgoingStage((TerminalOutgoingTrailStage)stage);
			}
		} catch (IllegalAccessException e) {
			throw (new IllegalStateException(e));
		} catch (InvocationTargetException e) {
			throw (new IllegalStateException(e));
		}
		throw (new IllegalStateException("Incorrect TerminalStage:" + stage.getClass()
				+ ". Only TerminalIncomingTrailStage, TerminalOutgoingTrailStage are allowed"));

	}

	private static UnifiedTerminalTrailStage transformIncomingStage(TerminalIncomingTrailStage stage)
			throws IllegalAccessException, InvocationTargetException {
		UnifiedTerminalTrailStage unifiedStage = new UnifiedTerminalTrailStage();
		unifiedStage.setStageType(stage.getStageType());

		TerminalScreen screen = stage.getTerminalScreen();

		transformCommonStage(unifiedStage, screen);

		return unifiedStage;
	}

	private static TrailStage transformCommonStage(UnifiedTerminalTrailStage unifiedStage, TerminalScreen screen)
			throws IllegalAccessException, InvocationTargetException {

		List<TerminalRow> rows = screen.getRows();
		for (TerminalRow terminalRow : rows) {
			TerminalTrailRow terminalTrailRow = new TerminalTrailRow();
			terminalTrailRow.setRowNumber(terminalRow.getRowNumber());

			List<TerminalField> fields = terminalRow.getFields();
			for (TerminalField terminalField : fields) {
				TerminalTrailField terminalTrailField = new TerminalTrailField();
				BeanUtils.copyProperties(terminalTrailField, terminalField);
				terminalTrailField.setScreenPosition(ScreenPositionBean.newInstance(terminalField.getPosition()));
				terminalTrailRow.getFields().add(terminalTrailField);
			}
			unifiedStage.getRows().add(terminalTrailRow);
		}
		return unifiedStage;
	}

	private static UnifiedTerminalTrailStage transformOutgoingStage(TerminalOutgoingTrailStage stage)
			throws IllegalAccessException, InvocationTargetException {
		UnifiedTerminalTrailStage unifiedStage = new UnifiedTerminalTrailStage();
		unifiedStage.setStageType(stage.getStageType());

		TerminalScreen screen = stage.getTerminalScreen();

		transformCommonStage(unifiedStage, screen);

		TerminalSendAction sendAction = stage.getTerminalSendAction();
		Map<ScreenPosition, String> fields = sendAction.getFields();
		Set<ScreenPosition> fieldPositions = fields.keySet();
		for (ScreenPosition fieldPosition : fieldPositions) {
			TerminalTrailRow row = unifiedStage.getRows().get(fieldPosition.getRow() - 1);
			TerminalTrailField field = (TerminalTrailField)row.getField(fieldPosition.getColumn());
			field.setValue(fields.get(fieldPosition));
			field.setModified(true);
		}
		return unifiedStage;
	}

}
