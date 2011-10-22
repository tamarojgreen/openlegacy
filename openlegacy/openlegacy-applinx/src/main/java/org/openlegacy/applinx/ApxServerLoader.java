package org.openlegacy.applinx;

import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXCreateSessionRequest;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.common.designtime.exceptions.GXConfigurationStorageException;
import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;
import com.sabratec.applinx.common.designtime.model.GXIApplicationContext;
import com.sabratec.applinx.common.designtime.model.config.GXApplicationConfiguration;
import com.sabratec.applinx.common.designtime.model.config.GXPreDefinedType;
import com.sabratec.applinx.common.designtime.model.db.GXInnerDBConfiguration;
import com.sabratec.applinx.server.runtime.GXServerContext;
import com.sabratec.util.GXSystem;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ApxServerLoader {

	private GXServerContext server;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private GXApplicationConfiguration apxConfig;

	private Properties properties;

	private Resource license;

	public void startServer() throws Exception {

		Resource resource = new ClassPathResource("/applinx.properties");
		properties = PropertiesLoaderUtils.loadProperties(resource);

		GXSystem.setProperty("com.softwareag.disablehttp", "true");

		initFiles();

		loadLogger();
		server = GXServerContext.instance();

		if (!server.isStarted()) {
			server.start(null);
			createDefaultApplication();

			loadLogger();
		}

	}

	private static void loadLogger() throws IOException {
		Resource resource = new ClassPathResource("/log4j.properties");
		if (resource.exists()) {
			Properties logProperties = PropertiesLoaderUtils.loadProperties(resource);
			new PropertyConfigurator().doConfigure(logProperties, LogManager.getLoggerRepository());
		}
	}

	private void createDefaultApplication() throws GXDesignTimeException {
		deleteApplicationIfNeeded();
		addApplicationIfNeeded();
	}

	private void addApplicationIfNeeded() throws GXDesignTimeException {
		if (getApplication() == null) {
			((GXInnerDBConfiguration)apxConfig.getRepositoryConfig().getDb()).setApplicationName(apxConfig.getName());
			server.addApplication(apxConfig, GXPreDefinedType.NONE);
		}
	}

	private void initFiles() throws IOException, FileNotFoundException {
		File workingDir = initWorkingDir();
		initLicense(workingDir);
		initConfigFiles(workingDir);
	}

	private static void initFile(File tempDir, String fileName) throws IOException {
		Resource fileResource = new ClassPathResource(fileName);
		initResource(tempDir, fileName, fileResource);
	}

	private static void initResource(File tempDir, String fileName, Resource fileResource) throws IOException,
			FileNotFoundException {
		File targetFile = new File(tempDir, fileName);
		targetFile.getParentFile().mkdirs();
		IOUtils.copy(fileResource.getInputStream(), new FileOutputStream(targetFile));
	}

	private static void initConfigFiles(File workingDir) throws IOException {
		initFile(workingDir, "/config/gxconfig.xml");
		initFile(workingDir, "/config/gxperm.cfg");
		initFile(workingDir, "/config/gxserver.cfg");
		initFile(workingDir, "/config/log/gxlog_config.xml");
	}

	private void initLicense(File workingDir) throws IOException {
		initResource(workingDir, license.getFilename(), license);
		System.setProperty("com.sabratec.license", workingDir.getAbsolutePath());
	}

	private static File initWorkingDir() {
		File workingDir = new File(System.getProperty("user.home") + "/.applinx");
		workingDir.mkdirs();
		GXSystem.setProperty("com.sabratec.gxhome", workingDir.getAbsolutePath());

		return workingDir;
	}

	private void deleteApplicationIfNeeded() {
		GXIApplicationContext applinxApplication = getApplication();
		if (applinxApplication != null && "true".equals(properties.get("applinx.use.repository"))) {
			return;
		}

		try {
			if (applinxApplication != null) {
				server.deleteApplication(applinxApplication, true, true);
			}
		} catch (GXConfigurationStorageException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public GXIApplicationContext getApplication() {
		GXIApplicationContext applinxApplication = server.getApplicationByName(apxConfig.getName());
		return applinxApplication;
	}

	public void stopServer() {
		if (server.isStarted()) {
			server.stop();
		}
	}

	public boolean isRunning() {
		return server != null;
	}

	public GXIClientBaseObject getSession() throws GXGeneralException {
		GXCreateSessionRequest sessionRequest = applicationContext.getBean(GXCreateSessionRequest.class);
		sessionRequest.setServerContext(server.getAppServerContext());
		GXIClientBaseObject baseObject;

		baseObject = GXClientBaseObjectFactory.getBaseObject(sessionRequest);

		return baseObject;
	}

	public void setLicense(Resource license) {
		this.license = license;
	}

	public GXServerContext getServer() {
		return server;
	}

}
