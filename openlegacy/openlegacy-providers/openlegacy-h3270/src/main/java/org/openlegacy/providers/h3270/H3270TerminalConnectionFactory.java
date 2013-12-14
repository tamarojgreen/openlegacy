package org.openlegacy.providers.h3270;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h3270.host.S3270;
import org.h3270.logicalunit.LogicalUnitException;
import org.h3270.logicalunit.LogicalUnitPool;
import org.h3270.logicalunit.LogicalUnitPoolFactory;
import org.h3270.render.H3270Configuration;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.utils.FeatureChecker;
import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.OsUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

public class H3270TerminalConnectionFactory implements TerminalConnectionFactory, InitializingBean {

	private LogicalUnitPool logicalUnitPool;

	private Configuration configuration;

	private final Log logger = LogFactory.getLog(H3270TerminalConnectionFactory.class);

	private Properties properties;

	private Boolean convertToLogical;

	public TerminalConnection getConnection(ConnectionProperties connectionProperties) {
		try {
			// TODO set device
			S3270 s3270Session = new S3270(leaseLogicalUnit(), properties.getProperty("host.name") + ":"
					+ properties.getProperty("host.port"), properties);
			return new H3270Connection(s3270Session, convertToLogical);
		} catch (LogicalUnitException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public void disconnect(TerminalConnection terminalConnection) {
		S3270 delegate = (S3270)terminalConnection.getDelegate();
		releaseLogicalUnit(delegate.getLogicalUnit());
		delegate.disconnect();
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

	private static File initWorkingDir() {
		File workingDir = new File(System.getProperty("user.home") + "/.openlegacy/.h3270");
		workingDir.mkdirs();
		return workingDir;
	}

	private static File initResource(File tempDir, String targetFileName, Resource fileResource, Map<String, String> keysValues)
			throws IOException, FileNotFoundException {
		File targetFile = new File(tempDir, targetFileName);
		targetFile.getParentFile().mkdirs();
		FileUtils.copyAndReplace(fileResource.getInputStream(), new FileOutputStream(targetFile), keysValues);
		return targetFile;

	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(properties, "properties not set for " + getClass().getName());
		initialize();
	}

	private void initialize() throws ConfigurationException, IOException {

		if (convertToLogical == null) {
			if (FeatureChecker.isSupportBidi()) {
				logger.info("Found com.ibm.icu library in the classpath. activating convert to logical. To disable define convertToLogical property to false");
				convertToLogical = true;
			} else {
				convertToLogical = false;
			}
		}

		File targetFile = initH3270ConfigWorkingCopy();

		initConfiguration(targetFile);

		if (OsUtils.isUnix()) {
			initResource("/s3270");
		} else {
			initResource("/s3270.exe");
			initResource("/cygwin1.dll");
		}

		properties.put("s3270.execPath", targetFile.getParent());

		logicalUnitPool = LogicalUnitPoolFactory.createLogicalUnitPool(configuration);
	}

	private void initResource(String resourceName) throws IOException {
		File targetFile = new File(initWorkingDir(), resourceName);
		targetFile.getParentFile().mkdirs();
		if (!targetFile.exists()) {
			IOUtils.copy(getClass().getResourceAsStream(resourceName), new FileOutputStream(targetFile));
		}
	}

	private void initConfiguration(File targetFile) {
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		try {
			configuration = H3270Configuration.create(new FileInputStream(targetFile));
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	private File initH3270ConfigWorkingCopy() throws IOException {
		String fileName = "/h3270-config.xml";
		Resource h3270ConfigResource = new ClassPathResource(fileName);
		Map keysValues = new HashedMap();
		File workingDir = initWorkingDir();

		logger.info(MessageFormat.format("*** Initializing H3270 configuration files in: {0}", workingDir));

		String hostProperties = "/host.properties";
		Resource hostPropertiesResource = new ClassPathResource(hostProperties);
		Properties properties = PropertiesLoaderUtils.loadProperties(hostPropertiesResource);

		keysValues.put("H3270_HOME", workingDir.getAbsolutePath().replace("\\", "\\\\"));
		keysValues.put("CHARSET", "cp" + properties.getProperty("host.codePage"));
		File configFile = initResource(workingDir, fileName, h3270ConfigResource, keysValues);
		return configFile;
	}

	public void setConvertToLogical(Boolean convertToLogical) {
		this.convertToLogical = convertToLogical;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
