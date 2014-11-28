package com.openlegacy.enterprise.ide.eclipse.editors.models;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractEntity {

	protected Map<UUID, Map<String, AbstractAction>> actions = new HashMap<UUID, Map<String, AbstractAction>>();

	private boolean isDirty = false;

	protected int newFieldsCount = 0;
	protected int newColumnsCount = 0;
	protected int newTableActionsCount = 0;
	protected int newPartsCount = 0;

	/**
	 * Sets dirty state of entity. Also clears actions list
	 * 
	 * @param isDirty
	 */
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		if (!this.isDirty) {
			actions.clear();
		}
	}

	public void addAction(AbstractAction action) {
		if (!this.actions.containsKey(action.getUuid())) {
			this.actions.put(action.getUuid(), new HashMap<String, AbstractAction>());
		}
		this.actions.get(action.getUuid()).put(action.getKey(), action);
		this.setDirty(true);
	}

	public void removeAction(UUID uuid, String key) {
		if (this.actions.containsKey(uuid)) {
			this.actions.get(uuid).remove(key);
			if (this.actions.get(uuid).isEmpty()) {
				this.actions.remove(uuid);
			}
		}
		this.setDirty(!this.actions.isEmpty());
	}

	public void removeAction(UUID uuid, String key, ActionType actionType) {
		if (this.actions.containsKey(uuid)) {
			Set<String> keysToRemove = new HashSet<String>();
			Map<String, AbstractAction> map = this.actions.get(uuid);
			for (String mapKey : map.keySet()) {
				if (StringUtils.equals(key, mapKey) && map.get(mapKey).getActionType().equals(actionType)) {
					keysToRemove.add(mapKey);
				}
			}
			for (String keyToRemove : keysToRemove) {
				map.remove(keyToRemove);
			}
			if (this.actions.get(uuid).isEmpty()) {
				this.actions.remove(uuid);
			}
		}
		this.setDirty(!this.actions.isEmpty());
	}

	public void removeActionsSet(UUID uuid) {
		if (this.actions.containsKey(uuid)) {
			this.actions.remove(uuid);
		}
		this.setDirty(!this.actions.isEmpty());
	}

	public void removeActionsForTypes(UUID uuid, ActionType[] types) {
		if (this.actions.containsKey(uuid)) {
			Set<String> keysToRemove = new HashSet<String>();
			Map<String, AbstractAction> map = this.actions.get(uuid);

			for (String key : map.keySet()) {
				for (ActionType type : types) {
					if (type.equals(map.get(key).getActionType())) {
						keysToRemove.add(key);
					}
				}
			}
			for (String key : keysToRemove) {
				map.remove(key);
			}
		}
		this.setDirty(!this.actions.isEmpty());
	}

	public boolean isDirty() {
		return isDirty;
	}

	public Map<UUID, Map<String, AbstractAction>> getActions() {
		return actions;
	}

	public abstract String getEntityFullyQualifiedName();

}
