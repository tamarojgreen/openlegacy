/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.rules.support;

import org.openlegacy.designtime.rules.RuleDefinition;
import org.openlegacy.designtime.rules.RuleParametersSet;

import java.util.ArrayList;
import java.util.List;

public class RuleDefinitionBean implements RuleDefinition {

	private String droolsFile;
	private List<RuleParametersSet> ruleParameterSets = new ArrayList<RuleParametersSet>();
	private String ruleId;
	private boolean enabled = true;

	@Override
	public String getDroolsFile() {
		return droolsFile;
	}

	public void setDroolsFile(String droolsFile) {
		this.droolsFile = droolsFile;
	}

	@Override
	public List<RuleParametersSet> getRuleParameterSets() {
		return ruleParameterSets;
	}

	public void setRuleParameterSets(List<RuleParametersSet> ruleParameterSets) {
		this.ruleParameterSets = ruleParameterSets;
	}

	@Override
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	@Override
	public boolean isEnable() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
