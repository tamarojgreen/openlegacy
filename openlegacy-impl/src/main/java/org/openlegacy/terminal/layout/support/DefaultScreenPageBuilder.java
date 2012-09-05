/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
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
 * Default screen page builder create a page model which is combined from page parts. Looks for neighbor fields by row & column,
 * and create page part with page part rows. Screen parts and tables are converted automatically to page parts
 * 
 * @author Roi Mor
 * 
 */
public class DefaultScreenPageBuilder implements ScreenPageBuilder {

	private final static Log logger = LogFactory.getLog(DefaultScreenPageBuilder.class);

	private int maxRowDistanceWithinPart = 1;
	private int maxColumnDistanceWithinPart = 12;
	private int labelFieldDistance = 3;

	// used for setting default field length when none specified (@ScreenField doesn't force endColumn/length)
	private int defaultFieldLength = 10;

	private int defaultLeftMarginOffset = 0;
	private int defaultTopMarginOffset = 0;

	private int additionalPartWidth = 0;

	public PageDefinition build(ScreenEntityDefinition entityDefinition) {
		Collection<ScreenFieldDefinition> fields = entityDefinition.getFieldsDefinitions().values();

		PageDefinition pageDefinition = new SimplePageDefinition(entityDefinition);

		// copy all actions to the page definition to allow separate control in page actions
		pageDefinition.getActions().addAll(entityDefinition.getActions());

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

		Collection<String> tables = entityDefinition.getTableDefinitions().keySet();
		for (String tableFieldName : tables) {
			ScreenTableDefinition tableDefinition = entityDefinition.getTableDefinitions().get(tableFieldName);
			pageDefinition.getPageParts().add(buildPagePartFromTable(tableFieldName, tableDefinition, entityDefinition));
		}
		return pageDefinition;
	}

	private PagePartDefinition buildPagePartFromTable(String tableFieldName, ScreenTableDefinition tableDefinition,
			ScreenEntityDefinition entityDefinition) {
		SimplePagePartDefinition pagePart = new SimplePagePartDefinition();

		List<ScreenColumnDefinition> columns = tableDefinition.getColumnDefinitions();

		int tableStartColumn = columns.get(0).getStartColumn();
		int tableEndColumn = columns.get(columns.size() - 1).getEndColumn();
		int firstRow = tableDefinition.getStartRow() - 1; // -1 -> header

		ScreenSize screenSize = entityDefinition.getScreenSize();
		// -1 -> since it is consider in HTML as start of the positioning
		int topMarginPercentage = (100 * (firstRow - 1)) / screenSize.getRows();
		int leftMarginPercentage = (100 * tableStartColumn) / screenSize.getColumns();
		pagePart.setTopMargin(topMarginPercentage);

		pagePart.setLeftMargin(leftMarginPercentage);
		calculateWidth(entityDefinition, pagePart, tableEndColumn - tableStartColumn);

		pagePart.setTableFieldName(tableFieldName);
		pagePart.setTableDefinition(tableDefinition);
		return pagePart;
	}

	private PagePartDefinition buildPagePartFromScreenPart(ScreenPartEntityDefinition screenPartEntityDefinition,
			ScreenEntityDefinition entityDefinition) {
		Collection<ScreenFieldDefinition> fields = screenPartEntityDefinition.getFieldsDefinitions().values();
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());

