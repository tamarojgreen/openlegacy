package org.openlegacy.terminal.support.wait_conditions;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.wait_conditions.WaitCoditionAdapter;
import org.openlegacy.terminal.wait_conditions.WaitConditionFactory;

import java.lang.reflect.Constructor;

public class DefaultWaitConditionFactory implements WaitConditionFactory {

	private long defaultWaitInterval;
	private long defaultWaitTimeout;

	/**
	 * Invoke the given waitClass constructor which matches the matching argument types
	 */
	@SuppressWarnings("unchecked")
	public <T extends WaitCoditionAdapter> T create(Class<T> waitClass, Object... args) {
		WaitCoditionAdapter instance;
		try {
			Class<?>[] argTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			Constructor<T> constructor = waitClass.getConstructor(argTypes);
			instance = constructor.newInstance(args);
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}
		instance.setWaitInterval(defaultWaitInterval);
		instance.setWaitTimeout(defaultWaitTimeout);

		return (T)instance;
	}

	public void setDefaultWaitInterval(long defaultWaitInterval) {
		this.defaultWaitInterval = defaultWaitInterval;
	}

	public void setDefaultWaitTimeout(long defaultWaitTimeout) {
		this.defaultWaitTimeout = defaultWaitTimeout;
	}
}
