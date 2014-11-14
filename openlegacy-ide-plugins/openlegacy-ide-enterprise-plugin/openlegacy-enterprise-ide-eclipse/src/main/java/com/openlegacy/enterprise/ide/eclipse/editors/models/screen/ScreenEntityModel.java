package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.pages.holders.screen.ScreenEntityRole;

import org.openlegacy.EntityType;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.modules.messages.Messages.MessagesEntity;
import org.openlegacy.modules.table.LookupEntity;
import org.openlegacy.modules.table.RecordSelectionEntity;
import org.openlegacy.terminal.ScreenEntityType;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents @ScreenEntity annotation
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenEntityModel extends ScreenNamedObject {

	// map of screen types
	public static Map<String, Class<? extends EntityType>> mapScreenTypes = Collections.unmodifiableMap(new HashMap<String, Class<? extends EntityType>>() {

		private static final long serialVersionUID = 9162336113861184461L;

		{
			put(ScreenEntityType.General.class.getSimpleName(), ScreenEntityType.General.class);
			put(LoginEntity.class.getSimpleName(), LoginEntity.class);
			put(LookupEntity.class.getSimpleName(), LookupEntity.class);
			put(MenuEntity.class.getSimpleName(), MenuEntity.class);
			put(MessagesEntity.class.getSimpleName(), MessagesEntity.class);
			put(RecordSelectionEntity.class.getSimpleName(), RecordSelectionEntity.class);
		}
	});

	protected String className;

	// annotation attributes
	private String name = "";
	private String displayName = "";
	private boolean supportTerminalData = false;
	private boolean window = false;
	private Class<? extends EntityType> screenType = ScreenEntityType.General.class;
	private int columns = ScreenSize.DEFAULT_COLUMN;
	private int rows = ScreenSize.DEFAULT_ROWS;
	private boolean child = false;
	private boolean validateKeys = true;
	private boolean rightToLeft = false;
	private List<ScreenEntityRole> roles = new ArrayList<ScreenEntityRole>();
	private boolean autoMapKeyboardActions = false;

	public ScreenEntityModel() {
		super(ScreenEntity.class.getSimpleName());
	}

	public ScreenEntityModel(UUID uuid) {
		super(ScreenEntity.class.getSimpleName());
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		this.name = entityDefinition.getEntityName();
		this.displayName = entityDefinition.getDisplayName();
		this.columns = entityDefinition.getScreenSize().getColumns();
		this.rows = entityDefinition.getScreenSize().getRows();
		this.child = entityDefinition.isChild();
		this.supportTerminalData = entityDefinition.isSupportTerminalData();
		this.window = entityDefinition.isWindow();
		this.screenType = mapScreenTypes.get(entityDefinition.getTypeName());
		this.validateKeys = entityDefinition.isValidateKeys();
		this.rightToLeft = entityDefinition.isRightToLeft();
		this.roles = convertRoles(entityDefinition.getRoles());
		this.autoMapKeyboardActions = entityDefinition.isAutoMapKeyboardActions();

		this.className = entityDefinition.getEntityClassName();
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Use
	 * <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Use init(CodeBasedScreenEntityDefinition entityDefinition) instead.");//$NON-NLS-1$
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ScreenEntityModel)) {
			return false;
		}
		ScreenEntityModel model = (ScreenEntityModel)obj;
		return (this.modelName.equals(model.getModelName()) && (this.name.equals(model.getName()))
				&& (this.displayName.equals(model.getDisplayName()))
				&& (this.supportTerminalData == model.isSupportTerminalData()) && (this.window == model.isWindow())
				&& (this.screenType == model.getScreenType()) && (this.columns == model.getColumns())
				&& (this.rows == model.getRows()) && (this.child == model.isChild())
				&& (this.validateKeys == model.isValidateKeys()) && (this.rightToLeft == model.isRightToLeft()) && (this.autoMapKeyboardActions == model.isAutoMapKeyboardActions()));
		// XXX Ivan: check, maybe need to add roles attribute into above lines
	}

	@Override
	public ScreenEntityModel clone() {
		ScreenEntityModel model = new ScreenEntityModel(this.uuid);
		model.setChild(this.child);
		model.setColumns(this.columns);
		model.setDisplayName(this.displayName);
		model.setName(this.name);
		model.setRows(this.rows);
		model.setScreenType(this.screenType);
		model.setSupportTerminalData(this.supportTerminalData);
		model.setWindow(this.window);
		model.setValidateKeys(this.validateKeys);
		model.setRightToLeft(this.rightToLeft);
		List<ScreenEntityRole> list = new ArrayList<ScreenEntityRole>();
		for (ScreenEntityRole role : roles) {
			list.add(new ScreenEntityRole(role.getRole()));
		}
		model.setRoles(list);
		model.setAutoMapKeyboardActions(this.autoMapKeyboardActions);
		return model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isSupportTerminalData() {
		return supportTerminalData;
	}

	public void setSupportTerminalData(boolean supportTerminalData) {
		this.supportTerminalData = supportTerminalData;
	}

	public boolean isWindow() {
		return window;
	}

	public void setWindow(boolean window) {
		this.window = window;
	}

	public Class<? extends EntityType> getScreenType() {
		return screenType;
	}

	public void setScreenType(Class<? extends EntityType> screenType) {
		this.screenType = screenType;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public String getClassName() {
		return this.className;
	}

	public boolean isValidateKeys() {
		return validateKeys;
	}

	public void setValidateKeys(boolean validateKeys) {
		this.validateKeys = validateKeys;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public List<ScreenEntityRole> getRoles() {
		return roles;
	}

	public void setRoles(List<ScreenEntityRole> roles) {
		this.roles = roles;
	}

	public List<String> getRolesAsStringList() {
		List<String> list = new ArrayList<String>();
		if (this.roles.isEmpty()) {
			return list;
		}
		for (ScreenEntityRole role : this.roles) {
			list.add(role.getRole());
		}
		return list;
	}

	public void setScreenTypeDefaultValue() {
		screenType = ScreenEntityType.General.class;
	}

	public boolean isAutoMapKeyboardActions() {
		return autoMapKeyboardActions;
	}

	public void setAutoMapKeyboardActions(boolean autoMapKeyboardActions) {
		this.autoMapKeyboardActions = autoMapKeyboardActions;
	}

	private List<ScreenEntityRole> convertRoles(List<String> list) {
		List<ScreenEntityRole> roles = new ArrayList<ScreenEntityRole>();
		if ((list == null) || list.isEmpty()) {
			return roles;
		}
		for (String str : list) {
			roles.add(new ScreenEntityRole(str));
		}
		return roles;
	}
}
