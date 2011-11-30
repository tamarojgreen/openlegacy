package org.openlegacy.modules.table.drilldown;

import org.openlegacy.exceptions.OpenLegacyException;

/**
 * This exception is thrown typically when unable to drill-down a table in a session
 * 
 */
public class DrilldownException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public DrilldownException(String s) {
		super(s);
	}
}
