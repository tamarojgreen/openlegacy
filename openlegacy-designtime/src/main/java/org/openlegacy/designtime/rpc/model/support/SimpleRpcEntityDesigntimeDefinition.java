package org.openlegacy.designtime.rpc.model.support;

import org.openlegacy.rpc.definitions.SimpleRpcEntityDefinition;

public class SimpleRpcEntityDesigntimeDefinition extends SimpleRpcEntityDefinition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String packageName;
	private String navigation;
	private boolean onlyPart;

	public SimpleRpcEntityDesigntimeDefinition() {
		super();
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setGenerateAspect(boolean generateAspectJ) {
		// TODO Auto-generated method stub

	}

	public boolean isOnlyPart() {
		return onlyPart;
	}

	public void setOnlyPart(boolean onlyPart) {
		this.onlyPart = onlyPart;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;

	}

}
