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
package org.openlegacy.designtime.mains;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.annotations.db.DbEntity;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.designtime.DesigntimeConstants;
import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzer;
import org.openlegacy.designtime.analyzer.TextTranslator;
import org.openlegacy.designtime.db.generators.DbEntityPageGenerator;
import org.openlegacy.designtime.db.generators.DbEntityServiceGenerator;
import org.openlegacy.designtime.db.generators.DbPojosAjGenerator;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;
import org.openlegacy.designtime.db.generators.support.DbCodeBasedDefinitionUtils;
import org.openlegacy.designtime.enums.BackendSolution;
import org.openlegacy.designtime.generators.EntityPageGenerator;
import org.openlegacy.designtime.generators.EntityServiceGenerator;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.generators.SpaGenerateUtil;
import org.openlegacy.designtime.newproject.ITemplateFetcher;
import org.openlegacy.designtime.newproject.model.ProjectTheme;
import org.openlegacy.designtime.rpc.GenerateRpcModelRequest;
import org.openlegacy.designtime.rpc.ImportSourceRequest;
import org.openlegacy.designtime.rpc.generators.RpcEntityPageGenerator;
import org.openlegacy.designtime.rpc.generators.RpcEntityServiceGenerator;
import org.openlegacy.designtime.rpc.generators.RpcPojosAjGenerator;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcCodeBasedDefinitionUtils;
import org.openlegacy.designtime.rpc.model.support.SimpleRpcEntityDesigntimeDefinition;
import org.openlegacy.designtime.rpc.source.CodeParser;
import org.openlegacy.designtime.rpc.source.CodeParserFactory;
import org.openlegacy.designtime.rpc.source.parsers.CobolLocalPartNamesFethcher;
import org.openlegacy.designtime.rpc.source.parsers.CobolNameRecognizer;
import org.openlegacy.designtime.rpc.source.parsers.CopyBookFetcher;
import org.openlegacy.designtime.rpc.source.parsers.OpenLegacyParseException;
import org.openlegacy.designtime.rpc.source.parsers.ParseResults;
import org.openlegacy.designtime.terminal.GenerateScreenModelRequest;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsAnalyzer;
import org.openlegacy.designtime.terminal.generators.ScreenEntityPageGenerator;
import org.openlegacy.designtime.terminal.generators.ScreenEntityServiceGenerator;
import org.openlegacy.designtime.terminal.generators.ScreenPojosAjGenerator;
import org.openlegacy.designtime.terminal.generators.TrailJunitGenerator;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenCodeBasedDefinitionUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.SourceFetcher;
import org.openlegacy.rpc.actions.RpcActions;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcActionDefinition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TrailObfuscator;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.modules.trail.TerminalPersistedTrail;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotXmlRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotTextRenderer;
import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.OsUtils;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.Entity;

/**
 * OpenLegacy main design-time API entry point. Consolidate all design-time common UI actions
 * 
 * @see DesignTimeExecuter
 */
public class DesignTimeExecuterImpl implements DesignTimeExecuter {

	private static final String APPLICATION_PROPERTIES = "src/main/resources/application.properties";

	private final static Log logger = LogFactory.getLog(DesignTimeExecuterImpl.class);

	private static final String DEFAULT_SPRING_TEST_CONTEXT_FILE = "/src/main/resources/META-INF/spring/applicationContext-test.xml";
	private static final String DEFAULT_SPRING_WEB_CONTEXT_FILE = "/src/main/webapp/WEB-INF/spring/webmvc-config.xml";

	private static final String mockupSessionCommentStart = "<!-- Use this definition to replay a mock-up session application";
	private static final String mockupSessionCommentEnd = "End use this definition to replay a mock-up session application -->";

	public static final String TEMPLATES_DIR = "templates";

	private static final String DEFAULT_TEMPLATES_PATTERN = "classpath*:**/*.template";

	private static final String DEFAULT_NEW_PROJECT_VERSION = "0.1";

	private static final String TEST_SOURCE_DIR = "src/test/java";

	private static final String PREFERENCES_FILE = ".preferences";

	private static final Object DEFAULT_CONTEXT = "default";

	private static final String dependencyCommentStart = "<!-- commented dependency start";
	private static final String dependencyCommentEnd = "commented dependency end -->";

	private static final String bidiCommentStart = "<!-- Uncomment for bidi support";
	private static final String bidiCommentEnd = "End uncomment for bidi support -->";

	private static final String USER_PROPERTIES_FOLDER_NAME = "userPropertiesFolderName";

	private ApplicationContext defaultDesigntimeApplicationContext;

	// map of project path to Spring application context
	private Map<String, ApplicationContext> projectsDesigntimeAplicationContexts = new HashMap<String, ApplicationContext>();

	private Map<File, ProjectPreferences> projectsPreferences = new HashMap<File, ProjectPreferences>();

	private static final String DELETE_THIS_DEFINITION_TO_REPLAY_A_MOCK_UP_SESSION_APPLICATION = "<!-- Delete this definition to replay a mock-up session application -->";
	private static final String END_DELETE_THIS_DEFINITION_TO_REPLAY_A_MOCK_UP_SESSION_APPLICATION = "<!-- End delete this definition to replay a mock-up session application -->";

	private static final String INDEX_JSP_PATH = "src/main/webapp/app/index.jsp";
	private static final String PERSISTENCE_XML_PATH = "src/main/resources/META-INF/persistence.xml";
	private static final String PACKAGE_DIR = "/src/main/java/";

	@Override
	public void createProject(ProjectCreationRequest projectCreationRequest) throws IOException {
		ITemplateFetcher templateFetcher = projectCreationRequest.getTemplateFetcher();

		File targetPath = templateFetcher.fetchZip(projectCreationRequest.getTemplateName(),
				projectCreationRequest.getProjectName(), projectCreationRequest.getBaseDir());

		if (projectCreationRequest.isDemo()) {
			templateFetcher.deleteZip();
			return;
		}

		// maven files
		renameProjectProperties(projectCreationRequest.getProjectName(), targetPath);
		if (projectCreationRequest.getBackendSolution().equals("JDBC")) {
			addDbDriverDependency(targetPath, projectCreationRequest.getDbDriverMavenDependency());
			setDbDdlAutoValue(projectCreationRequest.getDbDdlAuto(), targetPath);
			setDbDialect(projectCreationRequest.getDbDialect(), targetPath);
		} else {
			renameProviderInPOM(projectCreationRequest.getProvider(), targetPath);
			uncommentDependencies(targetPath);
		}

		if (projectCreationRequest.isSupportTheme()) {
			renameThemeInPOM(projectCreationRequest.getProjectTheme(), targetPath);
			renameThemeInAppProperties(projectCreationRequest.getProjectTheme(), targetPath);
			renameThemeInIndexJSP(projectCreationRequest.getProjectTheme(), projectCreationRequest.isRightToLeft(), targetPath);
		}

		// spring files
		updateSpringContextWithDefaultPackage(projectCreationRequest.getDefaultPackageName(), targetPath);
		uncommentMockSettings(projectCreationRequest.getProvider(), targetPath);

		// eclipse files
		renameProject(projectCreationRequest.getProjectName(), targetPath);
		renameLaunchers(projectCreationRequest.getProjectName(), targetPath);

		updatePropertiesFile(projectCreationRequest, targetPath);

		updateConfigJsFile(projectCreationRequest, targetPath);

		savePreference(targetPath, PreferencesConstants.API_PACKAGE, projectCreationRequest.getDefaultPackageName());
		savePreference(targetPath, PreferencesConstants.WEB_PACKAGE, projectCreationRequest.getDefaultPackageName() + ".web");
		savePreference(targetPath, PreferencesConstants.DESIGNTIME_CONTEXT, "default");
		savePreference(targetPath, PreferencesConstants.USE_AJ, "1");
		String backendSolution = (projectCreationRequest.getBackendSolution() != null) ? projectCreationRequest.getBackendSolution().toUpperCase()
				: "SCREEN";
		if (StringUtils.equals(backendSolution, "SCREENS")) {
			backendSolution = "SCREEN";
		}
		savePreference(targetPath, PreferencesConstants.BACKEND_SOLUTION, backendSolution);

		String frontendSolution = (projectCreationRequest.getFrontendSolution() != null) ? projectCreationRequest.getFrontendSolution().toUpperCase()
				: "REST";

		if (StringUtils.contains(frontendSolution, "REST") || StringUtils.contains(frontendSolution, "Mobile")) {
			frontendSolution = "REST";
		}

		savePreference(targetPath, PreferencesConstants.FRONTEND_SOLUTION, frontendSolution);

		savePreference(targetPath, PreferencesConstants.SUPPORT_RESTFUL_SERVICE, String.valueOf(
				projectCreationRequest.isRestFulService()).toUpperCase());

		if (projectCreationRequest.isRightToLeft()) {
			handleRightToLeft(targetPath);
		}

		createDefaultPackage(projectCreationRequest.getDefaultPackageName(), targetPath);

		handleTrailFilePath(targetPath, projectCreationRequest);

		if (StringUtils.equalsIgnoreCase(frontendSolution, "integration")) {
			configureIntegrationProject(targetPath, projectCreationRequest);
		}

		templateFetcher.deleteZip();
	}

	private static void updateConfigJsFile(ProjectCreationRequest projectCreationRequest, File targetPath) throws IOException {
		File configJs = new File(targetPath, SpaGenerateUtil.JS_APP_DIR + "config.js");
		if (configJs.exists()) {
			String fileContent = IOUtils.toString(new FileInputStream(configJs));

			fileContent = fileContent.replaceFirst(projectCreationRequest.getTemplateName(),
					projectCreationRequest.getProjectName());

			FileUtils.write(fileContent, configJs);
		}
	}

