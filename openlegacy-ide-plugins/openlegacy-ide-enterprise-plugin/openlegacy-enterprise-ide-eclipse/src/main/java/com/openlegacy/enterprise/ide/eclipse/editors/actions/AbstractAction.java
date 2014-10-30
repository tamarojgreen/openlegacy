package com.openlegacy.enterprise.ide.eclipse.editors.actions;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractAction {

	private UUID uuid;

	/**
	 * Determines what action should do: add, modify or remove
	 */
	private ActionType actionType = ActionType.NONE;
	/**
	 * Determines target of changes. e.g. if need change an annotation property then
	 * ASTNode.NORMAL_ANNOTATION|ASTNode.MEMBER_VALUE_PAIR will be used. Available values: ASTNode.NORMAL_ANNOTATION,
	 * ASTNode.MEMBER_VALUE_PAIR, ASTNode.FIELD_DECLARATION
	 */
	private int target;

	private String key;
	private Object value;

	private NamedObject namedObject;

	public AbstractAction(UUID uuid, NamedObject namedObject, ActionType actionType, int kind, String key, Object value) {
		this.uuid = uuid;
		this.namedObject = namedObject;
		this.actionType = actionType;
		this.target = kind;
		this.key = key;
		this.value = value;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getTarget() {
		return target;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public NamedObject getNamedObject() {
		return namedObject;
	}

	public abstract Class<?> getAnnotationClass();

	public String getKey(boolean split) {
		return split ? key.split("\\.")[0] : key;//$NON-NLS-1$
	}
}
