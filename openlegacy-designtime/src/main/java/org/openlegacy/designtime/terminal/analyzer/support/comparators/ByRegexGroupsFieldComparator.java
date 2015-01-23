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
package org.openlegacy.designtime.terminal.analyzer.support.comparators;

import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.terminal.TerminalField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Orders by fields that match the given pattern first.
 * Matches are ranked by the number of groups as well.
 */
public class ByRegexGroupsFieldComparator implements BestEntityNameFieldComparator {

	Pattern pattern=null;
	
	@Override
	public int compare(TerminalField field1, TerminalField field2) {
		if(pattern == null){
			return 0;
		}
		Matcher matcher1 = pattern.matcher(field1.getValue());
		Matcher matcher2 = pattern.matcher(field2.getValue());
		int field1count= 0;
		if(matcher1.matches()){
			field1count = matcher1.groupCount() + 1;
		}
		int field2count= 0;
		if(matcher2.matches()){
			field2count = matcher2.groupCount() + 1;
		}
		return field2count - field1count;
	}
	
	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

}
