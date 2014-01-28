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
