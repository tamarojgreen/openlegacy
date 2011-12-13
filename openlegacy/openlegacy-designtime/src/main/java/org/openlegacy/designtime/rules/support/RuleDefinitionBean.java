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

	public String getDroolsFile() {
		return droolsFile;
	}

	public void setDroolsFile(String droolsFile) {
		this.droolsFile = droolsFile;
	}

	public List<RuleParametersSet> getRuleParameterSets() {
		return ruleParameterSets;
	}

	public void setRuleParameterSets(List<RuleParametersSet> ruleParameterSets) {
		this.ruleParameterSets = ruleParameterSets;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public boolean isEnable() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
