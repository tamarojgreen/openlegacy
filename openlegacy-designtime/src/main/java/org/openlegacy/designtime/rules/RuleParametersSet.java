package org.openlegacy.designtime.rules;

import java.util.Map;

public interface RuleParametersSet {

	String getRuleId();

	Map<String, Object> getRuleParameters();
}
