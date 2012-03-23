package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXCreateSessionRequest;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;
import com.sabratec.applinx.common.designtime.model.GXIApplicationContext;
import com.sabratec.applinx.server.runtime.GXServerContext;
import com.sabratec.util.GXSystem;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.inject.Inject;

public class ApxServerLoader implements InitializingBean, DisposableBean {

	private GXServerContext server;

	@Inject
	private ApplicationContext applicationContext;

	private Properties properties;

	private Resource license;

	private Resource applicationConfiguration;

	private Resource serverConfiguration;

	private Resource applicationRepository;

	private final Log logger = LogFactory.getLog(ApxServerLoader.class);

	public void startServer() throws Exception {

		if (server != null) {
			return;
		}

		Resource resource = new ClassPathResource("/applinx.properties");
		properties = PropertiesLoaderUtils.loadProperties(resource);

		GXSystem.setProperty("com.softwareag.disablehttp", "true");

		initFiles();

		loadLogger();
		server = GXServerContext.instance();

		if (!server.isStarted()) {
			server.start(null);

			loadLogger();
		}

		initFile(initWorkingDir(), "/config/log/gxlog_config.apx", "/config/log/gxlog_config.xml");

		importRepository();

	}

	private void importRepository() {
		if (applicationRepository != null) {
			try {
				ApxUtils.importRepository(this, applicationRepository.getURL());
			} catch (Exception e) {
				throw (new OpenLegacyRuntimeException(e));
			}
		}
	}

	private static void loadLogger() throws IOException {
		Resource resource = new ClassPathResource("/log4j.properties");
		if (resource.exists()) {
			Properties logProperties = PropertiesLoaderUtils.loadProperties(resource);
			new PropertyConfigurator().doConfigure(logProperties, LogManager.getLoggerRepository());
		}
	}

	private void initFiles() throws IOException, FileNotFoundException {
		File workingDir = initWorkingDir();

		logger.info(MessageFormat.format("*** Initializing Applinx configuration files in: {0}", workingDir));

		initLicense(workingDir);
		initConfigFiles(workingDir);

	}

	private static void initFile(File tempDir, String fileName, String targetFileName) throws IOException {
		if (targetFileName == null) {
			targetFileName = fileName;
		}

		Resource fileResource = new ClassPathResource(fileName);

		if (fileResource.exists()) {
			initResource(tempDir, targetFileName, fileResource);
		}
	}

	private static void initResource(File tempDir, String targetFileName, Resource fileResource) throws IOException,
			FileNotFoundException {
		File targetFile = new File(tempDir, targetFileName);
		targetFile.getParentFile().mkdirs();
		IOUtils.copy(fileResource.getInputStream(), new FileOutputStream(targetFile));
	}

	private void initConfigFiles(File workingDir) throws IOException {
		initResource(workingDir, "/config/gxconfig.xml", serverConfiguration);
		initFile(workingDir, "/config/gxperm.cfg", null);
		initFile(workingDir, "/config/gxserver.cfg", null);

		initApplication(workingDir);
	}

	private void initApplication(File workingDir) throws IOException, FileNotFoundException {
		initResource(workingDir,
				MessageFormat.format("/host-applications/{0}/gxconfig.xml", properties.getProperty("applinx.application.name")),
				applicationConfiguration);

		initFile(
				workingDir,
				"config/ApplinX.h2.db",
				MessageFormat.format("/host-applications/{0}/db/repository/ApplinX.h2.db",
						properties.getProperty("applinx.application.name")));

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

	public GXIApplicationContext getApplication() {
		GXIApplicationContext applinxApplication = server.getApplicationByName(properties.getProperty("applinx.application.name"));
		return applinxApplication;
	}

	public synchronized void stopServer() {
		if (server == null) {
			return;
		}
		if (server.isStarted()) {
			server.stop();
		}
		server = null;
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

	public void setApplicationConfiguration(Resource applicationConfiguration) {
		this.applicationConfiguration = applicationConfiguration;
	}

	public void setServerConfiguration(Resource serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
	}

	public void setApplicationRepository(Resource applicationRepository) {
		this.applicationRepository = applicationRepository;
	}

	public void destroy() throws Exception {
		stopServer();

	}

	public void afterPropertiesSet() throws Exception {
		try {
			startServer();
		} catch (Exception e) {
			throw (new RuntimeException(e));
		}

	}
}