	private static void handleTrailFilePath(File targetPath, ProjectCreationRequest projectCreationRequest)
			throws FileNotFoundException, IOException {
		String projectName = projectCreationRequest.getProjectName();
		String trailFilePath = projectCreationRequest.getBaseDir().getAbsolutePath() + "/" + projectName
				+ "/src/main/resources/trails/" + projectName + ".trail";
		copyTrailFilePath(projectCreationRequest.getTrailContent(), trailFilePath);

		File appPropertiesFile = new File(targetPath, APPLICATION_PROPERTIES);

		String appPropertiesFileContent = IOUtils.toString(new FileInputStream(appPropertiesFile));
		if (!appPropertiesFile.exists()) {
			return;
		}
		boolean liveSession = true;
		if (projectCreationRequest.getTrailContent() != null) {
			liveSession = false;
		}
		appPropertiesFileContent = appPropertiesFileContent.replaceFirst("openLegacyProperties.liveSession=.*",
				MessageFormat.format("openLegacyProperties.liveSession={0}", liveSession));
		appPropertiesFileContent = appPropertiesFileContent.replaceFirst("openLegacyProperties.trailFilePath=.*",
				MessageFormat.format("openLegacyProperties.trailFilePath=/trails/{0}.trail", projectName));

		FileUtils.write(appPropertiesFileContent, appPropertiesFile);
	}

