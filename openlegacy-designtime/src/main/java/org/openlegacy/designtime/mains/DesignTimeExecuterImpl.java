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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.designtime.PerfrencesConstants;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzer;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsAnalyzer;
import org.openlegacy.designtime.terminal.generators.GenerateUtil;
import org.openlegacy.designtime.terminal.generators.ScreenEntityJavaGenerator;
import org.openlegacy.designtime.terminal.generators.ScreenEntityMvcGenerator;
import org.openlegacy.designtime.terminal.generators.ScreenEntityWebGenerator;
import org.openlegacy.designtime.terminal.generators.ScreenPojosAjGenerator;
import org.openlegacy.designtime.terminal.generators.TrailJunitGenerator;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedDefinitionUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotXmlRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotTextRenderer;
import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.ZipUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
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

	private static final String RUN_APPLICATION = "run-application.launch";
	private static final String BUILD_WAR = "build-application.launch";

	public static final String TEMPLATES_DIR = "templates";

	private static final String DEFAULT_TEMPLATES_PATTERN = "classpath*:/*.template";

	private static final String DEFAULT_NEW_PROJECT_VERSION = "0.1";

	private static final String TEST_SOURCE_DIR = "test/main/java";

	private static final String PERFERENCES_FILE = ".perferences";

	private ApplicationContext applicationContext;

	private File designtimeContextFile;

	private Map<File, ProjectPerferences> projectsPerferences = new HashMap<File, ProjectPerferences>();

	public void createProject(ProjectCreationRequest projectCreationRequest) throws IOException {
		File targetZip = extractTemplate(projectCreationRequest.getTemplateName(), projectCreationRequest.getBaseDir());
		File targetPath = unzipTemplate(projectCreationRequest.getBaseDir(), projectCreationRequest.getProjectName(), targetZip);

		if (projectCreationRequest.isDemo()) {
			return;
		}

		// maven files
		renameProjectProperties(projectCreationRequest.getProjectName(), targetPath);
		renameProviderInPOM(projectCreationRequest.getProvider(), targetPath);

		// spring files
		updateSpringContextWithDefaultPackage(projectCreationRequest.getDefaultPackageName(), targetPath);

		// eclipse files
		renameProject(projectCreationRequest.getProjectName(), targetPath);
		renameLaunchers(projectCreationRequest.getProjectName(), targetPath);

		updateHostPropertiesFile(projectCreationRequest, targetPath);

		savePerference(targetPath, PerfrencesConstants.API_PACKAGE, projectCreationRequest.getDefaultPackageName());
		savePerference(targetPath, PerfrencesConstants.WEB_PACKAGE, projectCreationRequest.getDefaultPackageName() + ".web");
		targetZip.delete();
	}

	private static void updateHostPropertiesFile(ProjectCreationRequest projectCreationRequest, File targetPath)
			throws IOException, FileNotFoundException {
		File hostPropertiesFile = new File(targetPath, "src/main/resources/host.properties");
		String hostPropertiesFileContent = IOUtils.toString(new FileInputStream(hostPropertiesFile));

		hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.name=.*",
				MessageFormat.format("host.name={0}", projectCreationRequest.getHostName()));

		hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.port=.*",
				MessageFormat.format("host.port={0}", projectCreationRequest.getHostPort()));
		hostPropertiesFileContent = hostPropertiesFileContent.replaceFirst("host.codePage=.*",
				MessageFormat.format("host.codePage={0}", projectCreationRequest.getCodePage()));
		FileOutputStream fos = new FileOutputStream(hostPropertiesFile);
		IOUtils.write(hostPropertiesFileContent, fos);
	}

	private static void renameLaunchers(String projectName, File targetPath) throws FileNotFoundException, IOException {
		renameLauncher(projectName, targetPath, RUN_APPLICATION);
		renameLauncher(projectName, targetPath, BUILD_WAR);
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

		springFileContent = springFileContent.replaceFirst("<context:component-scan base-package=\".*\"",
				MessageFormat.format("<context:component-scan base-package=\"{0}\"", defaultPackageName));
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

		if (!provider.equals(DesignTimeExecuter.MOCK_PROVIDER)) {
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

	private static File unzipTemplate(File baseDir, String projectName, File targetZip) throws IOException {
		File targetPath = new File(baseDir, projectName);
		ZipUtil.unzip(targetZip.getAbsolutePath(), targetPath.getAbsolutePath());
		return targetPath;
	}

	private File extractTemplate(String templateName, File baseDir) throws FileNotFoundException, IOException {
		URL zipFile = getClass().getResource(MessageFormat.format("/templates/{0}.zip", templateName));

		File targetZip = new File(baseDir, templateName + ".zip");
		FileOutputStream targetZipOutputStream = new FileOutputStream(targetZip);
		IOUtils.copy(zipFile.openStream(), targetZipOutputStream);
		targetZipOutputStream.close();
		return targetZip;
	}

	public void generateScreens(GenerateScreenRequest generateScreenRequest) throws GenerationException {
		// initialize application context
		getApplicationContext(generateScreenRequest.getAnalyzerContextFile());

		getGenerateUtil().setTemplateDirectory(generateScreenRequest.getCodeGenerationTemplatesDirectory());

		TerminalSnapshotsAnalyzer snapshotsAnalyzer = applicationContext.getBean(TerminalSnapshotsAnalyzer.class);

		FileInputStream trailInputStream;
		try {
			trailInputStream = new FileInputStream(generateScreenRequest.getTrailFile().getAbsolutePath());
		} catch (FileNotFoundException e1) {
			throw (new GenerationException(e1));
		}
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeTrail(trailInputStream);
		Collection<ScreenEntityDefinition> screenDefinitions = screenEntitiesDefinitions.values();
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setPackageName(generateScreenRequest.getPackageDirectory().replaceAll(
					"/", "."));

			EntityUserInteraction<ScreenEntityDefinition> entityUserInteraction = generateScreenRequest.getEntityUserInteraction();
			if (entityUserInteraction != null) {
				boolean generate = entityUserInteraction.customizeEntity(screenEntityDefinition);
				if (!generate) {
					continue;
				}
			}

			try {
				File packageDir = new File(generateScreenRequest.getSourceDirectory(),
						generateScreenRequest.getPackageDirectory());

				String entityName = screenEntityDefinition.getEntityName();
				File targetJavaFile = new File(packageDir, MessageFormat.format("{0}.java", entityName));
				if (targetJavaFile.exists()) {
					boolean override = entityUserInteraction != null && entityUserInteraction.isOverride(targetJavaFile);
					if (!override) {
						continue;
					}
				}
				generateJava(screenEntityDefinition, targetJavaFile);
				generateAspect(targetJavaFile);

				File screenResourcesDir = new File(packageDir, entityName + "-resources");
				screenResourcesDir.mkdir();
				TerminalSnapshot snapshot = screenEntityDefinition.getOriginalSnapshot();

				TerminalSnapshotImageRenderer imageRenderer = applicationContext.getBean(TerminalSnapshotImageRenderer.class);
				TerminalSnapshotTextRenderer textRenderer = applicationContext.getBean(TerminalSnapshotTextRenderer.class);
				DefaultTerminalSnapshotXmlRenderer xmlRenderer = applicationContext.getBean(DefaultTerminalSnapshotXmlRenderer.class);

				// generate txt file with screen content
				generateResource(snapshot, entityName, screenResourcesDir, textRenderer);
				// generate jpg file with screen image
				generateResource(snapshot, entityName, screenResourcesDir, imageRenderer);
				// generate xml file with screen XML for testing purposes
				generateResource(snapshot, entityName, screenResourcesDir, xmlRenderer);

			} catch (TemplateException e) {
				throw (new GenerationException(e));
			} catch (IOException e) {
				throw (new GenerationException(e));
			}
		}

		generateTest(generateScreenRequest.getTrailFile(), screenDefinitions, generateScreenRequest.getProjectPath());

	}

	private GenerateUtil getGenerateUtil() {
		return applicationContext.getBean(GenerateUtil.class);
	}

	private void generateTest(File trailFile, Collection<ScreenEntityDefinition> screenDefinitions, File projectPath) {
		TrailJunitGenerator generator = applicationContext.getBean(TrailJunitGenerator.class);
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
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		} finally {
			FileUtils.deleteEmptyFile(junitFile);

		}

	}

	private void generateJava(ScreenEntityDefinition screenEntityDefinition, File file) throws FileNotFoundException,
			TemplateException, IOException {

		FileOutputStream fos = null;
		try {
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);

			ScreenEntityJavaGenerator screenEntityJavaGenerator = applicationContext.getBean(ScreenEntityJavaGenerator.class);

			screenEntityJavaGenerator.generate(screenEntityDefinition, fos);
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
			fos = new FileOutputStream(renderedScreenResourceFile);
			renderer.render(terminalSnapshot, fos);
		} catch (FileNotFoundException e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
			FileUtils.deleteEmptyFile(renderedScreenResourceFile);
		}
	}

	private synchronized ApplicationContext getApplicationContext(File designtimeContextFile) {
		// in case the context file is not longer the default one
		if (applicationContext == null
				|| (designtimeContextFile != null && designtimeContextFile.exists() && this.designtimeContextFile == null)) {
			if (designtimeContextFile != null && designtimeContextFile.exists()) {
				this.designtimeContextFile = designtimeContextFile;
				applicationContext = new FileSystemXmlApplicationContext(designtimeContextFile.getAbsolutePath());
			} else {
				applicationContext = new ClassPathXmlApplicationContext("/openlegacy-default-designtime-context.xml");
			}
		}
		return applicationContext;
	}

	public void generateAspect(File javaFile) {

		OutputStream fos = null;
		try {
			ScreenPojosAjGenerator generator = getApplicationContext(null).getBean(ScreenPojosAjGenerator.class);
			generator.generate(javaFile);
		} catch (IOException e) {
			throw (new GenerationException(e));
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (ParseException e) {
			logger.warn("Failed parsing java file:" + e.getMessage());
			// non compiled java class. Ignore it
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}

	public void initialize(File analyzerContextFile) {
		// initialize application context & analyzer
		applicationContext = null;
		getApplicationContext(analyzerContextFile).getBean(SnapshotsAnalyzer.class);
	}

	/**
	 * Generates all required file for a Spring MVC framework
	 */
	public void generateWebPage(GeneratePageRequest generatePageRequest) throws GenerationException {

		ScreenEntityDefinition screenEntityDefinition = null;
		try {
			CompilationUnit compilationUnit = JavaParser.parse(generatePageRequest.getScreenEntitySourceFile());
			File packageDir = new File(generatePageRequest.getSourceDirectory(),
					compilationUnit.getPackage().getName().toString().replaceAll("\\.", "/"));
			screenEntityDefinition = CodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir);
		} catch (Exception e) {
			throw (new GenerationException(e));
		}
		if (screenEntityDefinition == null) {
			throw (new GenerationException(MessageFormat.format("{0} is not a screen entity",
					generatePageRequest.getScreenEntitySourceFile().getName())));
		}

		ScreenEntityWebGenerator screenEntityWebGenerator = getApplicationContext(null).getBean(ScreenEntityMvcGenerator.class);

		screenEntityWebGenerator.generateAll(generatePageRequest, screenEntityDefinition);
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
				String filename = resource.getFilename();
				fos = new FileOutputStream(new File(templatesDir, filename));
				IOUtils.copy(resource.getInputStream(), fos);
			}
		} catch (IOException e) {
			throw (new GenerationException("Error creating custom templates", e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	public String getPerferences(File projectPath, String key) {
		ProjectPerferences perferences = getPerferences(projectPath);
		return perferences.get(key);
	}

	public void savePerference(File projectPath, String key, String value) {
		ProjectPerferences perferences = getPerferences(projectPath);
		perferences.put(key, value);

	}

	private ProjectPerferences getPerferences(File projectPath) {
		ProjectPerferences perferences = projectsPerferences.get(projectPath);
		if (perferences != null) {
			return perferences;
		}

		File prefFile = new File(projectPath, PERFERENCES_FILE);

		ProjectPerferences perfernces = new SimpleProjectPerferences(prefFile);
		projectsPerferences.put(prefFile, perferences);

		return perfernces;
	}

	public void copyDesigntimeContext(File projectPath) {
		File customDesigntimeFile = new File(projectPath, DesignTimeExecuter.CUSTOM_DESIGNTIME_CONTEXT_RELATIVE_PATH);
		customDesigntimeFile.getParentFile().mkdirs();

		FileOutputStream fos = null;
		try {
			InputStream defaultDesigntimeStream = getClass().getResourceAsStream(
					DesignTimeExecuter.DEFAULT_DESIGNTIME_CONTEXT_FILE_NAME);
			fos = new FileOutputStream(customDesigntimeFile);
			IOUtils.copy(defaultDesigntimeStream, fos);
		} catch (IOException e) {
			throw (new GenerationException("Error creating custom templates", e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

}
