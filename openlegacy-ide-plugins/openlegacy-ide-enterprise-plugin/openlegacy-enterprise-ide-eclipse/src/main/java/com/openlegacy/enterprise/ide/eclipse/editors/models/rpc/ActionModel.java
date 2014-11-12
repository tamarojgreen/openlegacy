package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.RpcEntity;

/**
 * @author Ivan Bort
 * 
 */
public class ActionModel implements RpcActionDefinition {

	private String actionName;
	private String prevActionName;
	private String prevDisplayName;
	private String prevAlias;
	private String prevPath;
	private boolean prevGlobal;
	private String prevTargetEntityName;

	// annotation attributes
	private SessionAction<? extends Session> action;
	private String displayName = "";
	private String path;
	private boolean global = true;
	private String alias = "";
	private Class<?> targetEntity = RpcEntity.NONE.class;
	private String targetEntityName = RpcEntity.NONE.class.getSimpleName();

	public ActionModel(String actionName, String displayName, String alias, String path, boolean global) {
		this(actionName, displayName, alias, path, global, RpcEntity.NONE.class.getSimpleName());
	}

	public ActionModel(String actionName, String displayName, String alias, String path, boolean global, String targetEntityName) {
		this.actionName = actionName;
		this.displayName = displayName;
		this.alias = alias;
		this.path = path;
		this.global = global;
		this.targetEntityName = targetEntityName;

		this.prevActionName = actionName;
		this.prevDisplayName = displayName;
		this.prevAlias = alias;
		this.prevPath = path;
		this.prevGlobal = global;
		this.prevTargetEntityName = targetEntityName;
	}

	@Override
	public SessionAction<? extends Session> getAction() {
		return action;
	}

	@Override
	public String getActionName() {
		return actionName;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public boolean isDefaultAction() {
		return false;
	}

	@Override
	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	@Override
	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	@Override
	public String getProgramPath() {
		return path;
	}

	public void setProgramPath(String path) {
		this.path = path;
	}

	public String getPrevActionName() {
		return prevActionName;
	}

	public String getPrevDisplayName() {
		return prevDisplayName;
	}

	public String getPrevAlias() {
		return prevAlias;
	}

	public void setAction(SessionAction<? extends Session> action) {
		this.action = action;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getPrevPath() {
		return prevPath;
	}

	public boolean isPrevGlobal() {
		return prevGlobal;
	}

	public String getTargetEntityName() {
		return targetEntityName;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityName = targetEntityClassName;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public String getPrevTargetEntityClassName() {
		return prevTargetEntityName;
	}

}