	private static void copyTrailFilePath(byte[] content, String targetTrailFilePath) throws IOException {

		if (content == null) {
			return;
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(targetTrailFilePath);
			out.write(content);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	private static void handleRightToLeft(File targetPath) throws FileNotFoundException, IOException {
		File designtimeContext = new File(targetPath, DesignTimeExecuter.CUSTOM_DESIGNTIME_CONTEXT_RELATIVE_PATH);

		if (designtimeContext.exists()) {
			String designtimeContextFileContent = IOUtils.toString(new FileInputStream(designtimeContext));

			designtimeContextFileContent = designtimeContextFileContent.replaceFirst("openlegacy-default-designtime-context",
					"openlegacy-rtl-designtime-context");
			FileUtils.write(designtimeContextFileContent, designtimeContext);
		} else {
			logger.error(MessageFormat.format("Unable to find openlegacy-designtime-context.xml within {0}", targetPath));
		}

		removeComment(new File(targetPath, "pom.xml"), bidiCommentStart, bidiCommentEnd);
		removeComment(new File(targetPath, DesigntimeConstants.DEFAULT_SPRING_CONTEXT_FILE), bidiCommentStart, bidiCommentEnd);
		removeComment(new File(targetPath, DEFAULT_SPRING_TEST_CONTEXT_FILE), bidiCommentStart, bidiCommentEnd);

		File appPropertiesFile = new File(targetPath, APPLICATION_PROPERTIES);

		if (!appPropertiesFile.exists()) {
			return;
		}

		String appPropertiesFileContent = IOUtils.toString(new FileInputStream(appPropertiesFile));
		appPropertiesFileContent = appPropertiesFileContent.replaceFirst("#openLegacyProperties.rightToLeft=.*",
				"openLegacyProperties.rightToLeft=true");
		FileUtils.write(appPropertiesFileContent, appPropertiesFile);

		File preferenceFile = new File(targetPath, PREFERENCES_FILE);
		String preferenceContent = IOUtils.toString(new FileInputStream(preferenceFile));
		preferenceContent = preferenceContent.replaceFirst("DESIGNTIME_CONTEXT=.*", "DESIGNTIME_CONTEXT=rtl");
		FileUtils.write(preferenceContent, preferenceFile);
	}

	private static void uncommentDependencies(File targetPath) throws IOException {
		File pomFile = new File(targetPath, "pom.xml");
		removeComment(pomFile, dependencyCommentStart, dependencyCommentEnd);
	}

	private static void removeComment(File file, String commentStart, String commentEnd) throws IOException {
		if (!file.exists()) {
			return;
		}
		String fileContent = IOUtils.toString(new FileInputStream(file));

		fileContent = fileContent.replaceAll(commentStart, "");
		fileContent = fileContent.replaceAll(commentEnd, "");

		FileUtils.write(fileContent, file);
	}

	private static void uncommentMockSettings(String provider, File targetPath) throws IOException {
		if (provider == null || (!provider.equals("mock-up") && !provider.equals("openlegacy-impl"))) {
			return;
		}

		File springContextFile = new File(targetPath, "src/main/resources/META-INF/spring/applicationContext.xml");
		removeComment(springContextFile, mockupSessionCommentStart, mockupSessionCommentEnd);

		configureMockupSession(new File(targetPath, "src/main/resources/META-INF/spring/applicationContext-test.xml"));
		configureMockupSession(new File(targetPath, "src/main/resources/META-INF/spring/emulationContext.xml"));

		File appPropertiesFile = new File(targetPath, APPLICATION_PROPERTIES);
		String appPropertiesFileContent = IOUtils.toString(new FileInputStream(appPropertiesFile));
		appPropertiesFileContent = appPropertiesFileContent.replaceFirst("#screenEntityUtils.returnTrueOnDifferentKeys=.*",
				"screenEntityUtils.returnTrueOnDifferentKeys=true");
		FileUtils.write(appPropertiesFileContent, appPropertiesFile);

	}

	private static void configureMockupSession(File springContextFile) throws IOException {
		if (!springContextFile.exists()) {
			return;
		}
		removeComment(springContextFile, mockupSessionCommentStart, mockupSessionCommentEnd);

		String springTestFileContent = org.apache.commons.io.FileUtils.readFileToString(springContextFile);
		int startIndex = springTestFileContent.indexOf(DELETE_THIS_DEFINITION_TO_REPLAY_A_MOCK_UP_SESSION_APPLICATION) - 1;
		int endIndex = springTestFileContent.indexOf(END_DELETE_THIS_DEFINITION_TO_REPLAY_A_MOCK_UP_SESSION_APPLICATION)
				+ END_DELETE_THIS_DEFINITION_TO_REPLAY_A_MOCK_UP_SESSION_APPLICATION.length() + 1;

		if (startIndex > 0 && endIndex > 0) {
			springTestFileContent = springTestFileContent.substring(0, startIndex) + springTestFileContent.substring(endIndex);
			org.apache.commons.io.FileUtils.write(springContextFile, springTestFileContent);
		}
	}

	private static void updatePropertiesFile(ProjectCreationRequest projectCreationRequest, File targetPath) throws IOException,
			FileNotFoundException {
		File hostPropertiesFile = new File(targetPath, "src/main/resources/host.properties");
		if (hostPropertiesFile.exists()) {
			String hostPropertiesFileContent = IOUtils.toString(new FileInputStream(hostPropertiesFile));

			hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.name=.*", MessageFormat.format(
					"host.name={0}", projectCreationRequest.getHostName()));

			hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.port=.*", MessageFormat.format(
					"host.port={0}", String.valueOf(projectCreationRequest.getHostPort())));
			hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.codePage=.*", MessageFormat.format(
					"host.codePage={0}", projectCreationRequest.getCodePage()));

			hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.sslType=.*", MessageFormat.format(
					"host.sslType={0}", projectCreationRequest.getSslType()));

			FileUtils.write(hostPropertiesFileContent, hostPropertiesFile);
		}

		File rpcPropertiesFile = new File(targetPath, "src/main/resources/rpc.properties");
		if (rpcPropertiesFile.exists()) {
			String rpcPropertiesFileContent = IOUtils.toString(new FileInputStream(rpcPropertiesFile));

			rpcPropertiesFileContent = rpcPropertiesFileContent.replaceFirst("rpc.host.name=.*", MessageFormat.format(
					"rpc.host.name={0}", projectCreationRequest.getHostName()));

			FileUtils.write(rpcPropertiesFileContent, rpcPropertiesFile);
		}
		File appPropertiesFile = new File(targetPath, "src/main/resources/application.properties");
		if (appPropertiesFile.exists()) {
			String appPropertiesFileContent = IOUtils.toString(new FileInputStream(appPropertiesFile));

			appPropertiesFileContent = appPropertiesFileContent.replaceFirst("terminalConnectionFactory.preferencePath=.*",
					MessageFormat.format("terminalConnectionFactory.preferencePath={0}", projectCreationRequest.getProjectName()));

			// change folder name for user properties files
			boolean contains = appPropertiesFileContent.contains("openLegacyProperties." + USER_PROPERTIES_FOLDER_NAME + "=");
			if (contains) {
				appPropertiesFileContent = appPropertiesFileContent.replaceFirst("openLegacyProperties."
						+ USER_PROPERTIES_FOLDER_NAME + "=.*", MessageFormat.format("openLegacyProperties."
						+ USER_PROPERTIES_FOLDER_NAME + "={0}", projectCreationRequest.getProjectName()));
			} else {
				appPropertiesFileContent = appPropertiesFileContent
						+ MessageFormat.format("\nopenLegacyProperties." + USER_PROPERTIES_FOLDER_NAME + "={0}",
								projectCreationRequest.getProjectName());
			}

			FileUtils.write(appPropertiesFileContent, appPropertiesFile);
		}

		File dbPropertiesFile = new File(targetPath, "src/main/resources/database.properties");
		if (dbPropertiesFile.exists()) {
			String dbPropertiesFileContent = IOUtils.toString(new FileInputStream(dbPropertiesFile));

			dbPropertiesFileContent = dbPropertiesFileContent.replaceFirst("database.username=.*", MessageFormat.format(
					"database.username={0}", projectCreationRequest.getDbUser()));
			dbPropertiesFileContent = dbPropertiesFileContent.replaceFirst("database.password=.*", MessageFormat.format(
					"database.password={0}", projectCreationRequest.getDbPass()));
			dbPropertiesFileContent = dbPropertiesFileContent.replaceFirst("database.driverClassName=.*", MessageFormat.format(
					"database.driverClassName={0}", projectCreationRequest.getDbDriver()));
			dbPropertiesFileContent = dbPropertiesFileContent.replaceFirst("database.url=.*", MessageFormat.format(
					"database.url={0}", projectCreationRequest.getDbUrl()));

			FileUtils.write(dbPropertiesFileContent, dbPropertiesFile);
		}
	}

	private static void renameLaunchers(final String projectName, final File targetPath) throws FileNotFoundException,
			IOException {
		targetPath.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(".launch")) {
					try {
						renameLauncher(projectName, targetPath, pathname.getName());
					} catch (IOException e) {
						throw (new RuntimeException(e));
					}
				}
				return false;
			}
		});
	}

	private static void renameLauncher(String projectName, File targetPath, String fileName) throws FileNotFoundException,
			IOException {
		File launcherFile = new File(targetPath, fileName);

		if (!launcherFile.exists()) {
			logger.warn(MessageFormat.format("Unable to find launcher {0} within {1}", fileName, projectName));
			return;
		}

		String launchFileContent = IOUtils.toString(new FileInputStream(launcherFile));

		launchFileContent = launchFileContent.replaceAll("workspace_loc:.*}", ("workspace_loc:" + projectName + "}"));
		FileUtils.write(launchFileContent, launcherFile);

	}

	private static void updateSpringContextWithDefaultPackage(String defaultPackageName, File targetPath) throws IOException,
			FileNotFoundException {
		updateSpringFile(defaultPackageName, new File(targetPath, DesigntimeConstants.DEFAULT_SPRING_CONTEXT_FILE));
		updateSpringFile(defaultPackageName + ".web", new File(targetPath, DEFAULT_SPRING_WEB_CONTEXT_FILE));
		updateSpringFile(defaultPackageName, new File(targetPath, DEFAULT_SPRING_TEST_CONTEXT_FILE));
	}

	private static void createDefaultPackage(String defaultPackageName, File targetPath) throws IOException,
			FileNotFoundException {
		String packageFolders = defaultPackageName.replace(".", "/");
		File packageDir = new File(targetPath + PACKAGE_DIR);
		if (!packageDir.exists()) {
			packageDir.mkdirs();
		}
		File defaultPackage = new File(packageDir, packageFolders);
		defaultPackage.mkdirs();
	}

	private static void updateSpringFile(String defaultPackageName, File springFile) throws IOException, FileNotFoundException {
		if (!springFile.exists()) {
			return;
		}
		String springFileContent = IOUtils.toString(new FileInputStream(springFile));

		/*
		 * Replace only the 1st component-scan definition, assuming it is the project component-scan, 2nd component-scan points to
		 * openlegacy controllers
		 */
		springFileContent = springFileContent.replaceFirst("<context:component-scan base-package=\".*\"", MessageFormat.format(
				"<context:component-scan base-package=\"{0}\"", defaultPackageName));

		/*
		 * Replace apps.inventory.screens with default package
		 */
		springFileContent = springFileContent.replaceFirst("<value>[a-z_0-9\\.]+</value>", MessageFormat.format(
				"<value>{0}</value>", defaultPackageName));
		FileUtils.write(springFileContent, springFile);
	}

	private static void renameProject(String projectName, File targetPath) throws IOException, FileNotFoundException {
		File projectFile = new File(targetPath, ".project");
		String projectFileContent = IOUtils.toString(new FileInputStream(projectFile));

		// NOTE assuming all project templates starts with "openlegacy-"
		projectFileContent = projectFileContent.replaceAll("<name>openlegacy-.*</name>", MessageFormat.format("<name>{0}</name>",
				projectName));
		FileUtils.write(projectFileContent, projectFile);
	}

	private static void renameProjectProperties(String projectName, File targetPath) throws IOException, FileNotFoundException {
		File pomFile = new File(targetPath, "pom.xml");
		if (!pomFile.exists()) {
			logger.error(MessageFormat.format("Unable to find pom.xml within {0}", projectName));
			return;
		}
		String pomFileContent = IOUtils.toString(new FileInputStream(pomFile));

		pomFileContent = replaceFirstAttribute("artifactId", projectName, pomFileContent);
		pomFileContent = replaceFirstAttribute("groupId", projectName, pomFileContent);
		pomFileContent = replaceFirstAttribute("name", projectName, pomFileContent);
		pomFileContent = replaceFirstAttribute("finalName", projectName, pomFileContent);
		pomFileContent = replaceFirstAttribute("warName", projectName, pomFileContent);
		pomFileContent = replaceFirstAttribute("version", DEFAULT_NEW_PROJECT_VERSION, pomFileContent);
		String openlegacyVersion = DesignTimeExecuterImpl.class.getPackage().getImplementationVersion();
		if (openlegacyVersion != null) {
			pomFileContent = replaceFirstAttribute("openlegacy-version", openlegacyVersion, pomFileContent);
		}

		FileUtils.write(pomFileContent, pomFile);
	}

	private static String replaceFirstAttribute(String attributeName, String attributeValue, String pomFileContent) {
		String stringToReplace = MessageFormat.format("<{0}>.*</{0}>", attributeName);
		return pomFileContent.replaceFirst(stringToReplace, MessageFormat.format("<{0}>{1}</{0}>", attributeName, attributeValue));

	}

	private static void addDbDriverDependency(File targetPath, String mavenDependencyString) throws FileNotFoundException,
			IOException {
		File pomFile = new File(targetPath, "pom.xml");

		if (!pomFile.exists()) {
			logger.error(MessageFormat.format("Unable to find pom.xml within {0}", targetPath));
			return;
		}

		if (StringUtils.isBlank(mavenDependencyString)) {
			return;
		}

		String[] split = mavenDependencyString.split(":");
		if (split.length != 3) {
			logger.error("Maven dependency of DB driver is not specified. Should be in format {groupId}:{artifactId}:{version}");
			return;
		}

		String pomFileContent = IOUtils.toString(new FileInputStream(pomFile));
		pomFileContent = pomFileContent.replaceFirst(
				"</dependencies>",
				MessageFormat.format(
						"\t<dependency>\n\t\t\t<groupId>{0}</groupId>\n\t\t\t<artifactId>{1}</artifactId>\n\t\t\t<version>{2}</version>\n\t\t</dependency>\n\n\t</dependencies>",
						split[0], split[1], split[2]));

		FileUtils.write(pomFileContent, pomFile);
	}

	private static void renameProviderInPOM(String provider, File targetPath) throws FileNotFoundException, IOException {
		File pomFile = new File(targetPath, "pom.xml");

		if (!pomFile.exists()) {
			logger.error(MessageFormat.format("Unable to find pom.xml within {0}", targetPath));
			return;
		}

		String pomFileContent = IOUtils.toString(new FileInputStream(pomFile));

		if (!provider.equals("mock-up") && !provider.equals("openlegacy-impl")) {

			// openlegacy-impl is the default pom setting, since version 2.0
			// leave renaming tn5250j for older version of plugin
			if (provider.startsWith("openlegacy-")) {
				pomFileContent = pomFileContent.replaceFirst(
						"<groupId>org.openlegacy.providers</groupId>\\s+<artifactId>openlegacy-tn5250j</artifactId>",
						MessageFormat.format("<groupId>org.openlegacy.providers</groupId>\n\t\t\t<artifactId>{0}</artifactId>",
								provider));
				pomFileContent = pomFileContent.replaceFirst(
						"<groupId>org.openlegacy</groupId>\\s+<artifactId>openlegacy-impl</artifactId>", MessageFormat.format(
								"<groupId>org.openlegacy.providers</groupId>\n\t\t\t<artifactId>{0}</artifactId>", provider));
			} else {
				pomFileContent = pomFileContent.replaceFirst(
						"<groupId>org.openlegacy.providers</groupId>\\s+<artifactId>openlegacy-tn5250j</artifactId>",
						MessageFormat.format(
								"<groupId>org.openlegacy.providers</groupId>\n\t\t\t<artifactId>openlegacy-{0}</artifactId>",
								provider));
				pomFileContent = pomFileContent.replaceFirst(
						"<groupId>org.openlegacy</groupId>\\s+<artifactId>openlegacy-impl</artifactId>", MessageFormat.format(
								"<groupId>org.openlegacy.providers</groupId>\n\t\t\t<artifactId>openlegacy-{0}</artifactId>",
								provider));
			}
		}

		FileUtils.write(pomFileContent, pomFile);
	}

	private static void renameThemeInPOM(ProjectTheme projectTheme, File targetPath) throws FileNotFoundException, IOException {
		File pomFile = new File(targetPath, "pom.xml");

		if (!pomFile.exists()) {
			logger.error(MessageFormat.format("Unable to find pom.xml within {0}", targetPath));
			return;
		}

		String pomFileContent = IOUtils.toString(new FileInputStream(pomFile));

		if (projectTheme != null) {
			// rename theme for web
			pomFileContent = pomFileContent.replaceFirst(
					"<groupId>org.openlegacy.web</groupId>\\s+<artifactId>openlegacy-themes-\\w+(.*?)(?<!m)</artifactId>",
					MessageFormat.format("<groupId>org.openlegacy.web</groupId>\n\t\t\t<artifactId>{0}</artifactId>",
							projectTheme.getName()));
			FileUtils.write(pomFileContent, pomFile);
		}
	}

	private static void renameThemeInAppProperties(ProjectTheme projectTheme, File targetPath) throws FileNotFoundException,
			IOException {
		File appPropertiesFile = new File(targetPath, APPLICATION_PROPERTIES);

		if (!appPropertiesFile.exists()) {
			logger.error(MessageFormat.format("Unable to find application.properties within {0}", targetPath));
			return;
		}

		String appPropertiesFileContent = IOUtils.toString(new FileInputStream(appPropertiesFile));

		if (projectTheme != null) {
			appPropertiesFileContent = appPropertiesFileContent.replaceFirst("themeUtil.defaultTheme=.*", MessageFormat.format(
					"themeUtil.defaultTheme={0}", projectTheme.getDisplayName().toLowerCase()));
			FileUtils.write(appPropertiesFileContent, appPropertiesFile);
		}
	}

	private static void renameThemeInIndexJSP(ProjectTheme projectTheme, boolean rightToLeft, File targetPath)
			throws FileNotFoundException, IOException {
		File indexFile = new File(targetPath, INDEX_JSP_PATH);

		if (!indexFile.exists()) {
			logger.error(MessageFormat.format("Unable to find src/main/webapp/app/index.jsp within {0}", targetPath));
			return;
		}

		String indexFileContent = IOUtils.toString(new FileInputStream(indexFile));

		String bootstrapRtlSuffix = "";
		if (rightToLeft) {
			bootstrapRtlSuffix = "-rtl";
		}
		indexFileContent = indexFileContent.replaceAll("#rtlSuffix#", bootstrapRtlSuffix);

		if (projectTheme != null) {
			String themeName = projectTheme.getDisplayName().toLowerCase();
			String themeFolderName = themeName;
			if (rightToLeft) {
				themeName = themeName + "_rtl";
			}
			indexFileContent = indexFileContent.replaceFirst("<link ng-href=\"themes/.+/.+\\.css\".+>", MessageFormat.format(
					"<link ng-href=\"themes/{0}/{1}.css\" rel=\"stylesheet\" >", themeFolderName, themeName));
			FileUtils.write(indexFileContent, indexFile);
		}

	}

	//	private static void changeOrAddContextParamInWebXml(String paramName, String paramValue, File targetPath)
	//			throws FileNotFoundException, IOException {
	//		File webXmlFile = new File(targetPath, "src/main/webapp/WEB-INF/web.xml");
	//		if (!webXmlFile.exists()) {
	//			logger.error(MessageFormat.format("Unable to find web.xml within {0}", targetPath));
	//			return;
	//		}
	//
	//		String fileContent = IOUtils.toString(new FileInputStream(webXmlFile));
	//
	//		Pattern pattern = Pattern.compile(".*<context-param>\\s+<param-name>" + paramName + "</param-name>.*");
	//		Matcher matcher = pattern.matcher(fileContent);
	//		if (matcher.find()) {
	//			fileContent = fileContent.replaceFirst("<context-param>\\s+<param-name>" + paramName
	//					+ "</param-name>\\s+<param-value>.*</param-value>", MessageFormat.format(
	//					"<context-param>\n\t\t<param-name>{0}</param-name>\n\t\t<param-value>{1}</param-value>", paramName,
	//					paramValue));
	//		} else {
	//			// add new <context-param> into the end of file
	//			int indexOf = fileContent.indexOf("</web-app>");
	//			StringBuilder sb = new StringBuilder(fileContent);
	//			fileContent = sb.insert(
	//					indexOf,
	//					MessageFormat.format(
	//							"\t<context-param>\n\t\t<param-name>{0}</param-name>\n\t\t<param-value>{1}</param-value>\n\t</context-param>\n",
	//							paramName, paramValue)).toString();
	//		}
	//		FileUtils.write(fileContent, webXmlFile);
	//	}

	@Override
	public void generateScreenModel(GenerateScreenModelRequest generateModelRequest) throws GenerationException {
		// initialize application context

		ApplicationContext projectApplicationContext = getOrCreateApplicationContext(generateModelRequest.getProjectPath());

		getGenerateUtil().setTemplateDirectory(generateModelRequest.getTemplatesDirectory());

		TerminalSnapshotsAnalyzer snapshotsAnalyzer = projectApplicationContext.getBean(TerminalSnapshotsAnalyzer.class);

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = null;
		if (generateModelRequest.getTerminalSnapshots() == null || generateModelRequest.getTerminalSnapshots().length == 0) {
			FileInputStream trailInputStream;
			try {
				trailInputStream = new FileInputStream(generateModelRequest.getTrailFile().getAbsolutePath());
			} catch (FileNotFoundException e1) {
				throw (new GenerationException(e1));
			}
			screenEntitiesDefinitions = snapshotsAnalyzer.analyzeTrail(trailInputStream);
		} else {
			Assert.notNull(generateModelRequest.getTerminalSnapshots(),
					"Must set either trail file or terminal snapshots in generate API request");
			screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(Arrays.asList(generateModelRequest.getTerminalSnapshots()));
		}

		List<ScreenEntityDefinition> screenDefinitions = getSortedSnapshots(screenEntitiesDefinitions);

		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition) screenEntityDefinition).setGenerateAspect(generateModelRequest.isGenerateAspectJ());

			generateScreenEntityDefinition(generateModelRequest, screenEntityDefinition);
		}

		if (generateModelRequest.isGenerateTest()) {
			generateTest(generateModelRequest.getTrailFile(), screenDefinitions, generateModelRequest.getProjectPath());
		}

	}

	@Override
	public boolean generateScreenEntityDefinition(GenerateScreenModelRequest generateScreenModelRequest,
			ScreenEntityDefinition entityDefinition) {

		EntityUserInteraction<EntityDefinition<?>> entityUserInteraction = generateScreenModelRequest.getEntityUserInteraction();

		if (entityUserInteraction != null) {
			boolean generate = entityUserInteraction.customizeEntity(entityDefinition, generateScreenModelRequest);
			if (!generate) {
				return false;
			}
		}

		((ScreenEntityDesigntimeDefinition) entityDefinition).setPackageName(generateScreenModelRequest.getPackageDirectory().replaceAll(
				"/", "."));

		ApplicationContext projectApplicationContext = getOrCreateApplicationContext(generateScreenModelRequest.getProjectPath());

		try {
			File packageDir = new File(generateScreenModelRequest.getSourceDirectory(),
					generateScreenModelRequest.getPackageDirectory());
			String entityName = entityDefinition.getEntityName();
			File targetJavaFile = new File(packageDir, MessageFormat.format("{0}.java", entityName));

			boolean generate = true;
			if (targetJavaFile.exists()) {
				boolean override = entityUserInteraction != null && entityUserInteraction.isOverride(targetJavaFile);
				if (!override) {
					generate = false;
				}
			}
			if (generate) {
				generateJava(entityDefinition, targetJavaFile, "ScreenEntity.java.template");
				generateAspect(targetJavaFile);
			}

			File screenResourcesDir = new File(packageDir, entityName + "-resources");
			screenResourcesDir.mkdir();
			TerminalSnapshot snapshot = entityDefinition.getOriginalSnapshot();

			TerminalSnapshotImageRenderer imageRenderer = projectApplicationContext.getBean(TerminalSnapshotImageRenderer.class);
			TerminalSnapshotTextRenderer textRenderer = projectApplicationContext.getBean(TerminalSnapshotTextRenderer.class);
			DefaultTerminalSnapshotXmlRenderer xmlRenderer = projectApplicationContext.getBean(DefaultTerminalSnapshotXmlRenderer.class);

			if (generateScreenModelRequest.isGenerateSnapshotText()) {
				// generate txt file with screen content
				generateResource(snapshot, entityName, screenResourcesDir, textRenderer);
			}
			if (generateScreenModelRequest.isGenerateSnapshotImage()) {
				// generate jpg file with screen image
				generateResource(snapshot, entityName, screenResourcesDir, imageRenderer);
			}

			if (generateScreenModelRequest.isGenerateSnapshotXml()) {
				// generate xml file with screen XML for testing purposes
				generateResource(snapshot, entityName, screenResourcesDir, xmlRenderer);
			}

			// open in editor
			entityUserInteraction.open(targetJavaFile, entityDefinition);

			return true;

		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}

	}

	@Override
	public boolean generateRpcEntityDefinition(GenerateRpcModelRequest generateRpcModelRequest,
			RpcEntityDefinition entityDefinition) {

		EntityUserInteraction<EntityDefinition<?>> entityUserInteraction = generateRpcModelRequest.getEntityUserInteraction();

		if (entityUserInteraction != null) {
			boolean generate = entityUserInteraction.customizeEntity(entityDefinition, generateRpcModelRequest);
			if (!generate) {
				return false;
			}
		}

		((SimpleRpcEntityDesigntimeDefinition) entityDefinition).setPackageName(generateRpcModelRequest.getPackageDirectory().replaceAll(
				"/", "."));

		((SimpleRpcEntityDesigntimeDefinition) entityDefinition).setNavigation(generateRpcModelRequest.getNavigation());

		SimpleRpcActionDefinition actionDefinition = new SimpleRpcActionDefinition(RpcActions.READ(), "Read");

		actionDefinition.setProgramPath(generateRpcModelRequest.getReadAction());
		entityDefinition.getActions().add(actionDefinition);

		try {
			File packageDir = new File(generateRpcModelRequest.getSourceDirectory(),
					generateRpcModelRequest.getPackageDirectory());
			String entityName = entityDefinition.getEntityName();
			File targetJavaFile = new File(packageDir, MessageFormat.format("{0}.java", entityName));

			boolean generate = true;
			if (targetJavaFile.exists()) {
				boolean override = entityUserInteraction != null && entityUserInteraction.isOverride(targetJavaFile);
				if (!override) {
					generate = false;
				}
			}
			if (generate) {
				generateJava(entityDefinition, targetJavaFile, "RpcEntity.java.template");
				generateAspect(targetJavaFile);
			}

			File screenResourcesDir = new File(packageDir, entityName + "-resources");
			screenResourcesDir.mkdir();

			if (generateRpcModelRequest.isGenerateSource()) {
				// generate src file with source content
				FileInputStream fis = new FileInputStream(generateRpcModelRequest.getSourceFile());
				IOUtils.copy(fis, new FileOutputStream(new File(screenResourcesDir, entityDefinition.getEntityName() + ".src")));
			}

			return true;

		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}

	}

	private static List<ScreenEntityDefinition> getSortedSnapshots(Map<String, ScreenEntityDefinition> screenEntitiesDefinitions) {
		List<ScreenEntityDefinition> screenDefinitions = new ArrayList<ScreenEntityDefinition>();
		screenDefinitions.addAll(screenEntitiesDefinitions.values());
		Collections.sort(screenDefinitions, new Comparator<ScreenEntityDefinition>() {

			@Override
			public int compare(ScreenEntityDefinition o1, ScreenEntityDefinition o2) {
				if (o2.getSnapshot().getSequence() == null) {
					return -1;
				}
				if (o1.getSnapshot().getSequence() == null) {
					return 1;
				}
				return o1.getSnapshot().getSequence() - o2.getSnapshot().getSequence();
			}
		});
		return screenDefinitions;
	}

	private GenerateUtil getGenerateUtil() {
		return defaultDesigntimeApplicationContext.getBean(GenerateUtil.class);
	}

	private SourceFetcher getImportUtil() {
		return defaultDesigntimeApplicationContext.getBean(SourceFetcher.class);
	}

	private CobolNameRecognizer getCobolNameRecognizer() {
		return defaultDesigntimeApplicationContext.getBean(CobolNameRecognizer.class);

	}

	private CopyBookFetcher getCopyBookFetcher() {
		return defaultDesigntimeApplicationContext.getBean(CopyBookFetcher.class);

	}

	private CobolLocalPartNamesFethcher getCobolLocalPartNamesFethcher() {
		return defaultDesigntimeApplicationContext.getBean(CobolLocalPartNamesFethcher.class);
	}

	private void generateTest(File trailFile, Collection<ScreenEntityDefinition> screenDefinitions, File projectPath) {
		ApplicationContext projectApplicationContext = getOrCreateApplicationContext(projectPath);

		TrailJunitGenerator generator = projectApplicationContext.getBean(TrailJunitGenerator.class);
		File testSourceDirectory = new File(projectPath, TEST_SOURCE_DIR);
		File testsDirectory = new File(testSourceDirectory, "tests");
		File junitFile = null;
		try {
			testsDirectory.mkdirs();
			String fileWithoutAnyExtension = FileUtils.fileWithoutAnyExtension(trailFile.getName());
			String testName = StringUtils.capitalize(StringUtil.toClassName(fileWithoutAnyExtension) + "Test");
			junitFile = new File(testsDirectory, testName + ".java");
			FileOutputStream fos = new FileOutputStream(junitFile);
			generator.generate(screenDefinitions, testName, fos);
			fos.close();
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		} finally {
			FileUtils.deleteEmptyFile(junitFile);

		}

	}

	private void generateJava(EntityDefinition<?> screenEntityDefinition, File file, String templateName)
			throws FileNotFoundException, TemplateException, IOException {

		FileOutputStream fos = null;
		try {
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);

			String typeName = screenEntityDefinition.getTypeName();
			getGenerateUtil().generate(screenEntityDefinition, fos, templateName, typeName);

		} finally {
			IOUtils.closeQuietly(fos);
			FileUtils.deleteEmptyFile(file);
		}
	}

	private static void generateResource(TerminalSnapshot terminalSnapshot, String entityName, File screenResourcesDir,
			TerminalSnapshotRenderer renderer) {
		generateResource(terminalSnapshot, entityName, screenResourcesDir, renderer, false);
	}

	private static void generateResource(TerminalSnapshot terminalSnapshot, String entityName, File screenResourcesDir,
			TerminalSnapshotRenderer renderer, boolean overrideResource) {
		FileOutputStream fos = null;
		File renderedScreenResourceFile = null;
		try {
			renderedScreenResourceFile = new File(screenResourcesDir, MessageFormat.format("{0}.{1}", entityName,
					renderer.getFileFormat()));
			if (!renderedScreenResourceFile.exists() || overrideResource) {
				fos = new FileOutputStream(renderedScreenResourceFile);
				renderer.render(terminalSnapshot, fos);
			}
		} catch (FileNotFoundException e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
			FileUtils.deleteEmptyFile(renderedScreenResourceFile);
		}
	}

	/**
	 * Get or create a project Spring application context If the project has it's own designtime context (done using
	 * copyDesigntimeContext) then use it, otherwise use default according to preferences (default/rtl for now)
	 * 
	 * @param projectPath
	 *            The project file system path
	 * @return
	 */
	private synchronized ApplicationContext getOrCreateApplicationContext(File projectPath) {

		if (defaultDesigntimeApplicationContext == null) {
			defaultDesigntimeApplicationContext = new ClassPathXmlApplicationContext("/openlegacy-default-designtime-context.xml");
		}

		// no project path specified - return default context
		if (projectPath == null) {
			return defaultDesigntimeApplicationContext;
		}

		ApplicationContext projectApplicationContext = projectsDesigntimeAplicationContexts.get(projectPath.getAbsolutePath());

		if (projectApplicationContext == null) {
			File designtimeContextFile = new File(projectPath, DesignTimeExecuter.CUSTOM_DESIGNTIME_CONTEXT_RELATIVE_PATH);
			if (designtimeContextFile.exists()) {
				// in windows add / to the file path (http://www.ehour.nl/forum/viewtopic.php?t=1113)
				// #453 - in linux need throws an error when having only single /
				String prefix = OsUtils.isWindows() && designtimeContextFile.getAbsolutePath().startsWith("/") ? "" : "/";
				projectApplicationContext = new FileSystemXmlApplicationContext(prefix + designtimeContextFile.getAbsolutePath());
			} else {
				String embeddedDesigntimeContextFile = getEmbeddedDesigntimeContextFile(projectPath);
				String designtimeContextType = getPreferences(projectPath).get(PreferencesConstants.DESIGNTIME_CONTEXT);
				// don't re-initialize the default context on project level if exists on root level
				// (defaultDesigntimeApplicationContext)
				if (designtimeContextType == null || designtimeContextType.equals(DEFAULT_CONTEXT)) {
					projectApplicationContext = defaultDesigntimeApplicationContext;
				} else {
					projectApplicationContext = new ClassPathXmlApplicationContext(embeddedDesigntimeContextFile);
				}
			}
			projectsDesigntimeAplicationContexts.put(projectPath.getAbsolutePath(), projectApplicationContext);
		}
		return projectApplicationContext;

	}

	/*
	 * assume Maven project structure. All files are either in src, test or target folder
	 */
	public File getProjectPath(File someProjectFile) {
		while (!someProjectFile.getName().equals("src") && !someProjectFile.getName().equals("test")
				&& !someProjectFile.getName().equals("target")) {
			someProjectFile = someProjectFile.getParentFile();
		}

		return someProjectFile.getParentFile();

	}

	@Override
	public boolean generateAspect(File javaFile) {

		File projectPath = getProjectPath(javaFile);
		ApplicationContext applicationContext = getOrCreateApplicationContext(projectPath);
		GenerateUtil generateUtil = applicationContext.getBean(GenerateUtil.class);
		generateUtil.setTemplateDirectory(new File(projectPath, TEMPLATES_DIR));
		try {
			FileInputStream input = new FileInputStream(javaFile);
			CompilationUnit compilationUnit = JavaParser.parse(input, CharEncoding.UTF_8);

			if (JavaParserUtil.hasAnnotation(compilationUnit, ScreenAnnotationConstants.SCREEN_ENTITY_ANNOTATION,
					ScreenAnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)) {
				ScreenPojosAjGenerator generator = applicationContext.getBean(ScreenPojosAjGenerator.class);
				return generator.generate(javaFile, compilationUnit);
			} else if (JavaParserUtil.hasAnnotation(compilationUnit, RpcAnnotationConstants.RPC_ENTITY_ANNOTATION,
					RpcAnnotationConstants.RPC_ENTITY_SUPER_CLASS_ANNOTATION)
					|| JavaParserUtil.hasAnnotation(compilationUnit, RpcAnnotationConstants.RPC_ENTITY_ANNOTATION,
							RpcAnnotationConstants.RPC_PART_ANNOTATION)) {
				RpcPojosAjGenerator generator = applicationContext.getBean(RpcPojosAjGenerator.class);
				return generator.generate(javaFile, compilationUnit);
			} else if (JavaParserUtil.hasAnnotation(compilationUnit, DbAnnotationConstants.DB_JPA_ENTITY_ANNOTATION,
					DbAnnotationConstants.DB_ENTITY_ANNOTATION, DbAnnotationConstants.DB_ENTITY_SUPER_CLASS_ANNOTATION)
					|| JavaParserUtil.hasAnnotation(compilationUnit, RpcAnnotationConstants.RPC_ENTITY_ANNOTATION,
							RpcAnnotationConstants.RPC_PART_ANNOTATION)) {
				DbPojosAjGenerator generator = applicationContext.getBean(DbPojosAjGenerator.class);
				return generator.generate(javaFile, compilationUnit);
			}
		} catch (IOException e) {
			throw (new GenerationException(e));
		} catch (ParseException e) {
			logger.warn("Failed parsing java file:" + e.getMessage());
			// non compiled java class. Ignore it
		}
		return false;

	}

	@Override
	public void initialize(File projectPath) {
		// initialize application context & analyzer
		if (projectPath != null) {
			projectsDesigntimeAplicationContexts.remove(projectPath.getAbsolutePath());
		}
		// init the analyzer rules
		getOrCreateApplicationContext(projectPath).getBean(SnapshotsAnalyzer.class);
	}

	/**
	 * Generates all required view files for a Spring MVC framework
	 */
	@Override
	public void generateView(GenerateViewRequest generateViewRequest) throws GenerationException {

		EntityDefinition<?> entityDefinition = initEntityDefinition(generateViewRequest.getEntitySourceFile());

		File projectPath = getProjectPath(generateViewRequest.getEntitySourceFile());
		EntityPageGenerator entityWebGenerator = null;
		if (entityDefinition instanceof ScreenEntityDefinition) {
			entityWebGenerator = getOrCreateApplicationContext(projectPath).getBean(ScreenEntityPageGenerator.class);
			entityWebGenerator.generateView(generateViewRequest, entityDefinition);
		} else if (entityDefinition instanceof RpcEntityDefinition) {
			entityWebGenerator = getOrCreateApplicationContext(projectPath).getBean(RpcEntityPageGenerator.class);
			entityWebGenerator.generateView(generateViewRequest, entityDefinition);
		} else {
			DbEntityPageGenerator dbEntityWebGenerator = getOrCreateApplicationContext(projectPath).getBean(
					DbEntityPageGenerator.class);
			dbEntityWebGenerator.generateView(generateViewRequest, entityDefinition, entityDefinition.getEntityName() + ".list",
					DbEntityPageGenerator.LIST_MODE);
			dbEntityWebGenerator.generateView(generateViewRequest, entityDefinition, entityDefinition.getEntityName() + ".edit",
					DbEntityPageGenerator.EDIT_MODE);
		}

	}

	/**
	 * Generates all required view files for a Spring MVC framework
	 */
	@Override
	public void generateController(GenerateControllerRequest generateControllerRequest) throws GenerationException {

		EntityDefinition<?> entityDefinition = initEntityDefinition(generateControllerRequest.getEntitySourceFile());

		File projectPath = getProjectPath(generateControllerRequest.getSourceDirectory());
		EntityPageGenerator entityPageGenerator = null;
		if (entityDefinition instanceof ScreenEntityDefinition) {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(ScreenEntityPageGenerator.class);
		} else if (entityDefinition instanceof RpcEntityDefinition) {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(RpcEntityPageGenerator.class);
		} else {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(DbEntityPageGenerator.class);
		}

		if (entityPageGenerator.isSupportControllerGeneration()) {
			entityPageGenerator.generateController(generateControllerRequest, entityDefinition);
		} else {
			logger.warn(MessageFormat.format("{0} doesnt support controller generation", entityPageGenerator.getClass()));
		}
	}

	@Override
	public boolean isSupportControllerGeneration(File entityFile) {
		return getOrCreateApplicationContext(getProjectPath(entityFile)).getBean(ScreenEntityPageGenerator.class).isSupportControllerGeneration();
	}

	@Override
	public boolean isSupportServiceGeneration(File projectPath, Boolean supportRestFulService) {
		return getOrCreateApplicationContext(projectPath).getBean(ScreenEntityServiceGenerator.class).isSupportServiceGeneration(
				projectPath, supportRestFulService);
	}

	@Override
	public void generateService(GenerateServiceRequest generateServiceRequest, Boolean supportRestFulService) {
		File projectPath = generateServiceRequest.getProjectPath();
		EntityServiceGenerator entityServiceGenerator = null;
		if (generateServiceRequest.getServiceType() == BackendSolution.SCREEN) {
			entityServiceGenerator = getOrCreateApplicationContext(projectPath).getBean(ScreenEntityServiceGenerator.class);
		} else if (generateServiceRequest.getServiceType() == BackendSolution.RPC) {
			entityServiceGenerator = getOrCreateApplicationContext(projectPath).getBean(RpcEntityServiceGenerator.class);
		} else if (generateServiceRequest.getServiceType() == BackendSolution.JDBC) {
			entityServiceGenerator = getOrCreateApplicationContext(projectPath).getBean(DbEntityServiceGenerator.class);
		}

		if (entityServiceGenerator.isSupportServiceGeneration(generateServiceRequest.getProjectPath(), supportRestFulService)) {
			entityServiceGenerator.generateService(generateServiceRequest, supportRestFulService);
		} else {
			logger.warn(MessageFormat.format("{0} doesnt support controller generation", entityServiceGenerator.getClass()));
		}

	}

	@Override
	public EntityDefinition<?> initEntityDefinition(File sourceFile) {
		EntityDefinition<?> entityDefinition = null;
		try {
			FileInputStream fis = new FileInputStream(sourceFile);
			String fileContent = IOUtils.toString(fis);
			CompilationUnit compilationUnit = JavaParser.parse(sourceFile, CharEncoding.UTF_8);
			File packageDir = new File(sourceFile.getParent());
			if (fileContent.contains(ScreenEntity.class.getSimpleName())) {
				entityDefinition = ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir);
			} else if (fileContent.contains(RpcEntity.class.getSimpleName())) {
				entityDefinition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir);
			} else if (fileContent.contains(DbEntity.class.getSimpleName()) || fileContent.contains(Entity.class.getSimpleName())) {
				entityDefinition = DbCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir);
			}
		} catch (Exception e) {
			throw (new GenerationException(e));
		}
		if (entityDefinition == null) {
			throw (new GenerationException(MessageFormat.format("{0} is not a screen entity", sourceFile.getName())));
		}
		return entityDefinition;
	}

	@Override
	public void copyCodeGenerationTemplates(File projectPath) {
		File templatesDir = new File(projectPath, TEMPLATES_DIR);
		templatesDir.mkdirs();
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		Resource[] defaultTemplates;
		OutputStream fos = null;
		try {
			defaultTemplates = pathResolver.getResources(DEFAULT_TEMPLATES_PATTERN);
			for (Resource resource : defaultTemplates) {
				String uri = resource.getURI().toString();
				String fileRelativePath = uri.substring(uri.indexOf("!") + 2);
				File targetFile = new File(templatesDir, fileRelativePath);
				targetFile.getParentFile().mkdirs();
				fos = new FileOutputStream(targetFile);
				IOUtils.copy(resource.getInputStream(), fos);
				IOUtils.closeQuietly(fos);
			}
		} catch (IOException e) {
			throw (new GenerationException("Error creating custom templates", e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	@Override
	public String getPreferences(File projectPath, String key) {
		ProjectPreferences perferences = getPreferences(projectPath);
		return perferences.get(key);
	}

	@Override
	public void savePreference(File projectPath, String key, String value) {
		ProjectPreferences perferences = getPreferences(projectPath);
		perferences.put(key, value);

	}

	private ProjectPreferences getPreferences(File projectPath) {
		ProjectPreferences preferences = projectsPreferences.get(projectPath);
		if (preferences != null) {
			return preferences;
		}

		File prefFile = new File(projectPath, PREFERENCES_FILE);

		preferences = new SimpleProjectPreferences(prefFile);
		projectsPreferences.put(projectPath, preferences);

		return preferences;
	}

	@Override
	public void reloadPreferences(File projectPath) {
		projectsPreferences.remove(projectPath);
		projectsDesigntimeAplicationContexts.remove(projectPath.getAbsolutePath());
		getOrCreateApplicationContext(projectPath);
	}

	@Override
	public void copyDesigntimeContext(File projectPath) {
		File customDesigntimeFile = new File(projectPath, DesignTimeExecuter.CUSTOM_DESIGNTIME_CONTEXT_RELATIVE_PATH);
		customDesigntimeFile.getParentFile().mkdirs();

		FileOutputStream fos = null;
		try {
			String embeddedDesigntimeContext = getEmbeddedDesigntimeContextFile(projectPath);
			InputStream defaultDesigntimeStream = getClass().getResourceAsStream(embeddedDesigntimeContext);
			fos = new FileOutputStream(customDesigntimeFile);
			IOUtils.copy(defaultDesigntimeStream, fos);
		} catch (IOException e) {
			throw (new GenerationException("Error creating custom designtime context file", e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	/*
	 * The embedded design-time context file to user by preferences. Currently default/rtl
	 */
	private String getEmbeddedDesigntimeContextFile(File projectPath) {
		String designtimeContextType = getPreferences(projectPath).get(PreferencesConstants.DESIGNTIME_CONTEXT);
		designtimeContextType = designtimeContextType != null ? designtimeContextType : "default";
		return MessageFormat.format("/openlegacy-{0}-designtime-context.xml", designtimeContextType);
	}

	public List<File> generateRpcModelParts(GenerateRpcModelRequest generateRpcModelRequest, CodeParser codeParser)
			throws FileNotFoundException {
		ParseResults parseResults;
		List<File> javaFiles = new ArrayList<File>();
		CopyBookFetcher copyBookFetcher = getCopyBookFetcher();
		Map<String, InputStream> copybookStreams = copyBookFetcher.getCopyBooks(generateRpcModelRequest.getSourceFile());
		File packageDir = new File(generateRpcModelRequest.getSourceDirectory(), generateRpcModelRequest.getPackageDirectory());
		String packegeName = generateRpcModelRequest.getPackageDirectory().replaceAll("/", ".");
		for (String copyBookName : copybookStreams.keySet()) {
			String baseName = FileUtils.fileWithoutAnyExtension(copyBookName);
			File targetPartJavaFile = new File(packageDir, MessageFormat.format("{0}.java", StringUtil.toClassName(baseName)));
			javaFiles.add(targetPartJavaFile);
			if (targetPartJavaFile.exists()) {
				continue;
			}
			InputStream inputStream = copybookStreams.get(copyBookName);
			try {
				StringWriter writer = new StringWriter();

				IOUtils.copy(inputStream, writer);

				String fileContent = writer.toString();
				parseResults = codeParser.parse(fileContent, copyBookName);
				RpcEntityDefinition rpcEntityDefinition = parseResults.getEntityDefinition();
				SimpleRpcEntityDesigntimeDefinition devEntity = (SimpleRpcEntityDesigntimeDefinition) rpcEntityDefinition;
				devEntity.setEntityName(baseName);
				devEntity.setPackageName(packegeName);
				devEntity.setOnlyPart(true);
				generateJava(rpcEntityDefinition, targetPartJavaFile, "RpcEntity.java.template");
				generateAspect(targetPartJavaFile);
			} catch (IOException e) {
				throw (new OpenLegacyRuntimeException(e));
			} catch (TemplateException e) {
				throw (new OpenLegacyRuntimeException(e));
			}
		}
		return javaFiles;
	}

	@Override
	public void generateRpcModel(GenerateRpcModelRequest generateRpcModelRequest) {
		ApplicationContext projectApplicationContext = getOrCreateApplicationContext(generateRpcModelRequest.getProjectPath());

		getGenerateUtil().setTemplateDirectory(generateRpcModelRequest.getTemplatesDirectory());

		EntityUserInteraction<EntityDefinition<?>> entityUserInteraction = generateRpcModelRequest.getEntityUserInteraction();

		String fileExtension = FileUtils.fileExtension(generateRpcModelRequest.getSourceFile().getName());

		CodeParser codeParser = projectApplicationContext.getBean(CodeParserFactory.class).getParser(fileExtension.substring(1));

		if (codeParser == null) {
			throw (new OpenLegacyParseException("No matching parser found for extension " + fileExtension));
		}
		String fileContent;
		RpcEntityDefinition rpcEntityDefinition = null;
		List<File> javaFiles = new ArrayList<File>();

		try {
			File sourceFile = generateRpcModelRequest.getSourceFile();

			ParseResults parseResults;
			CopyBookFetcher copyBookFetcher = getCopyBookFetcher();

			Map<String, InputStream> copybookStreams = copyBookFetcher.getCopyBooks(generateRpcModelRequest.getSourceFile());
			fileContent = IOUtils.toString(new FileInputStream(sourceFile));
			if (copybookStreams.isEmpty()) {
				parseResults = codeParser.parse(fileContent, sourceFile.getName());
			} else {
				javaFiles = generateRpcModelParts(generateRpcModelRequest, codeParser);
				parseResults = codeParser.parse(fileContent, copybookStreams);
			}
			rpcEntityDefinition = parseResults.getEntityDefinition();
			if (!copybookStreams.isEmpty()) {
				CobolLocalPartNamesFethcher cobolLocalPartNamesFethcher = getCobolLocalPartNamesFethcher();
				List<String> externalParts = new ArrayList<String>();
				for (String externalPart : copybookStreams.keySet()) {
					externalParts.add(FileUtils.fileWithoutAnyExtension(externalPart));
				}
				Map<String, String> localToExternal = cobolLocalPartNamesFethcher.get(fileContent, externalParts);
				((SimpleRpcEntityDesigntimeDefinition) rpcEntityDefinition).convertToExternal(localToExternal);
			}

			CobolNameRecognizer cobolNameRecognizer = getCobolNameRecognizer();
			((SimpleRpcEntityDesigntimeDefinition) rpcEntityDefinition).setEntityName(cobolNameRecognizer.getEntityName(
					fileContent, sourceFile.getName()));
		} catch (IOException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
		((SimpleRpcEntityDesigntimeDefinition) rpcEntityDefinition).setGenerateAspect(generateRpcModelRequest.isGenerateAspectJ());

		boolean generated = generateRpcEntityDefinition(generateRpcModelRequest, rpcEntityDefinition);

		if (generated) {
			File packageDir = new File(generateRpcModelRequest.getSourceDirectory(),
					generateRpcModelRequest.getPackageDirectory());
			String entityName = rpcEntityDefinition.getEntityName();
			File targetJavaFile = new File(packageDir, MessageFormat.format("{0}.java", entityName));
			entityUserInteraction.open(targetJavaFile, rpcEntityDefinition);
			for (File javaFile : javaFiles) {
				entityUserInteraction.open(javaFile, rpcEntityDefinition);
			}

		}
	}

	@Override
	public void importSourceFile(ImportSourceRequest importSourceRequest) throws OpenLegacyException {

		byte[] source = getImportUtil().fetch(importSourceRequest.getHost(), importSourceRequest.getUser(),
				importSourceRequest.getPwd(), importSourceRequest.getLegacyFile());

		boolean getFile = true;

		UserInteraction userInteraction = importSourceRequest.getUserInteraction();
		try {

			File dir = new File(importSourceRequest.getWorkingDirPath());
			dir.mkdirs();
			importSourceRequest.setNewFileName(getImportUtil().convertExtension(importSourceRequest.getLegacyFile()));
			String fileName = importSourceRequest.getWorkingDirPath() + importSourceRequest.getNewFileName();
			File file = new File(fileName);
			if (file.exists()) {
				boolean override = userInteraction.isOverride(file);
				if (!override) {
					getFile = false;
				}
			}
			if (getFile) {
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(source);
				IOUtils.closeQuietly(fos);

			}
		} catch (IOException e) {
			throw new OpenLegacyException("Fail to write file", e);
		}

	}

	@Override
	public void addServiceOutputAnnotation(File javaEntityFile) {
		String fileContent;
		try {
			fileContent = org.apache.commons.io.FileUtils.readFileToString(javaEntityFile);
			fileContent = fileContent.replace("@ScreenEntity",
					"import org.openlegacy.annotations.ServiceOutput;\n\n@ServiceOutput\n@ScreenEntity");
			fileContent = fileContent.replace("@RpcEntity",
					"import org.openlegacy.annotations.ServiceOutput;\n\n@ServiceOutput\n@RpcEntity");
			FileUtils.write(fileContent, javaEntityFile);
		} catch (IOException e) {
			throw (new RuntimeException(e));
		}

	}

	@Override
	public void generateScreenEntityResources(String entityName, GenerateScreenModelRequest generateScreenModelRequest) {

		File packageDir = new File(generateScreenModelRequest.getSourceDirectory(),
				generateScreenModelRequest.getPackageDirectory());
		File screenResourcesDir = new File(packageDir, entityName + "-resources");
		if (!screenResourcesDir.exists()) {
			screenResourcesDir.mkdir();
		}

		ApplicationContext projectApplicationContext = getOrCreateApplicationContext(generateScreenModelRequest.getProjectPath());

		TerminalSnapshotImageRenderer imageRenderer = projectApplicationContext.getBean(TerminalSnapshotImageRenderer.class);
		TerminalSnapshotTextRenderer textRenderer = projectApplicationContext.getBean(TerminalSnapshotTextRenderer.class);
		DefaultTerminalSnapshotXmlRenderer xmlRenderer = projectApplicationContext.getBean(DefaultTerminalSnapshotXmlRenderer.class);

		TerminalSnapshot snapshot = (TerminalSnapshot) SerializationUtils.clone(generateScreenModelRequest.getTerminalSnapshots()[0]);

		if (generateScreenModelRequest.isGenerateSnapshotText()) {
			// generate txt file with screen content
			generateResource(snapshot, entityName, screenResourcesDir, textRenderer, true);
		}
		if (generateScreenModelRequest.isGenerateSnapshotImage()) {
			// generate jpg file with screen image
			generateResource(snapshot, entityName, screenResourcesDir, imageRenderer, true);
		}

		if (generateScreenModelRequest.isGenerateSnapshotXml()) {
			// generate xml file with screen XML for testing purposes
			generateResource(snapshot, entityName, screenResourcesDir, xmlRenderer, true);
		}
	}

	@Override
	public void renameViews(String fileNoExtension, String newName, File javaFile, String fileExtension) {
		File projectPath = getProjectPath(javaFile);
		File renamedJavaFile = new File(javaFile.getParentFile(), MessageFormat.format("{0}.{1}", newName, fileExtension));
		EntityDefinition<?> entityDefinition = initEntityDefinition(renamedJavaFile);
		EntityPageGenerator entityWebGenerator = null;
		if (entityDefinition instanceof ScreenEntityDefinition) {
			entityWebGenerator = getOrCreateApplicationContext(projectPath).getBean(ScreenEntityPageGenerator.class);
		} else {
			entityWebGenerator = getOrCreateApplicationContext(projectPath).getBean(RpcEntityPageGenerator.class);
		}
		entityWebGenerator.renameViews(fileNoExtension, newName, projectPath);
		String sourceFolder = getPreferences(projectPath, PreferencesConstants.API_SOURCE_FOLDER);
		entityWebGenerator.renameMatchesInJava(fileNoExtension, newName, projectPath, sourceFolder);
	}

	@Override
	public String translate(String text, File projectPath) {
		TextTranslator translator = getOrCreateApplicationContext(projectPath).getBean(TextTranslator.class);
		if (translator != null) {
			return translator.translate(text);
		}
		return text;
	}

	@Override
	public BackendSolution getServiceType(File projectPath) {
		String backendSolution = getPreferences(projectPath).get(PreferencesConstants.BACKEND_SOLUTION);
		if (!StringUtils.isEmpty(backendSolution)
				&& StringUtils.equalsIgnoreCase(backendSolution, BackendSolution.RPC.toString())) {
			return BackendSolution.RPC;
		} else if (!StringUtils.isEmpty(backendSolution)
				&& StringUtils.equalsIgnoreCase(backendSolution, BackendSolution.JDBC.toString())) {
			return BackendSolution.JDBC;
		}
		return BackendSolution.SCREEN;
	}

	@Override
	public void obfuscateTrail(File trailFile) {
		FileOutputStream out = null;
		try {
			TrailObfuscator trailObfuscater = getOrCreateApplicationContext(getProjectPath(trailFile)).getBean(
					TrailObfuscator.class);
			TerminalPersistedTrail trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class, new FileInputStream(
					trailFile));
			trailObfuscater.obfuscate(trail);
			out = new FileOutputStream(trailFile);
			XmlSerializationUtil.serialize(TerminalPersistedTrail.class, trail, out);
		} catch (Exception e) {
			throw (new RuntimeException(e));
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	private static void setDbDdlAutoValue(String ddlAutoValue, File targetPath) throws FileNotFoundException, IOException {
		File persistenceFile = new File(targetPath, PERSISTENCE_XML_PATH);

		if (!persistenceFile.exists()) {
			logger.error(MessageFormat.format("Unable to find persistence.xml within {0}", targetPath));
			return;
		}

		String persistenceFileContent = IOUtils.toString(new FileInputStream(persistenceFile));

		if (ddlAutoValue != null || ddlAutoValue != "") {
			persistenceFileContent = persistenceFileContent.replaceFirst(
					"<property name=\"hibernate\\.hbm2ddl\\.auto\" value=\"update\"/>", MessageFormat.format(
							"<property name=\"hibernate\\.hbm2ddl\\.auto\" value=\"{0}\"/>", ddlAutoValue));
			try {
				FileUtils.write(persistenceFileContent, persistenceFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isSupportRestControllerGeneration(File file) {
		File projectPath = getProjectPath(file);
		String backendSolution = getPreferences(projectPath).get(PreferencesConstants.BACKEND_SOLUTION);
		EntityPageGenerator entityPageGenerator = null;
		if (!StringUtils.isBlank(backendSolution)
				&& StringUtils.equalsIgnoreCase(backendSolution, BackendSolution.JDBC.toString())) {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(DbEntityPageGenerator.class);
		} else if (!StringUtils.isBlank(backendSolution)
				&& StringUtils.equalsIgnoreCase(backendSolution, BackendSolution.RPC.toString())) {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(RpcEntityPageGenerator.class);
		} else if (!StringUtils.isBlank(backendSolution)
				&& StringUtils.equalsIgnoreCase(backendSolution, BackendSolution.SCREEN.toString())) {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(ScreenEntityPageGenerator.class);
		}
		return entityPageGenerator != null && entityPageGenerator.isSupportRestControllerGeneration();
	}

	@Override
	public void generateRestController(GenerateControllerRequest generateControllerRequest) {

		EntityDefinition<?> entityDefinition = initEntityDefinition(generateControllerRequest.getEntitySourceFile());

		File projectPath = getProjectPath(generateControllerRequest.getSourceDirectory());
		EntityPageGenerator entityPageGenerator = null;
		if (entityDefinition instanceof ScreenEntityDefinition) {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(ScreenEntityPageGenerator.class);
		} else if (entityDefinition instanceof RpcEntityDefinition) {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(RpcEntityPageGenerator.class);
		} else {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(DbEntityPageGenerator.class);
		}

		if (entityPageGenerator != null && entityPageGenerator.isSupportRestControllerGeneration()) {
			entityPageGenerator.generateRestController(generateControllerRequest, entityDefinition);
		} else {
			logger.warn(MessageFormat.format("{0} doesnt support controller generation", entityPageGenerator.getClass()));
		}
	}

	private static void setDbDialect(String dbDialect, File targetPath) throws FileNotFoundException, IOException {
		File persistenceFile = new File(targetPath, PERSISTENCE_XML_PATH);

		if (!persistenceFile.exists()) {
			logger.error(MessageFormat.format("Unable to find persistence.xml within {0}", targetPath));
			return;
		}

		String persistenceFileContent = IOUtils.toString(new FileInputStream(persistenceFile));

		if (dbDialect != null || dbDialect != "") {
			persistenceFileContent = persistenceFileContent.replaceFirst("<property name=\"hibernate\\.dialect\" value=\".+\"/>",
					MessageFormat.format("<property name=\"hibernate\\.dialect\" value=\"{0}\"/>", dbDialect));
			try {
				FileUtils.write(persistenceFileContent, persistenceFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void configureIntegrationProject(File targetPath, ProjectCreationRequest projectCreationRequest)
			throws FileNotFoundException, IOException {
		if (projectCreationRequest.isRestFulService()) {
			// 1. maven files
			File pomFile = new File(targetPath, "pom.xml");

			if (!pomFile.exists()) {
				logger.error(MessageFormat.format("Unable to find pom.xml within {0}", targetPath));
				return;
			}

			FileInputStream fis = new FileInputStream(pomFile);
			String pomFileContent = IOUtils.toString(fis);
			IOUtils.closeQuietly(fis);
			// 1.1 replace <property>
			pomFileContent = pomFileContent.replaceFirst("<cxf.version>.*</cxf.version>", "<jersey-version>1.19</jersey-version>");

			// 1.2 replace dependencies
			URL cxfResource = getClass().getResource("/replacement/cxf_pattern.xml");
			if (cxfResource == null) {
				logger.error("Unable to find cxf_mvn_dependencies_pattern.xml within classpath");
				return;
			}
			InputStream inputStream = cxfResource.openStream();
			String cxfResourceContent = IOUtils.toString(inputStream);
			IOUtils.closeQuietly(inputStream);

			URL jerseyMvnResource = getClass().getResource("/replacement/jersey_mvn_dependencies.xml");
			if (jerseyMvnResource == null) {
				logger.error("Unable to find jersey_mvn_dependencies.xml within classpath");
				return;
			}
			inputStream = jerseyMvnResource.openStream();
			String jerseyResourceContent = IOUtils.toString(inputStream);
			IOUtils.closeQuietly(inputStream);

			Pattern pattern = Pattern.compile(cxfResourceContent, Pattern.DOTALL);
			pomFileContent = pattern.matcher(pomFileContent).replaceFirst(jerseyResourceContent);

			FileUtils.write(pomFileContent, pomFile);

			// 2. web.xml
			File webXmlFile = new File(targetPath, "src/main/webapp/WEB-INF/web.xml");
			if (!webXmlFile.exists()) {
				logger.error(MessageFormat.format("Unable to find web.xml within {0}", targetPath));
				return;
			}
			fis = new FileInputStream(webXmlFile);
			String webXmlFileContent = IOUtils.toString(fis);
			IOUtils.closeQuietly(fis);

			URL jerseyServletResource = getClass().getResource("/replacement/jersey_servlet.xml");
			if (jerseyServletResource == null) {
				logger.error("Unable to find jersey_servlet.xml within classpath");
				return;
			}
			inputStream = jerseyServletResource.openStream();
			jerseyResourceContent = IOUtils.toString(inputStream);
			IOUtils.closeQuietly(inputStream);

			jerseyResourceContent = MessageFormat.format(jerseyResourceContent, projectCreationRequest.getDefaultPackageName());
			webXmlFileContent = pattern.matcher(webXmlFileContent).replaceFirst(jerseyResourceContent);

			jerseyServletResource = getClass().getResource("/replacement/jersey_servlet_mapping.xml");
			if (jerseyServletResource == null) {
				logger.error("Unable to find jersey_servlet_mapping.xml within classpath");
				return;
			}
			inputStream = jerseyServletResource.openStream();
			jerseyResourceContent = IOUtils.toString(inputStream);
			IOUtils.closeQuietly(inputStream);

			webXmlFileContent = pattern.matcher(webXmlFileContent).replaceFirst(jerseyResourceContent);

			FileUtils.write(webXmlFileContent, webXmlFile);

			// 3. remove serviceContext.xml
			File serviceContextFile = new File(targetPath, DesigntimeConstants.SERVICE_CONTEXT_RELATIVE_PATH);
			if (serviceContextFile.exists()) {
				serviceContextFile.delete();
			}
			// 4. process applicationContext.xml
			File defaultSpringContextFile = new File(targetPath, DesigntimeConstants.DEFAULT_SPRING_CONTEXT_FILE);
			fis = new FileInputStream(defaultSpringContextFile);
			String contextFileContent = IOUtils.toString(fis);
			IOUtils.closeQuietly(fis);

			contextFileContent = contextFileContent.replaceAll(
					"<import\\s?resource=\"classpath:/META-INF/spring/serviceContext.xml\"\\s?/>", "");

			// add new "context:component-scan..." into the end of file
			int indexOf = contextFileContent.indexOf("</beans>");
			StringBuilder sb = new StringBuilder(contextFileContent);
			contextFileContent = sb.insert(
					indexOf,
					MessageFormat.format("\t<context:component-scan base-package=\"{0}.services.controllers\" />\n",
							projectCreationRequest.getDefaultPackageName())).toString();
			FileUtils.write(contextFileContent, defaultSpringContextFile);

			// 5. process applicationContext-test.xml
			File defaultSpringTestContextFile = new File(targetPath, DEFAULT_SPRING_TEST_CONTEXT_FILE);
			fis = new FileInputStream(defaultSpringTestContextFile);
			contextFileContent = IOUtils.toString(fis);
			IOUtils.closeQuietly(fis);

			contextFileContent = contextFileContent.replaceAll(
					"<import\\s?resource=\"classpath:/META-INF/spring/serviceContext.xml\"\\s?/>", "");
			FileUtils.write(contextFileContent, defaultSpringTestContextFile);
		}
	}
}
