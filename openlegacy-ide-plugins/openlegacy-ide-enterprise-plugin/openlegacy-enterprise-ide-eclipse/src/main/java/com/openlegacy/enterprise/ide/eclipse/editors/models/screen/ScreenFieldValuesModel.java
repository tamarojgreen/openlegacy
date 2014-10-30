package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.definitions.FieldWithValuesTypeDefinition;
import org.openlegacy.terminal.ScreenRecordsProvider;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.UUID;

/**
 * Represent @ScreenFieldValues annotation
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenFieldValuesModel extends ScreenFieldModel {

	// annotation attributes
	private Class<? extends ScreenRecordsProvider> provider = ScreenRecordsProvider.class;
	private Class<?> sourceScreenEntity;
	private boolean collectAll = false;
	private boolean asWindow = false;
	private Class<? extends TerminalAction> autoSubmitAction = TerminalActions.NULL.class;
	private String displayFieldName = "";
	private String searchField = "";

	// other
	private String sourceScreenEntityName = "";
	private String autoSubmitActionName = "";

	public ScreenFieldValuesModel(NamedObject parent) {
		super(ScreenFieldValues.class.getName(), parent);
	}

	public ScreenFieldValuesModel(UUID uuid, NamedObject parent) {
		super(ScreenFieldValues.class.getSimpleName(), parent);
		this.uuid = uuid;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		super.init(screenFieldDefinition);
		if (super.isInitialized() && (screenFieldDefinition.getFieldTypeDefinition() instanceof FieldWithValuesTypeDefinition)) {
			FieldWithValuesTypeDefinition definition = (FieldWithValuesTypeDefinition)screenFieldDefinition.getFieldTypeDefinition();
			RecordsProvider<Session, Object> recordsProvider = definition.getRecordsProvider();
			if (recordsProvider != null) {
				this.provider = (Class<? extends ScreenRecordsProvider>)recordsProvider.getClass();
			}
			this.collectAll = definition.isCollectAll();
			this.sourceScreenEntity = definition.getSourceEntityClass();
			this.sourceScreenEntityName = definition.getSourceEntityClassName();
			this.asWindow = definition.isAsWindow();

			autoSubmitActionName = definition.getAutoSubmitActionName() != null ? definition.getAutoSubmitActionName() : "";
			displayFieldName = definition.getDisplayFieldName() != null ? definition.getDisplayFieldName() : "";
			searchField = definition.getSearchField() != null ? definition.getSearchField() : "";
		}
	}

	@Override
	public ScreenFieldValuesModel clone() {
		ScreenFieldValuesModel model = new ScreenFieldValuesModel(this.uuid, this.parent);
		fillModel(model);

		model.setProvider(this.provider);
		model.setCollectAll(this.collectAll);
		model.setSourceScreenEntity(this.sourceScreenEntity);
		model.setSourceScreenEntityName(this.sourceScreenEntityName);
		model.setAsWindow(this.asWindow);
		model.setAutoSubmitAction(this.autoSubmitAction);
		model.setAutoSubmitActionName(this.autoSubmitActionName);
		model.setDisplayFieldName(this.displayFieldName);
		model.setSearchField(this.searchField);
		return model;
	}

	public Class<? extends ScreenRecordsProvider> getProvider() {
		return provider;
	}

	public void setProvider(Class<? extends ScreenRecordsProvider> provider) {
		this.provider = provider;
	}

	public Class<?> getSourceScreenEntity() {
		return sourceScreenEntity;
	}

	public void setSourceScreenEntity(Class<?> sourceScreenEntity) {
		this.sourceScreenEntity = sourceScreenEntity;
	}

	public boolean isCollectAll() {
		return collectAll;
	}

	public void setCollectAll(boolean collectAll) {
		this.collectAll = collectAll;
	}

	public String getSourceScreenEntityName() {
		return sourceScreenEntityName;
	}

	public void setSourceScreenEntityName(String sourceScreenEntityName) {
		this.sourceScreenEntityName = sourceScreenEntityName;
	}

	public boolean isAsWindow() {
		return asWindow;
	}

	public void setAsWindow(boolean asWindow) {
		this.asWindow = asWindow;
	}

	public Class<? extends TerminalAction> getAutoSubmitAction() {
		return autoSubmitAction;
	}

	public void setAutoSubmitAction(Class<? extends TerminalAction> autoSubmitAction) {
		this.autoSubmitAction = autoSubmitAction;
	}

	public String getDisplayFieldName() {
		return displayFieldName;
	}

	public void setDisplayFieldName(String displayFieldName) {
		this.displayFieldName = displayFieldName;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getAutoSubmitActionName() {
		return autoSubmitActionName;
	}

	public void setAutoSubmitActionName(String autoSubmitActionName) {
		this.autoSubmitActionName = autoSubmitActionName;
	}

	public void setAutoSubmitActionDefaultValue() {
		autoSubmitAction = TerminalActions.NULL.class;
		autoSubmitActionName = "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public ScreenFieldModel convertFrom(ScreenFieldModel model) {
		super.convertFrom(model);
		provider = getFieldValue("provider") != null ? (Class<? extends ScreenRecordsProvider>)getFieldValue("provider")
				: ScreenRecordsProvider.class;
		sourceScreenEntity = getFieldValue("sourceScreenEntity") != null ? (Class<?>)getFieldValue("sourceScreenEntity") : null;
		collectAll = getFieldValue("collectAll") != null ? (Boolean)getFieldValue("collectAll") : false;
		sourceScreenEntityName = getFieldValue("sourceScreenEntityName") != null ? (String)getFieldValue("sourceScreenEntityName")
				: "";
		asWindow = getFieldValue("asWindow") != null ? (Boolean)getFieldValue("asWindow") : false;
		autoSubmitAction = getFieldValue("autoSubmitAction") != null ? (Class<? extends TerminalAction>)getFieldValue("autoSubmitAction")
				: TerminalActions.NULL.class;
		autoSubmitActionName = getFieldValue("autoSubmitActionName") != null ? (String)getFieldValue("autoSubmitActionName") : "";
		displayFieldName = getFieldValue("displayFieldName") != null ? (String)getFieldValue("displayFieldName") : "";
		searchField = getFieldValue("searchField") != null ? (String)getFieldValue("searchField") : "";
		return super.convertFrom(model);
	}

	@Override
	public void fillFieldsMap() {
		super.fillFieldsMap();
		fieldsMap.put("provider", provider);
		fieldsMap.put("sourceScreenEntity", sourceScreenEntity);
		fieldsMap.put("collectAll", collectAll);
		fieldsMap.put("sourceScreenEntityName", sourceScreenEntityName);
		fieldsMap.put("asWindow", asWindow);
		fieldsMap.put("autoSubmitAction", autoSubmitAction);
		fieldsMap.put("autoSubmitActionName", autoSubmitActionName);
		fieldsMap.put("displayFieldName", displayFieldName);
		fieldsMap.put("searchField", searchField);
	}

}
