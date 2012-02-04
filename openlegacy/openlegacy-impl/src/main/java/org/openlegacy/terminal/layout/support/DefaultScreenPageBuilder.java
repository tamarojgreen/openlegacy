package org.openlegacy.terminal.layout.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.definitions.page.support.SimplePagePartDefinition;
import org.openlegacy.definitions.page.support.SimplePagePartRowDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.layout.PagePartDefinition;
import org.openlegacy.layout.PagePartRowDefinition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.layout.ScreenPageBuilder;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultScreenPageBuilder implements ScreenPageBuilder {

	private final static Log logger = LogFactory.getLog(DefaultScreenPageBuilder.class);

	private int maxRowDistanceWithinPart = 1;
	private int maxColumnDistanceWithinPart = 12;
	private int labelFieldDistance = 3;

	public PageDefinition build(ScreenEntityDefinition entityDefinition) {
		Collection<ScreenFieldDefinition> fields = entityDefinition.getFieldsDefinitions().values();

		PageDefinition pageDefinition = new SimplePageDefinition(entityDefinition);

		// sort fields by position
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());

		List<List<ScreenFieldDefinition>> neighourFieldsGroups = groupNeighbourFields(sortedFields);

		for (List<ScreenFieldDefinition> neighbourfields : neighourFieldsGroups) {
			pageDefinition.getPageParts().add(buildPagePart(neighbourfields, entityDefinition));

		}
		return pageDefinition;
	}

	private PagePartDefinition buildPagePart(List<ScreenFieldDefinition> neighbourfields, ScreenEntityDefinition entityDefinition) {
		SimplePagePartDefinition pagePart = new SimplePagePartDefinition();

		calculateMargins(neighbourfields, entityDefinition, pagePart);

		int currentRow = -1;
		PagePartRowDefinition currentPagePartRow = null;
		Set<Integer> columnValues = new HashSet<Integer>();
		ScreenFieldDefinition firstField = neighbourfields.get(0);
		int startColumn = calculateLabelColumn(firstField);
		int endColumn = firstField.getEndPosition().getColumn();
		for (ScreenFieldDefinition screenFieldDefinition : neighbourfields) {
			if (screenFieldDefinition.getPosition().getRow() != currentRow) {
				currentPagePartRow = new SimplePagePartRowDefinition();
				pagePart.getPartRows().add(currentPagePartRow);
				currentRow = screenFieldDefinition.getPosition().getRow();
			}
			currentPagePartRow.getFields().add(screenFieldDefinition);
			columnValues.add(screenFieldDefinition.getPosition().getColumn());
			int fieldEndColumn = screenFieldDefinition.getEndPosition().getColumn();
			if (fieldEndColumn > endColumn) {
				endColumn = fieldEndColumn;
			}
		}
		pagePart.setColumns(columnValues.size());
		int widthPercentage = 100 * (endColumn - startColumn) / entityDefinition.getScreenSize().getColumns();
		pagePart.setWidthPercentage(widthPercentage);
		return pagePart;
	}

	private void calculateMargins(List<ScreenFieldDefinition> neighbourfields, ScreenEntityDefinition entityDefinition,
			SimplePagePartDefinition pagePart) {
		ScreenFieldDefinition firstField = neighbourfields.get(0);
		TerminalPosition firstFieldPosition = firstField.getPosition();

		ScreenSize screenSize = entityDefinition.getScreenSize();
		int topMarginPercentage = (100 * (firstFieldPosition.getRow() - 1)) / screenSize.getRows();
		int topLeftColumn = calculateLabelColumn(firstField);
		int leftMarginPercentage = (100 * topLeftColumn) / screenSize.getColumns();

		pagePart.setTopMarginPercentage(topMarginPercentage);
		pagePart.setLeftMarginPercentage(leftMarginPercentage);
	}

	private int calculateLabelColumn(ScreenFieldDefinition firstField) {
		return firstField.getPosition().getColumn() - (firstField.getDisplayName().length() + labelFieldDistance);
	}

	/**
	 * Group all fields which are close to each other by a given row (1 - default max), or same row with max distance column (10)
	 * 
	 * @param sortedFields
	 * @return
	 */
	private List<List<ScreenFieldDefinition>> groupNeighbourFields(List<ScreenFieldDefinition> sortedFields) {
		List<List<ScreenFieldDefinition>> neighourFieldsGroups = new ArrayList<List<ScreenFieldDefinition>>();

		for (ScreenFieldDefinition screenFieldDefinition : sortedFields) {
			boolean found = false;
			TerminalPosition fieldPosition = screenFieldDefinition.getPosition();
			for (List<ScreenFieldDefinition> neighourFields : neighourFieldsGroups) {
				for (ScreenFieldDefinition neighourField : neighourFields) {
					TerminalPosition neighbourFieldPosition = neighourField.getPosition();
					if (isUnderNighbour(fieldPosition, neighbourFieldPosition)
							|| isRightNeighbour(screenFieldDefinition.getPosition(), neighourField)) {
						logger.debug(MessageFormat.format("Adding field definition {0} to neighbour fields: {1}",
								screenFieldDefinition, neighourFields));
						neighourFields.add(screenFieldDefinition);
						found = true;
						break;
					}
				}

			}
			if (!found) {
				List<ScreenFieldDefinition> neighourFields = new ArrayList<ScreenFieldDefinition>();
				neighourFields.add(screenFieldDefinition);
				neighourFieldsGroups.add(neighourFields);
			}
		}
		return neighourFieldsGroups;
	}

	private boolean isRightNeighbour(TerminalPosition fieldPosition, ScreenFieldDefinition neighourField) {
		TerminalPosition neighbourEndPosition = neighourField.getEndPosition();
		return fieldPosition.getColumn() <= neighbourEndPosition.getColumn() + maxColumnDistanceWithinPart
				&& fieldPosition.getRow() == neighbourEndPosition.getRow();
	}

	private boolean isUnderNighbour(TerminalPosition fieldPosition, TerminalPosition neighbourFieldPosition) {
		return fieldPosition.getColumn() == neighbourFieldPosition.getColumn()
				&& fieldPosition.getRow() - maxRowDistanceWithinPart == neighbourFieldPosition.getRow();
	}

	public void setLabelFieldDistance(int labelFieldDistance) {
		this.labelFieldDistance = labelFieldDistance;
	}

	public void setMaxRowDistanceWithinPart(int maxRowDistanceWithinPart) {
		this.maxRowDistanceWithinPart = maxRowDistanceWithinPart;
	}

	public void setMaxColumnDistanceWithinPart(int maxColumnDistanceWithinPart) {
		this.maxColumnDistanceWithinPart = maxColumnDistanceWithinPart;
	}
}
