package org.openlegacy.terminal.wait_conditions;

public interface WaitConditionFactory {

	/**
	 * Initialize the given wait condition class with factory defaults and the given args. The waitClass should have a constructor
	 * with a matching argument types
	 * 
	 * @param waitClass
	 * @param args
	 * @return
	 */
	<T extends WaitCoditionAdapter> T create(Class<T> waitClass, Object... args);
}