package org.openlegacy.providers.h3270;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.h3270.host.S3270;
import org.h3270.logicalunit.LogicalUnitException;
import org.h3270.logicalunit.LogicalUnitPool;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;

public class H3270TerminalConnectionFactory implements TerminalConnectionFactory, InitializingBean {

	private LogicalUnitPool logicalUnitPool;

	private String hostName;

	private Configuration configuration;

	public TerminalConnection getConnection() {
		try {

			S3270 s3270Session = new S3270(leaseLogicalUnit(), "localhost", configuration);
			return new H3270Connection(s3270Session);
		} catch (LogicalUnitException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public void disconnect(TerminalConnection terminalConnection) {
		((S3270)terminalConnection.getDelegate()).disconnect();
	}

	private String leaseLogicalUnit() throws LogicalUnitException {
		if (logicalUnitPool != null) {
			return logicalUnitPool.leaseLogicalUnit();
		}
		return null;
	}

	private void releaseLogicalUnit(String logicalUnit) {
		if (logicalUnit != null) {
			logicalUnitPool.releaseLogicalUnit(logicalUnit);
		}
	}

	public void afterPropertiesSet() throws Exception {
		initialize();
	}

	private void initialize() {
		InputStream configurationResource = getClass().getResourceAsStream("/h3270-config.xml");
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		try {
			configuration = builder.build(configurationResource);
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}

		// logicalUnitPool = LogicalUnitPoolFactory.createLogicalUnitPool(configuration);
	}

}
