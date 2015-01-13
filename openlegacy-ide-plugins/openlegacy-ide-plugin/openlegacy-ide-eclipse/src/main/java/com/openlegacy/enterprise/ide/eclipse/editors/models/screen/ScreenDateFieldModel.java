package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.UUID;

/**
 * Represents @ScreenDateField
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenDateFieldModel extends ScreenFieldModel {

	private Integer year = 0;
	private Integer month = 0;
	private Integer day = 0;
	private String pattern = "";

	public ScreenDateFieldModel(NamedObject parent) {
		super(ScreenDateField.class.getSimpleName(), parent);
		this.javaTypeName = Messages.getString("type.date");//$NON-NLS-1$
	}

	public ScreenDateFieldModel(UUID uuid, NamedObject parent) {
		super(ScreenDateField.class.getSimpleName(), parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.date");//$NON-NLS-1$
	}

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		super.init(screenFieldDefinition);
		if (super.isInitialized() && (screenFieldDefinition.getFieldTypeDefinition() instanceof DateFieldTypeDefinition)) {
			DateFieldTypeDefinition definition = (DateFieldTypeDefinition)screenFieldDefinition.getFieldTypeDefinition();
			this.year = definition.getYearColumn();
			this.month = definition.getMonthColumn();
			this.day = definition.getDayColumn();
			this.pattern = definition.getPattern();
		}
	}

	@Override
	public ScreenDateFieldModel clone() {
		ScreenDateFieldModel model = new ScreenDateFieldModel(this.uuid, this.parent);
		fillModel(model);

		model.setYear(this.year);
		model.setMonth(this.month);
		model.setDay(this.day);
		model.setPattern(this.pattern);
		return model;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public ScreenFieldModel convertFrom(ScreenFieldModel model) {
		super.convertFrom(model);
		year = model.getFieldValue("year") != null ? (Integer)model.getFieldValue("year") : 0;
		month = model.getFieldValue("month") != null ? (Integer)model.getFieldValue("month") : 0;
		day = model.getFieldValue("day") != null ? (Integer)model.getFieldValue("day") : 0;
		pattern = model.getFieldValue("pattern") != null ? (String)model.getFieldValue("pattern") : "";
		return this;
	}

	@Override
	public void fillFieldsMap() {
		super.fillFieldsMap();
		fieldsMap.put("year", year);
		fieldsMap.put("month", month);
		fieldsMap.put("day", day);
		fieldsMap.put("pattern", pattern);
	}

}
