package org.openlegacy.designtime.rpc.model.support;

import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcPartEntityDefinition;
import org.openlegacy.utils.StringUtil;

import java.util.Map;

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

	public void convertToExternal(Map<String, String> localToExternal) {
		Map<String, PartEntityDefinition<RpcFieldDefinition>> parts = getPartsDefinitions();
		for (String partName : parts.keySet()) {
			if (localToExternal.containsKey(partName)) {
				SimpleRpcPartEntityDefinition part = (SimpleRpcPartEntityDefinition)parts.get(partName);
				part.setExternalName(StringUtil.toClassName(localToExternal.get(partName)));

			}
		}

	}

}
