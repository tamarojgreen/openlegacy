package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.RpcActionDefinition;

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

	// annotation attributes
	private SessionAction<? extends Session> action;
	private String displayName = "";
	private String path;
	private boolean global = true;
	private String alias = "";

	public ActionModel(String actionName, String displayName, String alias, String path, boolean global) {
		this.actionName = actionName;
		this.displayName = displayName;
		this.alias = alias;
		this.path = path;
		this.global = global;

		this.prevActionName = actionName;
		this.prevDisplayName = displayName;
		this.prevAlias = alias;
		this.prevPath = path;
		this.prevGlobal = global;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getAction()
	 */
	public SessionAction<? extends Session> getAction() {
		return action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getActionName()
	 */
	public String getActionName() {
		return actionName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getDisplayName()
	 */
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getAlias()
	 */
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#isDefaultAction()
	 */
	public boolean isDefaultAction() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#isGlobal()
	 */
	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getTargetEntity()
	 */
	public Class<?> getTargetEntity() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.RpcActionDefinition#getProgramPath()
	 */
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

}
