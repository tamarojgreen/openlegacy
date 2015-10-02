package org.openlegacy.log;

import org.openlegacy.SessionAction;

import java.util.List;


public interface ApiLogger {

	public static enum Level {

		INFO,
		TRACE,
		ERROR,
		WARNING
	}

	void log(Class<? extends SessionAction<?>> actionClass, Object entity, List<Object> entityKeys);

	void error(Exception e, Class<? extends SessionAction<?>> action, Object entity);

	void warning(String message);

}
