/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzer;
import org.openlegacy.designtime.generators.EntityPageGenerator;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.newproject.ITemplateFetcher;
import org.openlegacy.designtime.newproject.model.ProjectTheme;
import org.openlegacy.designtime.rpc.GenerateRpcModelRequest;
import org.openlegacy.designtime.rpc.generators.RpcEntityPageGenerator;
import org.openlegacy.designtime.rpc.generators.RpcPojosAjGenerator;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcCodeBasedDefinitionUtils;
import org.openlegacy.designtime.rpc.model.support.SimpleRpcEntityDesigntimeDefinition;
import org.openlegacy.designtime.rpc.source.CodeParser;
import org.openlegacy.designtime.rpc.source.parsers.ParseResults;
import org.openlegacy.designtime.terminal.GenerateScreenModelRequest;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsAnalyzer;
import org.openlegacy.designtime.terminal.generators.ScreenEntityPageGenerator;
import org.openlegacy.designtime.terminal.generators.ScreenPojosAjGenerator;
import org.openlegacy.designtime.terminal.generators.TrailJunitGenerator;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenCodeBasedDefinitionUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotXmlRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotTextRenderer;
import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.StringUtil;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenLegacy main design-time API entry point. Consolidate all design-time common UI actions
 * 
 * @see DesignTimeExecuter
 */
public class DesignTimeExecuterImpl implements DesignTimeExecuter {

	private final static Log logger = LogFactory.getLog(DesignTimeExecuterImpl.class);

	private static final String DEFAULT_SPRING_CONTEXT_FILE = "/src/main/resources/META-INF/spring/applicationContext.xml";
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

	private ApplicationContext defaultDesigntimeApplicationContext;

	// map of project path to Spring application context
	private Map<String, ApplicationContext> projectsDesigntimeAplicationContexts = new HashMap<String, ApplicationContext>();

	private Map<File, ProjectPreferences> projectsPreferences = new HashMap<File, ProjectPreferences>();

	public void createProject(ProjectCreationRequest projectCreationRequest) throws IOException {
		ITemplateFetcher templateFetcher = projectCreationRequest.getTemplateFetcher();

		File targetPath = templateFetcher.fetchZip(projectCreationRequest.getTemplateName(),
				projectCreationRequest.getProjectName(), projectCreationRequest.getBaseDir());

		if (projectCreationRequest.isDemo()) {
			return;
		}

		// maven files
		renameProjectProperties(projectCreationRequest.getProjectName(), targetPath);
		renameProviderInPOM(projectCreationRequest.getProvider(), targetPath);
		uncommentDependencies(targetPath);

		if (projectCreationRequest.isSupportTheme()) {
			renameThemeInPOM(projectCreationRequest.getProjectTheme(), targetPath);
			renameThemeInAppProperties(projectCreationRequest.getProjectTheme(), targetPath);
		}

		// spring files
		updateSpringContextWithDefaultPackage(projectCreationRequest.getDefaultPackageName(), targetPath);
		uncommentMockConnection(projectCreationRequest.getProvider(), targetPath);

		// eclipse files
		renameProject(projectCreationRequest.getProjectName(), targetPath);
		renameLaunchers(projectCreationRequest.getProjectName(), targetPath);

		updatePropertiesFile(projectCreationRequest, targetPath);

		savePreference(targetPath, PreferencesConstants.API_PACKAGE, projectCreationRequest.getDefaultPackageName());
		savePreference(targetPath, PreferencesConstants.WEB_PACKAGE, projectCreationRequest.getDefaultPackageName() + ".web");
		savePreference(targetPath, PreferencesConstants.DESIGNTIME_CONTEXT, "default");
		savePreference(targetPath, PreferencesConstants.USE_AJ, "1");

		templateFetcher.deleteZip();
	}

	private static void uncommentDependencies(File targetPath) throws IOException {
		File pomFile = new File(targetPath, "pom.xml");
		String pomFileContent = IOUtils.toString(new FileInputStream(pomFile));

		pomFileContent = pomFileContent.replaceAll(dependencyCommentStart, "");
		pomFileContent = pomFileContent.replaceAll(dependencyCommentEnd, "");

		FileOutputStream fos = new FileOutputStream(pomFile);
		IOUtils.write(pomFileContent, fos);
	}

