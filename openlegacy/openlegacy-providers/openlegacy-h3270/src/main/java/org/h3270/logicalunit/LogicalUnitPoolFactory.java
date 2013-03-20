package org.h3270.logicalunit;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h3270.render.H3270Configuration;

public class LogicalUnitPoolFactory {

	protected final static Log logger = LogFactory.getLog(LogicalUnitPoolFactory.class);

	public static LogicalUnitPool createLogicalUnitPool(H3270Configuration configuration) {
		String luBuilderClass = configuration.getLogicalUnitBuilderClass();
		try {
			return createLogicalUnitPool(luBuilderClass);
		} catch (ConfigurationException e) {
			throw (new RuntimeException(e));
		}
	}

	public static LogicalUnitPool createLogicalUnitPool(Configuration configuration) throws ConfigurationException {
		String luBuilderClass = createLogicalUnitBuilderClass(configuration);
		return createLogicalUnitPool(luBuilderClass);
	}

	public static LogicalUnitPool createLogicalUnitPool(String luBuilderClass) throws ConfigurationException {
		if (luBuilderClass != null) {
			try {
				LogicalUnitBuilder builder = (LogicalUnitBuilder)Class.forName(luBuilderClass).newInstance();
				return new LogicalUnitPool(builder);
			} catch (Exception e) {
				String message = "Cannot create an instance of class '" + luBuilderClass
						+ "'. See h3270 configuration of logical units.";
				logger.error(message);
				throw new RuntimeException(message, e);
			}
		}
		return null;
	}

	public static String createLogicalUnitBuilderClass(Configuration config) throws ConfigurationException {
		// boolean usePool = config.getChild("logical-units").getChild("use-pool").getValueAsBoolean();
		// if (usePool) {
		// return config.getChild("lu-builder").getValue();
		// }
		return null;
	}
}