		SimplePagePartDefinition pagePart = (SimplePagePartDefinition)buildPagePart(sortedFields, entityDefinition);
		// if screen part has position (loaded from @PartPosition), override the calculated position
		if (screenPartEntityDefinition.getPartPosition() != null) {
			int leftMargin = calculateLeftMargin(entityDefinition.getScreenSize(),
					screenPartEntityDefinition.getPartPosition().getColumn());
			pagePart.setLeftMargin(leftMargin);
			int topMargin = calculateTopMargin(entityDefinition.getScreenSize(),
					screenPartEntityDefinition.getPartPosition().getRow());
			pagePart.setTopMargin(topMargin);
		}
		if (screenPartEntityDefinition.getWidth() > 0) {
			calculateWidth(entityDefinition, pagePart, screenPartEntityDefinition.getWidth());
		}
		return pagePart;

	}

	private PagePartDefinition buildPagePart(List<ScreenFieldDefinition> fields, ScreenEntityDefinition entityDefinition) {
		SimplePagePartDefinition pagePart = new SimplePagePartDefinition();

		int currentRow = -1;
		PagePartRowDefinition currentPagePartRow = null;
		Set<Integer> columnValues = new HashSet<Integer>();
		if (fields.size() == 0) {
			logger.warn("A screen/screen part with 0 fields found. Page part not created. Class:"
					+ entityDefinition.getEntityClassName());
			return pagePart;
		}
		ScreenFieldDefinition firstField = fields.get(0);
		int startColumn = calculateStartColumn(firstField);
		int endColumn = calculateEndColumn(firstField);

		// iterate through all the neighbor fields, and build row part rows upon row change, and find the end column
		for (ScreenFieldDefinition screenFieldDefinition : fields) {
			if (screenFieldDefinition.getPosition().getRow() != currentRow) {
				currentPagePartRow = new SimplePagePartRowDefinition();
				pagePart.getPartRows().add(currentPagePartRow);
				currentRow = screenFieldDefinition.getPosition().getRow();
			}
			currentPagePartRow.getFields().add(screenFieldDefinition);
			columnValues.add(screenFieldDefinition.getPosition().getColumn());
			int fieldEndColumn = calculateEndColumn(screenFieldDefinition);
			// find the most right end column
			if (fieldEndColumn > endColumn) {
				endColumn = fieldEndColumn;
			}
		}
		pagePart.setColumns(columnValues.size());

		calculatePartPosition(fields, entityDefinition, pagePart);
		calculateWidth(entityDefinition, pagePart, endColumn - startColumn);

		return pagePart;
	}

	protected void calculateWidth(ScreenEntityDefinition entityDefinition, SimplePagePartDefinition pagePart, int width) {
		int widthPercentage = 100 * width / entityDefinition.getScreenSize().getColumns();
		pagePart.setWidth(widthPercentage);
	}

	protected void calculatePartPosition(List<ScreenFieldDefinition> neighbourfields, ScreenEntityDefinition entityDefinition,
			SimplePagePartDefinition pagePart) {
		ScreenFieldDefinition firstField = neighbourfields.get(0);
		TerminalPosition firstFieldPosition = firstField.getPosition();

		ScreenSize screenSize = entityDefinition.getScreenSize();
		int topMarginPercentage = calculateTopMargin(screenSize, firstFieldPosition.getRow());
		int topLeftColumn = calculateStartColumn(firstField) + defaultLeftMarginOffset;
		int leftMarginPercentage = calculateLeftMargin(screenSize, topLeftColumn);

		pagePart.setTopMargin(topMarginPercentage);
		pagePart.setLeftMargin(leftMarginPercentage);
	}

	private static int calculateTopMargin(ScreenSize screenSize, int row) {
		return (100 * (row - 1)) / screenSize.getRows();
	}

	private int calculateLeftMargin(ScreenSize screenSize, int topLeftColumn) {
		return ((100 * topLeftColumn) / screenSize.getColumns()) + defaultTopMarginOffset;
	}

	/**
	 * Calculates the most left column of a field, consider the label position
	 * 
	 * @param field
	 * @return
	 */
	protected int calculateStartColumn(ScreenFieldDefinition field) {
		if (field.getLabelPosition() != null) {
			return field.getLabelPosition().getColumn();
		} else {
			int column = field.getPosition().getColumn() - (field.getDisplayName().length() + labelFieldDistance);
			return column > 0 ? column : 1;
		}
	}

	protected int calculateEndColumn(ScreenFieldDefinition screenFieldDefinition) {
		int fieldEndColumn = screenFieldDefinition.getLength() > 0 ? screenFieldDefinition.getEndPosition().getColumn()
				: screenFieldDefinition.getPosition().getColumn() + defaultFieldLength;
		return fieldEndColumn + getAdditionalPartWidth();
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
			for (List<ScreenFieldDefinition> neighourFields : neighourFieldsGroups) {
				for (ScreenFieldDefinition neighourField : neighourFields) {
					if (isUnderNighbour(screenFieldDefinition, neighourField)
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

	private boolean isUnderNighbour(ScreenFieldDefinition screenFieldDefinition, ScreenFieldDefinition neighbourField) {

		TerminalPosition fieldPosition = screenFieldDefinition.getPosition();
		TerminalPosition neighbourFieldPosition = neighbourField.getPosition();
		boolean underNeighbourByField = fieldPosition.getColumn() == neighbourFieldPosition.getColumn()
				&& fieldPosition.getRow() - maxRowDistanceWithinPart == neighbourFieldPosition.getRow();
		if (underNeighbourByField) {
			return true;
		}
		TerminalPosition fieldLabelPosition = screenFieldDefinition.getLabelPosition();
		TerminalPosition neighbourFieldLabelPosition = neighbourField.getLabelPosition();

		if (fieldLabelPosition == null || neighbourFieldLabelPosition == null) {
			return false;
		}

		boolean underNeighbourByLabel = fieldLabelPosition.getColumn() == neighbourFieldLabelPosition.getColumn()
				&& fieldPosition.getRow() - maxRowDistanceWithinPart == neighbourFieldPosition.getRow();
		return underNeighbourByLabel;
	}

	protected int getLabelFieldDistance() {
		return labelFieldDistance;
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

	protected int getDefaultLeftMarginOffset() {
		return defaultLeftMarginOffset;
	}

	protected int getDefaultFieldLength() {
		return defaultFieldLength;
	}

	protected int getDefaultTopMarginOffset() {
		return defaultTopMarginOffset;
	}

	public void setDefaultTopMarginOffset(int defaultTopMarginOffset) {
		this.defaultTopMarginOffset = defaultTopMarginOffset;
	}

	public int getAdditionalPartWidth() {
		return additionalPartWidth;
	}

	public void setAdditionalPartWidth(int additionalPartWidth) {
		this.additionalPartWidth = additionalPartWidth;
	}
}
