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
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.layout.ScreenPageBuilder;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default screen page builder create a page which is combined from page part
 * 
 */
public class DefaultScreenPageBuilder implements ScreenPageBuilder {

	private final static Log logger = LogFactory.getLog(DefaultScreenPageBuilder.class);

	private int maxRowDistanceWithinPart = 1;
	private int maxColumnDistanceWithinPart = 12;
	private int labelFieldDistance = 3;

	// used for setting default field length when none specified (@ScreenField doesn't force endColumn/length)
	private int defaultFieldLength = 10;

	private int defaultLeftMarginOffset = 10;
	private int defaultTopMarginOffset = 0;

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

		Collection<ScreenPartEntityDefinition> screenParts = entityDefinition.getPartsDefinitions().values();
		for (ScreenPartEntityDefinition screenPartEntityDefinition : screenParts) {
			pageDefinition.getPageParts().add(buildPagePartFromScreenPart(screenPartEntityDefinition, entityDefinition));
		}

		Collection<ScreenTableDefinition> tables = entityDefinition.getTableDefinitions().values();
		for (ScreenTableDefinition tableDefinition : tables) {
			pageDefinition.getPageParts().add(buildPagePartFromTable(tableDefinition, entityDefinition));
		}
		return pageDefinition;
	}

	private PagePartDefinition buildPagePartFromTable(ScreenTableDefinition tableDefinition,
			ScreenEntityDefinition entityDefinition) {
		SimplePagePartDefinition pagePart = new SimplePagePartDefinition();

		List<ScreenColumnDefinition> columns = tableDefinition.getColumnDefinitions();

		int tableStartColumn = columns.get(0).getStartColumn();
		int tableEndColumn = columns.get(columns.size() - 1).getEndColumn();
		int firstRow = tableDefinition.getStartRow() - 1; // -1 -> header

		ScreenSize screenSize = entityDefinition.getScreenSize();
		int topMarginPercentage = (100 * (firstRow - 1)) / screenSize.getRows();
		int leftMarginPercentage = (100 * tableStartColumn) / screenSize.getColumns();

		pagePart.setTopMargin(topMarginPercentage);
		pagePart.setLeftMargin(leftMarginPercentage);
		calculateWidth(entityDefinition, pagePart, tableStartColumn, tableEndColumn);

		pagePart.setTableDefinition(tableDefinition);
		return pagePart;
	}

	private PagePartDefinition buildPagePartFromScreenPart(ScreenPartEntityDefinition screenPartEntityDefinition,
			ScreenEntityDefinition entityDefinition) {
		Collection<ScreenFieldDefinition> fields = screenPartEntityDefinition.getFieldsDefinitions().values();
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());

		return buildPagePart(sortedFields, entityDefinition);
	}

	private PagePartDefinition buildPagePart(List<ScreenFieldDefinition> fields, ScreenEntityDefinition entityDefinition) {
		SimplePagePartDefinition pagePart = new SimplePagePartDefinition();

		int currentRow = -1;
		PagePartRowDefinition currentPagePartRow = null;
		Set<Integer> columnValues = new HashSet<Integer>();
		ScreenFieldDefinition firstField = fields.get(0);
		int startColumn = calculateLabelColumn(firstField);
		int endColumn = firstField.getEndPosition().getColumn();

		// iterate through all the neighbor fields, and build row part rows upon row change, and find the end column
		for (ScreenFieldDefinition screenFieldDefinition : fields) {
			if (screenFieldDefinition.getPosition().getRow() != currentRow) {
				currentPagePartRow = new SimplePagePartRowDefinition();
				pagePart.getPartRows().add(currentPagePartRow);
				currentRow = screenFieldDefinition.getPosition().getRow();
			}
			currentPagePartRow.getFields().add(screenFieldDefinition);
			columnValues.add(screenFieldDefinition.getPosition().getColumn());
			int fieldEndColumn = screenFieldDefinition.getLength() > 0 ? screenFieldDefinition.getEndPosition().getColumn()
					: screenFieldDefinition.getPosition().getColumn() + defaultFieldLength;
			// find the most right end column
			if (fieldEndColumn > endColumn) {
				endColumn = fieldEndColumn;
			}
		}
		pagePart.setColumns(columnValues.size());

		calculatePartPosition(fields, entityDefinition, pagePart);
		calculateWidth(entityDefinition, pagePart, startColumn, endColumn);

		return pagePart;
	}

	protected void calculateWidth(ScreenEntityDefinition entityDefinition, SimplePagePartDefinition pagePart, int startColumn,
			int endColumn) {
		int widthPercentage = 100 * (endColumn - startColumn) / entityDefinition.getScreenSize().getColumns();
		pagePart.setWidth(widthPercentage);
	}

	protected void calculatePartPosition(List<ScreenFieldDefinition> neighbourfields, ScreenEntityDefinition entityDefinition,
			SimplePagePartDefinition pagePart) {
		ScreenFieldDefinition firstField = neighbourfields.get(0);
		TerminalPosition firstFieldPosition = firstField.getPosition();

		ScreenSize screenSize = entityDefinition.getScreenSize();
		int topMarginPercentage = (100 * (firstFieldPosition.getRow() - 1)) / screenSize.getRows();
		int topLeftColumn = calculateLabelColumn(firstField) + defaultLeftMarginOffset;
		int leftMarginPercentage = ((100 * topLeftColumn) / screenSize.getColumns()) + defaultTopMarginOffset;

		pagePart.setTopMargin(topMarginPercentage);
		pagePart.setLeftMargin(leftMarginPercentage);
	}

	private int calculateLabelColumn(ScreenFieldDefinition firstField) {
		int column = firstField.getPosition().getColumn() - (firstField.getDisplayName().length() + labelFieldDistance);
		return column > 0 ? column : 1;
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

	public void setDefaultFieldLength(int defaultFieldLength) {
		this.defaultFieldLength = defaultFieldLength;
	}

	public void setDefaultLeftMarginOffset(int defaultLeftMarginOffset) {
		this.defaultLeftMarginOffset = defaultLeftMarginOffset;
	}

	public void setDefaultTopMarginOffset(int defaultTopMarginOffset) {
		this.defaultTopMarginOffset = defaultTopMarginOffset;
	}
}
