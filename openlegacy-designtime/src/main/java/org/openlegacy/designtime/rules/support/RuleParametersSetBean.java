package org.openlegacy.designtime.rules.support;

import org.openlegacy.designtime.rules.RuleParametersSet;

import java.util.HashMap;
import java.util.Map;

public class RuleParametersSetBean implements RuleParametersSet {

	private String ruleId;
	private Map<String, Object> ruleParameters = new HashMap<String, Object>();

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public Map<String, Object> getRuleParameters() {
		return ruleParameters;
	}

	public void setRuleParameters(Map<String, Object> ruleParameters) {
		this.ruleParameters = ruleParameters;
	}
}
