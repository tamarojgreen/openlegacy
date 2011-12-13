package org.openlegacy.designtime.rules;

import java.util.List;

public interface RuleDefinition {

	String getDroolsFile();

	String getRuleId();

	List<RuleParametersSet> getRuleParameterSets();

	boolean isEnable();

}
