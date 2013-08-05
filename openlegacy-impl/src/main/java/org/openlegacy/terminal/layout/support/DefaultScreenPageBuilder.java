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
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.definitions.page.support.SimplePagePartDefinition;
import org.openlegacy.definitions.page.support.SimplePagePartRowDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.layout.PagePartDefinition;
import org.openlegacy.layout.PagePartRowDefinition;
import org.openlegacy.terminal.PositionedPart;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	private int maxColumnDistanceWithinPart = 40;
	private int labelFieldDistance = 3;

	// used for setting default field length when none specified (@ScreenField doesn't force endColumn/length)
	private int defaultFieldLength = 10;

	private int defaultLeftMarginOffset = 0;
	private int defaultTopMarginOffset = 0;

	private int additionalPartWidth = 0;

	/**
	 * Determine the max distance between to fields start, to consider them in a single panel column
	 */
	private int maxNeighbourColumnsOffset = 5;

	/**
	 * Page builder entry point. Builds a page definition from a screen entity definition
	 */
	public PageDefinition build(ScreenEntityDefinition entityDefinition) {
		Collection<ScreenFieldDefinition> fields = entityDefinition.getFieldsDefinitions().values();

		PageDefinition pageDefinition = new SimplePageDefinition(entityDefinition);

		// copy all actions to the page definition to allow separate control in page actions
		pageDefinition.getActions().addAll(entityDefinition.getActions());

		// sort fields by position
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		sortFields(sortedFields);

		// create groups of neighbor fields
		List<List<ScreenFieldDefinition>> neighourFieldsGroups = groupNeighbourFields(sortedFields);

		// create a page part from each fields group
		for (List<ScreenFieldDefinition> neighbourfields : neighourFieldsGroups) {
			pageDefinition.getPageParts().add(buildPagePart(neighbourfields, entityDefinition));
		}

		List<ScreenPartEntityDefinition> screenPartsList = sortPageParts(entityDefinition);

		// create page parts from screen parts
		for (ScreenPartEntityDefinition screenPartEntityDefinition : screenPartsList) {
			pageDefinition.getPageParts().add(buildPagePartFromScreenPart(screenPartEntityDefinition, entityDefinition));
		}

		// create page parts from screen tables
		Collection<String> tables = entityDefinition.getTableDefinitions().keySet();
		for (String tableFieldName : tables) {
			ScreenTableDefinition tableDefinition = entityDefinition.getTableDefinitions().get(tableFieldName);
			pageDefinition.getPageParts().add(buildPagePartFromTable(tableFieldName, tableDefinition, entityDefinition));
		}
		return pageDefinition;
	}

	@SuppressWarnings("unchecked")
	private static List<ScreenPartEntityDefinition> sortPageParts(ScreenEntityDefinition entityDefinition) {
		Collection<PartEntityDefinition<ScreenFieldDefinition>> screenParts = entityDefinition.getPartsDefinitions().values();
		List<ScreenPartEntityDefinition> screenPartsList = new ArrayList<ScreenPartEntityDefinition>();
		screenPartsList.addAll((Collection<? extends ScreenPartEntityDefinition>)screenParts);
		Collections.sort(screenPartsList, new Comparator<ScreenPartEntityDefinition>() {

			public int compare(ScreenPartEntityDefinition o1, ScreenPartEntityDefinition o2) {
				return o1.getTopRow() - o2.getTopRow();
			}
		});
		return screenPartsList;
	}

	protected void sortFields(List<ScreenFieldDefinition> sortedFields) {
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
	}

	/**
	 * Build a page part from screen table. calculates the table top/left position and positions the page part by this position
	 * 
	 * @param tableFieldName
	 *            the table field name within the entity definition
	 * @param tableDefinition
	 *            table definition which the page part should be based on
	 * @param entityDefinition
	 *            entity definition which holds the table
	 * @return page part
	 */
	private PagePartDefinition buildPagePartFromTable(String tableFieldName, ScreenTableDefinition tableDefinition,
			ScreenEntityDefinition entityDefinition) {
		SimplePagePartDefinition pagePart = new SimplePagePartDefinition();

		List<ScreenColumnDefinition> columns = tableDefinition.getColumnDefinitions();

		int tableStartColumn = getTableStartColumn(columns);
		int tableEndColumn = getTableEndColumn(columns);
		int firstRow = tableDefinition.getStartRow() - 1; // -1 -> header

		ScreenSize screenSize = entityDefinition.getScreenSize();
		// -1 -> since it is consider in HTML as start of the positioning
		int topMarginPercentage = (100 * (firstRow - 1)) / screenSize.getRows();
		int leftMarginPercentage = (100 * tableStartColumn) / screenSize.getColumns();
		pagePart.setTopMargin(topMarginPercentage);

		pagePart.setLeftMargin(leftMarginPercentage);
		calculateWidth(entityDefinition, pagePart, Math.abs(tableEndColumn - tableStartColumn));

		pagePart.setTableFieldName(tableFieldName);
		pagePart.setTableDefinition(tableDefinition);

		overridePositionAndWidth((PositionedPart)tableDefinition, entityDefinition, pagePart);
		return pagePart;
	}

	protected int getTableEndColumn(List<ScreenColumnDefinition> columns) {
		return columns.get(columns.size() - 1).getEndColumn();
	}

	protected int getTableStartColumn(List<ScreenColumnDefinition> columns) {
		return columns.get(0).getStartColumn();
	}

	private PagePartDefinition buildPagePartFromScreenPart(ScreenPartEntityDefinition screenPartEntityDefinition,
			ScreenEntityDefinition entityDefinition) {
		Collection<ScreenFieldDefinition> fields = screenPartEntityDefinition.getFieldsDefinitions().values();
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		sortFields(sortedFields);

		SimplePagePartDefinition pagePart = (SimplePagePartDefinition)buildPagePart(sortedFields, entityDefinition);
		pagePart.setDisplayName(screenPartEntityDefinition.getDisplayName());
		// if screen part has position (loaded from @PartPosition), override the calculated position
		overridePositionAndWidth((PositionedPart)screenPartEntityDefinition, entityDefinition, pagePart);
		return pagePart;

	}

	private void overridePositionAndWidth(PositionedPart positionedPart, ScreenEntityDefinition entityDefinition,
			SimplePagePartDefinition pagePart) {
		if (positionedPart.getPartPosition() != null) {
			int leftMargin = calculateLeftMargin(entityDefinition.getScreenSize(), positionedPart.getPartPosition().getColumn());
			pagePart.setLeftMargin(leftMargin);
			int topMargin = calculateTopMargin(entityDefinition.getScreenSize(), positionedPart.getPartPosition().getRow());
			pagePart.setTopMargin(topMargin);
		}
		if (positionedPart.getWidth() > 0) {
			calculateWidth(entityDefinition, pagePart, positionedPart.getWidth());
			// if has width (set from @PartPosition) but no position - set it as relative
			if (positionedPart.getPartPosition() == null) {
				pagePart.setRelative(true);
			}
		}
		if (entityDefinition.isWindow()) {
			pagePart.setRelative(true);
		}
	}

	/**
	 * Builds a page part from a list of neighbor fields
	 * 
	 * @param fields
	 *            the neighbor fields
	 * @param entityDefinition
	 *            the entity containing all the fields
	 * @return page part
	 */
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
			int fieldStartColumn = calculateStartColumn(screenFieldDefinition);
			int fieldEndColumn = calculateEndColumn(screenFieldDefinition);
			columnValues.add(getFieldLogicalStart(fieldStartColumn, fieldEndColumn));
			// find the most right end column
			if (fieldEndColumn > endColumn) {
				endColumn = fieldEndColumn;
			}
			if (fieldStartColumn < startColumn) {
				startColumn = fieldStartColumn;
			}
		}

		columnValues = calculateNumberOfColumnsForPagePage(columnValues);

		pagePart.setColumns(columnValues.size());

		calculatePartPosition(fields, entityDefinition, pagePart, startColumn);
		calculateWidth(entityDefinition, pagePart, endColumn - startColumn);

		return pagePart;
	}

	protected Integer getFieldLogicalStart(int fieldStartColumn, int fieldEndColumn) {
		return fieldStartColumn;
	}

	/**
	 * Calculate the row part columns consider small offsets between neighbor fields. e.g: Field which starts at column 10 and
	 * field which starts at columns 12, will be considered as 1
	 * 
	 * @param columnValues
	 * @return
	 */
	private Set<Integer> calculateNumberOfColumnsForPagePage(Set<Integer> columnValues) {
		Integer[] columnValuesArr = columnValues.toArray(new Integer[columnValues.size()]);
		// make sure columns are ordered
		Arrays.sort(columnValuesArr);

		Set<Integer> newColumnValues = new HashSet<Integer>();
		newColumnValues.add(columnValuesArr[0]);
		for (int i = 0; i < columnValuesArr.length - 1; i++) {
			int columnDistance = Math.abs(columnValuesArr[i] - columnValuesArr[i + 1]);
			if (columnDistance > maxNeighbourColumnsOffset) {
				newColumnValues.add(columnValuesArr[i + 1]);
			}
		}
		return newColumnValues;
	}

	/**
	 * Calculated the given page part width in percentages. Overridden by Bidi
	 * 
	 * @param entityDefinition
	 *            the entity which contains the page part is based on
	 * @param pagePart
	 *            the page part
	 * @param width
	 *            the page part columns width
	 */
	protected void calculateWidth(ScreenEntityDefinition entityDefinition, SimplePagePartDefinition pagePart, int width) {
		int widthPercentage = 0;
		if (entityDefinition.isWindow()) {
			widthPercentage = 100;
		} else {
			widthPercentage = 100 * width / entityDefinition.getScreenSize().getColumns();
		}
		pagePart.setWidth(widthPercentage);
	}

	protected void calculatePartPosition(List<ScreenFieldDefinition> neighbourfields, ScreenEntityDefinition entityDefinition,
			SimplePagePartDefinition pagePart, int startColumn) {
		ScreenFieldDefinition firstField = neighbourfields.get(0);
		TerminalPosition firstFieldPosition = firstField.getPosition();

		ScreenSize screenSize = entityDefinition.getScreenSize();
		int topMarginPercentage = calculateTopMargin(screenSize, firstFieldPosition.getRow());
		int topLeftColumn = startColumn + defaultLeftMarginOffset;
		int leftMarginPercentage = calculateLeftMargin(screenSize, topLeftColumn);

		pagePart.setTopMargin(topMarginPercentage);
		pagePart.setLeftMargin(leftMarginPercentage);
	}

	private int calculateTopMargin(ScreenSize screenSize, int row) {
		int result = (100 * (row - 1)) / screenSize.getRows() + defaultTopMarginOffset;
		return result > 0 ? result : 0;
	}

	private int calculateLeftMargin(ScreenSize screenSize, int topLeftColumn) {
		int result = ((100 * topLeftColumn) / screenSize.getColumns()) + defaultLeftMarginOffset;
		return result > 0 ? result : 0;
	}

	/**
	 * Calculates the most left column of a field, considering the label position
	 * 
	 * @param field
	 * @return start column of the field
	 */
	protected int calculateStartColumn(ScreenFieldDefinition field) {
		if (field.getLabelPosition() != null) {
			return field.getLabelPosition().getColumn();
		} else {
			int column = field.getPosition().getColumn() - (field.getDisplayName().length() + labelFieldDistance);
			return column > 0 ? column : 1;
		}
	}

	/**
	 * Calculates the most right column of a field, considering the label position
	 * 
	 * @param field
	 * @return start column of the field
	 */
	protected int calculateEndColumn(ScreenFieldDefinition screenFieldDefinition) {
		int fieldEndColumn = screenFieldDefinition.getLength() > 0 ? screenFieldDefinition.getEndPosition().getColumn()
				: screenFieldDefinition.getPosition().getColumn() + defaultFieldLength;

		if (screenFieldDefinition.getDescriptionFieldDefinition() != null) {
			fieldEndColumn = screenFieldDefinition.getDescriptionFieldDefinition().getEndPosition().getColumn();
		}
		return fieldEndColumn + getAdditionalPartWidth();
	}

	/**
	 * Group all fields which are close to each other by a given row (1 - default max), or same row with max distance column (10)
	 * 
	 * @param sortedFields
	 * @return list of neighbor fields
	 */
	private List<List<ScreenFieldDefinition>> groupNeighbourFields(List<ScreenFieldDefinition> sortedFields) {
		List<List<ScreenFieldDefinition>> neighourFieldsGroups = new ArrayList<List<ScreenFieldDefinition>>();

		for (ScreenFieldDefinition screenFieldDefinition : sortedFields) {
			boolean found = false;
			for (List<ScreenFieldDefinition> neighourFields : neighourFieldsGroups) {
				if (!found) {
					for (ScreenFieldDefinition neighourField : neighourFields) {
						if (isBelowNighbour(screenFieldDefinition, neighourField)
								|| isRightNeighbour(screenFieldDefinition.getPosition(), neighourField)) {
							logger.debug(MessageFormat.format("Adding field definition {0} to neighbour fields: {1}",
									screenFieldDefinition, neighourFields));
							neighourFields.add(screenFieldDefinition);
							found = true;
							break;
						}
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

	private boolean isBelowNighbour(ScreenFieldDefinition screenFieldDefinition, ScreenFieldDefinition neighbourField) {

		TerminalPosition fieldPosition = screenFieldDefinition.getPosition();
		TerminalPosition neighbourFieldPosition = neighbourField.getPosition();
		int belowColumnsDistance = Math.abs(fieldPosition.getColumn() - neighbourFieldPosition.getColumn());
		int belowRowsDistance = Math.abs(fieldPosition.getRow() - neighbourFieldPosition.getRow());
		boolean isBelowNeighbour = belowColumnsDistance <= maxNeighbourColumnsOffset
				&& belowRowsDistance <= maxRowDistanceWithinPart;
		if (isBelowNeighbour) {
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

	/**
	 * The default label distance from it's field. Used for part width calculations
	 * 
	 * @param labelFieldDistance
	 */
	public void setLabelFieldDistance(int labelFieldDistance) {
		this.labelFieldDistance = labelFieldDistance;
	}

	/**
	 * The maximum row distance within a part. If higher fields will be in different parts
	 * 
	 * @param maxRowDistanceWithinPart
	 */
	public void setMaxRowDistanceWithinPart(int maxRowDistanceWithinPart) {
		this.maxRowDistanceWithinPart = maxRowDistanceWithinPart;
	}

	/**
	 * The maximum distance between 2 fields column's in the same part, which considers them as neighbors within the same part
	 * 
	 * @param maxColumnDistanceWithinPart
	 */
	public void setMaxColumnDistanceWithinPart(int maxColumnDistanceWithinPart) {
		this.maxColumnDistanceWithinPart = maxColumnDistanceWithinPart;
	}

	/**
	 * The maximum distance between 2 vertical neighbor fields which consider them as 1 column. Default to 5
	 * 
	 * @param maxNeighbourColumnsOffset
	 */
	public void setMaxNeighbourColumnsOffset(int maxNeighbourColumnsOffset) {
		this.maxNeighbourColumnsOffset = maxNeighbourColumnsOffset;
	}

	public void setDefaultFieldLength(int defaultFieldLength) {
		this.defaultFieldLength = defaultFieldLength;
	}

	/**
	 * Additional pixels to add or remove (when negative) to part left location
	 * 
	 * @param defaultLeftMarginOffset
	 */
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

	/**
	 * Additional pixels to add or remove (when negative) to part top location
	 * 
	 * @param defaultTopMarginOffset
	 */
	public void setDefaultTopMarginOffset(int defaultTopMarginOffset) {
		this.defaultTopMarginOffset = defaultTopMarginOffset;
	}

	public int getAdditionalPartWidth() {
		return additionalPartWidth;
	}

	/**
	 * The additional pixels width to add to each part
	 * 
	 * @param additionalPartWidth
	 */
	public void setAdditionalPartWidth(int additionalPartWidth) {
		this.additionalPartWidth = additionalPartWidth;
	}
}