	private static void uncommentMockConnection(String provider, File targetPath) throws IOException {
		if (!provider.equals("mock-up")) {
			return;
		}
		File springContextFile = new File(targetPath, "src/main/resources/META-INF/spring/applicationContext.xml");
		if (!springContextFile.exists()) {
			return;
		}
		String springContextFileContent = IOUtils.toString(new FileInputStream(springContextFile));

		springContextFileContent = springContextFileContent.replace(mockupSessionCommentStart, "");
		springContextFileContent = springContextFileContent.replace(mockupSessionCommentEnd, "");

		FileOutputStream fos = new FileOutputStream(springContextFile);
		IOUtils.write(springContextFileContent, fos);

	}

	private static void updatePropertiesFile(ProjectCreationRequest projectCreationRequest, File targetPath) throws IOException,
			FileNotFoundException {
		File hostPropertiesFile = new File(targetPath, "src/main/resources/host.properties");
		if (hostPropertiesFile.exists()) {
			String hostPropertiesFileContent = IOUtils.toString(new FileInputStream(hostPropertiesFile));

			hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.name=.*",
					MessageFormat.format("host.name={0}", projectCreationRequest.getHostName()));

			hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.port=.*",
					MessageFormat.format("host.port={0}", String.valueOf(projectCreationRequest.getHostPort())));
			hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.codePage=.*",
					MessageFormat.format("host.codePage={0}", projectCreationRequest.getCodePage()));
			FileOutputStream fos = new FileOutputStream(hostPropertiesFile);
			IOUtils.write(hostPropertiesFileContent, fos);
		}

		File rpcPropertiesFile = new File(targetPath, "src/main/resources/rpc.properties");
		if (rpcPropertiesFile.exists()) {
			String rpcPropertiesFileContent = IOUtils.toString(new FileInputStream(rpcPropertiesFile));

			rpcPropertiesFileContent = rpcPropertiesFileContent.replaceFirst("rpc.host.name=.*",
					MessageFormat.format("rpc.host.name={0}", projectCreationRequest.getHostName()));

			FileOutputStream fos = new FileOutputStream(rpcPropertiesFile);
			IOUtils.write(rpcPropertiesFileContent, fos);
		}
	}

	private static void renameLaunchers(final String projectName, final File targetPath) throws FileNotFoundException,
			IOException {
		targetPath.listFiles(new FileFilter() {

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
		FileOutputStream fos = new FileOutputStream(launcherFile);
		IOUtils.write(launchFileContent, fos);

	}

	private static void updateSpringContextWithDefaultPackage(String defaultPackageName, File targetPath) throws IOException,
			FileNotFoundException {
		updateSpringFile(defaultPackageName, new File(targetPath, DEFAULT_SPRING_CONTEXT_FILE));
		updateSpringFile(defaultPackageName, new File(targetPath, DEFAULT_SPRING_WEB_CONTEXT_FILE));
		updateSpringFile(defaultPackageName, new File(targetPath, DEFAULT_SPRING_TEST_CONTEXT_FILE));
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
		springFileContent = springFileContent.replaceFirst("<context:component-scan base-package=\".*\"",
				MessageFormat.format("<context:component-scan base-package=\"{0}\"", defaultPackageName));

		/*
		 * Replace apps.inventory.screens with default package
		 */
		springFileContent = springFileContent.replaceFirst("<value>[a-z_0-9\\.]+</value>",
				MessageFormat.format("<value>{0}</value>", defaultPackageName));
		FileOutputStream fos = new FileOutputStream(springFile);
		IOUtils.write(springFileContent, fos);
	}

	private static void renameProject(String projectName, File targetPath) throws IOException, FileNotFoundException {
		File projectFile = new File(targetPath, ".project");
		String projectFileContent = IOUtils.toString(new FileInputStream(projectFile));

		// NOTE assuming all project templates starts with "openlegacy-"
		projectFileContent = projectFileContent.replaceAll("<name>openlegacy-.*</name>",
				MessageFormat.format("<name>{0}</name>", projectName));
		FileOutputStream fos = new FileOutputStream(projectFile);
		IOUtils.write(projectFileContent, fos);
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
		pomFileContent = replaceFirstAttribute("warName", projectName, pomFileContent);
		pomFileContent = replaceFirstAttribute("version", DEFAULT_NEW_PROJECT_VERSION, pomFileContent);
		String openlegacyVersion = DesignTimeExecuterImpl.class.getPackage().getImplementationVersion();
		if (openlegacyVersion != null) {
			pomFileContent = replaceFirstAttribute("openlegacy-version", openlegacyVersion, pomFileContent);
		}

		FileOutputStream fos = new FileOutputStream(pomFile);
		IOUtils.write(pomFileContent, fos);
	}

	private static String replaceFirstAttribute(String attributeName, String attributeValue, String pomFileContent) {
		String stringToReplace = MessageFormat.format("<{0}>.*</{0}>", attributeName);
		return pomFileContent.replaceFirst(stringToReplace, MessageFormat.format("<{0}>{1}</{0}>", attributeName, attributeValue));

	}

	private static void renameProviderInPOM(String provider, File targetPath) throws FileNotFoundException, IOException {
		File pomFile = new File(targetPath, "pom.xml");

		if (!pomFile.exists()) {
			logger.error(MessageFormat.format("Unable to find pom.xml within {0}", targetPath));
			return;
		}

		String pomFileContent = IOUtils.toString(new FileInputStream(pomFile));

		if (!provider.equals("mock-up")) {

			// tn5250j or impl is the default pom setting
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

		FileOutputStream fos = new FileOutputStream(pomFile);
		IOUtils.write(pomFileContent, fos);
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

			// rename theme for mobile
			pomFileContent = pomFileContent.replaceFirst(
					"<groupId>org.openlegacy.web</groupId>\\s+<artifactId>openlegacy-themes-\\w+(.*?)(?<=m)</artifactId>",
					MessageFormat.format("<groupId>org.openlegacy.web</groupId>\n\t\t\t<artifactId>{0}</artifactId>",
							projectTheme.getMobileTheme()));
			FileOutputStream fos = new FileOutputStream(pomFile);
			IOUtils.write(pomFileContent, fos);
		}
	}

	private static void renameThemeInAppProperties(ProjectTheme projectTheme, File targetPath) throws FileNotFoundException,
			IOException {
		File appPropertiesFile = new File(targetPath, "src/main/resources/application.properties");

		if (!appPropertiesFile.exists()) {
			logger.error(MessageFormat.format("Unable to find application.properties within {0}", targetPath));
			return;
		}

		String appPropertiesFileContent = IOUtils.toString(new FileInputStream(appPropertiesFile));

		if (projectTheme != null) {
			appPropertiesFileContent = appPropertiesFileContent.replaceFirst("themeUtil.defaultTheme=.*",
					MessageFormat.format("themeUtil.defaultTheme={0}", projectTheme.getDisplayName().toLowerCase()));
			// get mobile theme name
			String[] split = projectTheme.getMobileTheme().split("-");
			// suppose, that last element of String array is mobile theme name
			appPropertiesFileContent = appPropertiesFileContent.replaceFirst("themeUtil.defaultMobileTheme=.*",
					MessageFormat.format("themeUtil.defaultMobileTheme={0}", split[split.length - 1].toLowerCase()));
			FileOutputStream fos = new FileOutputStream(appPropertiesFile);
			IOUtils.write(appPropertiesFileContent, fos);
		}
	}

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

		EntityUserInteraction<EntityDefinition<?>> entityUserInteraction = generateModelRequest.getEntityUserInteraction();
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setGenerateAspect(generateModelRequest.isGenerateAspectJ());

			boolean generated = generateScreenEntityDefinition(generateModelRequest, screenEntityDefinition);

			if (generated && screenDefinitions.size() == 1) {
				File packageDir = new File(generateModelRequest.getSourceDirectory(), generateModelRequest.getPackageDirectory());
				String entityName = screenEntityDefinition.getEntityName();
				File targetJavaFile = new File(packageDir, MessageFormat.format("{0}.java", entityName));
				entityUserInteraction.open(targetJavaFile);
			}
		}

		generateTest(generateModelRequest.getTrailFile(), screenDefinitions, generateModelRequest.getProjectPath());

	}

	public boolean generateScreenEntityDefinition(GenerateScreenModelRequest generateScreenModelRequest,
			ScreenEntityDefinition entityDefinition) {

		EntityUserInteraction<EntityDefinition<?>> entityUserInteraction = generateScreenModelRequest.getEntityUserInteraction();

		if (entityUserInteraction != null) {
			boolean generate = entityUserInteraction.customizeEntity(entityDefinition);
			if (!generate) {
				return false;
			}
		}

		((ScreenEntityDesigntimeDefinition)entityDefinition).setPackageName(generateScreenModelRequest.getPackageDirectory().replaceAll(
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
			return true;

		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}

	}

	public boolean generateRpcEntityDefinition(GenerateRpcModelRequest generateRpcModelRequest,
			RpcEntityDefinition entityDefinition) {

		EntityUserInteraction<EntityDefinition<?>> entityUserInteraction = generateRpcModelRequest.getEntityUserInteraction();

		if (entityUserInteraction != null) {
			boolean generate = entityUserInteraction.customizeEntity(entityDefinition);
			if (!generate) {
				return false;
			}
		}

		((SimpleRpcEntityDesigntimeDefinition)entityDefinition).setPackageName(generateRpcModelRequest.getPackageDirectory().replaceAll(
				"/", "."));

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
		FileOutputStream fos = null;
		File renderedScreenResourceFile = null;
		try {
			renderedScreenResourceFile = new File(screenResourcesDir, MessageFormat.format("{0}.{1}", entityName,
					renderer.getFileFormat()));
			if (!renderedScreenResourceFile.exists()) {
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
				String prefix = designtimeContextFile.getAbsolutePath().startsWith("/") ? "" : "/";
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

	public void generateAspect(File javaFile) {

		try {
			FileInputStream input = new FileInputStream(javaFile);
			CompilationUnit compilationUnit = JavaParser.parse(input, CharEncoding.UTF_8);

			if (JavaParserUtil.hasAnnotation(compilationUnit, ScreenAnnotationConstants.SCREEN_ENTITY_ANNOTATION,
					ScreenAnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)) {
				ScreenPojosAjGenerator generator = getOrCreateApplicationContext(getProjectPath(javaFile)).getBean(
						ScreenPojosAjGenerator.class);
				generator.generate(javaFile, compilationUnit);
			} else if (JavaParserUtil.hasAnnotation(compilationUnit, RpcAnnotationConstants.RPC_ENTITY_ANNOTATION,
					RpcAnnotationConstants.RPC_ENTITY_SUPER_CLASS_ANNOTATION)) {
				RpcPojosAjGenerator generator = getOrCreateApplicationContext(getProjectPath(javaFile)).getBean(
						RpcPojosAjGenerator.class);
				generator.generate(javaFile, compilationUnit);

			}
		} catch (IOException e) {
			throw (new GenerationException(e));
		} catch (ParseException e) {
			logger.warn("Failed parsing java file:" + e.getMessage());
			// non compiled java class. Ignore it
		}

	}

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
	public void generateView(GenerateViewRequest generateViewRequest) throws GenerationException {

		EntityDefinition<?> entityDefinition = initEntityDefinition(generateViewRequest,
				generateViewRequest.getEntitySourceFile());

		File projectPath = getProjectPath(generateViewRequest.getEntitySourceFile());
		EntityPageGenerator entityWebGenerator = null;
		if (entityDefinition instanceof ScreenEntityDefinition) {
			entityWebGenerator = getOrCreateApplicationContext(projectPath).getBean(ScreenEntityPageGenerator.class);
		} else {
			entityWebGenerator = getOrCreateApplicationContext(projectPath).getBean(RpcEntityPageGenerator.class);
		}
		entityWebGenerator.generateView(generateViewRequest, entityDefinition);

	}

	/**
	 * Generates all required view files for a Spring MVC framework
	 */
	public void generateController(GenerateControllerRequest generateControllerRequest) throws GenerationException {

		EntityDefinition<?> entityDefinition = initEntityDefinition(generateControllerRequest,
				generateControllerRequest.getEntitySourceFile());

		File projectPath = getProjectPath(generateControllerRequest.getSourceDirectory());
		EntityPageGenerator entityPageGenerator = null;
		if (entityDefinition instanceof ScreenEntityDefinition) {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(ScreenEntityPageGenerator.class);
		} else {
			entityPageGenerator = getOrCreateApplicationContext(projectPath).getBean(RpcEntityPageGenerator.class);
		}

		if (entityPageGenerator.isSupportControllerGeneration()) {
			entityPageGenerator.generateController(generateControllerRequest, entityDefinition);
		} else {
			logger.warn(MessageFormat.format("{0} doesnt support controller generation", entityPageGenerator.getClass()));
		}
	}

	public boolean isSupportControllerGeneration(File entityFile) {
		return getOrCreateApplicationContext(getProjectPath(entityFile)).getBean(ScreenEntityPageGenerator.class).isSupportControllerGeneration();
	}

	private static EntityDefinition<?> initEntityDefinition(AbstractGenerateRequest generatePageRequest, File sourceFile) {
		EntityDefinition<?> entityDefinition = null;
		try {
			FileInputStream fis = new FileInputStream(sourceFile);
			String fileContent = IOUtils.toString(fis);
			CompilationUnit compilationUnit = JavaParser.parse(sourceFile, CharEncoding.UTF_8);
			File packageDir = new File(generatePageRequest.getSourceDirectory(),
					compilationUnit.getPackage().getName().toString().replaceAll("\\.", "/"));
			if (fileContent.contains(ScreenEntity.class.getSimpleName())) {
				entityDefinition = ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir);
			} else {
				entityDefinition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir);

			}
		} catch (Exception e) {
			throw (new GenerationException(e));
		}
		if (entityDefinition == null) {
			throw (new GenerationException(MessageFormat.format("{0} is not a screen entity", sourceFile.getName())));
		}
		return entityDefinition;
	}

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
			}
		} catch (IOException e) {
			throw (new GenerationException("Error creating custom templates", e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	public String getPreferences(File projectPath, String key) {
		ProjectPreferences perferences = getPreferences(projectPath);
		return perferences.get(key);
	}

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

	public void generateRpcModel(GenerateRpcModelRequest generateRpcModelRequest) {
		ApplicationContext projectApplicationContext = getOrCreateApplicationContext(generateRpcModelRequest.getProjectPath());

		getGenerateUtil().setTemplateDirectory(generateRpcModelRequest.getTemplatesDirectory());

		EntityUserInteraction<EntityDefinition<?>> entityUserInteraction = generateRpcModelRequest.getEntityUserInteraction();

		CodeParser codeParser = projectApplicationContext.getBean(CodeParser.class);

		String fileContent;
		RpcEntityDefinition rpcEntityDefinition = null;
		try {
			File sourceFile = generateRpcModelRequest.getSourceFile();
			fileContent = IOUtils.toString(new FileInputStream(sourceFile));
			ParseResults parseResults = codeParser.parse(fileContent, sourceFile.getName());

			rpcEntityDefinition = parseResults.getEntityDefinition();

			String name = FileUtils.fileWithoutAnyExtension(sourceFile.getName());
			((SimpleRpcEntityDesigntimeDefinition)rpcEntityDefinition).setEntityName(name);
		} catch (IOException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
		((SimpleRpcEntityDesigntimeDefinition)rpcEntityDefinition).setGenerateAspect(generateRpcModelRequest.isGenerateAspectJ());

		boolean generated = generateRpcEntityDefinition(generateRpcModelRequest, rpcEntityDefinition);

		if (generated) {
			File packageDir = new File(generateRpcModelRequest.getSourceDirectory(),
					generateRpcModelRequest.getPackageDirectory());
			String entityName = rpcEntityDefinition.getEntityName();
			File targetJavaFile = new File(packageDir, MessageFormat.format("{0}.java", entityName));
			entityUserInteraction.open(targetJavaFile);
		}
	}
}
