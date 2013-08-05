package org.openlegacy.designtime.rpc.model.support;

import org.openlegacy.rpc.definitions.SimpleRpcEntityDefinition;

public class SimpleRpcEntityDesigntimeDefinition extends SimpleRpcEntityDefinition {

	private String packageName;
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

}
