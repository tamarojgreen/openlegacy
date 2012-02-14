package org.openlegacy.designtime.mains;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzer;
import org.openlegacy.designtime.analyzer.support.AbstractSnapshotsOrganizer;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsAnalyzer;
import org.openlegacy.designtime.terminal.generators.ScreenEntityJavaGenerator;
import org.openlegacy.designtime.terminal.generators.ScreenPojosAjGenerator;
import org.openlegacy.designtime.terminal.generators.TrailJunitGenerator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotTextRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotXmlRenderer;
import org.openlegacy.utils.FileUtils;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.ZipUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import japa.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

public class DesignTimeExecuterImpl implements DesignTimeExecuter {

	private final static Log logger = LogFactory.getLog(AbstractSnapshotsOrganizer.class);

	private static final String DEFAULT_SPRING_CONTEXT_FILE = "/src/main/resources/META-INF/spring/applicationContext.xml";

	private static final String RUN_APPLICATION = "run-application.launch";

	private ApplicationContext applicationContext;

	public void createProject(String templateName, File baseDir, String projectName, String provider, String defaultPackageName)
			throws IOException {
		File targetZip = extractTemplate(templateName, baseDir);
		File targetPath = unzipTemplate(baseDir, projectName, targetZip);

		// maven files
		renameProjectPOM(projectName, targetPath);
		renameProviderInPOM(provider, targetPath);

		// spring files
		updateSpringContextWithDefaultPackage(defaultPackageName, targetPath);

		// eclipse files
		renameProject(projectName, targetPath);
		renameLauncher(projectName, targetPath);

		targetZip.delete();
	}

	private static void renameLauncher(String projectName, File targetPath) throws FileNotFoundException, IOException {
		File launcherFile = new File(targetPath, RUN_APPLICATION);

		if (!launcherFile.exists()) {
			return;
		}

		String launchFileContent = IOUtils.toString(new FileInputStream(launcherFile));

		launchFileContent = launchFileContent.replaceAll("workspace_loc:.*}", ("workspace_loc:" + projectName + "}"));
		FileOutputStream fos = new FileOutputStream(launcherFile);
		IOUtils.write(launchFileContent, fos);

	}

	private static void updateSpringContextWithDefaultPackage(String defaultPackageName, File targetPath) throws IOException,
			FileNotFoundException {
		File springFile = new File(targetPath, DEFAULT_SPRING_CONTEXT_FILE);

		String springFileContent = IOUtils.toString(new FileInputStream(springFile));

		springFileContent = springFileContent.replaceAll("<context:component-scan base-package=\".*\" />",
				MessageFormat.format("<context:component-scan base-package=\"{0}\" />", defaultPackageName));
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

	private static void renameProjectPOM(String projectName, File targetPath) throws IOException, FileNotFoundException {
		File pomFile = new File(targetPath, "pom.xml");
		String pomFileContent = IOUtils.toString(new FileInputStream(pomFile));

		pomFileContent = pomFileContent.replaceFirst("<artifactId>.*</artifactId>",
				MessageFormat.format("<artifactId>{0}</artifactId>", projectName));
		FileOutputStream fos = new FileOutputStream(pomFile);
		IOUtils.write(pomFileContent, fos);
	}

	private static void renameProviderInPOM(String provider, File targetPath) throws FileNotFoundException, IOException {
		File pomFile = new File(targetPath, "pom.xml");
		String pomFileContent = IOUtils.toString(new FileInputStream(pomFile));

		if (!provider.equals(DesignTimeExecuter.MOCK_PROVIDER)) {
			pomFileContent = pomFileContent.replace(
					"<groupId>org.openlegacy</groupId>\\s+<artifactId>openlegacy-Impl</artifactId>", MessageFormat.format(
							"<groupId>org.openlegacy.providers</groupId>\\n\\t\\t\\t<artifactId>openlegacy-{0}</artifactId>",
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

	public void generateScreens(File trailFile, File sourceDirectory, String packageDirectoryName,
			OverrideConfirmer overrideConfirmer, File analyzerContextFile) throws GenerationException {
		ApplicationContext applicationContext = getApplicationContext(analyzerContextFile);
		TerminalSnapshotsAnalyzer snapshotsAnalyzer = applicationContext.getBean(TerminalSnapshotsAnalyzer.class);

		FileInputStream trailInputStream;
		try {
			trailInputStream = new FileInputStream(trailFile.getAbsolutePath());
		} catch (FileNotFoundException e1) {
			throw (new GenerationException(e1));
		}
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeTrail(trailInputStream);
		Collection<ScreenEntityDefinition> screenDefinitions = screenEntitiesDefinitions.values();
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setPackageName(packageDirectoryName.replaceAll("/", "."));
			try {
				File packageDir = new File(sourceDirectory, packageDirectoryName);
				String entityName = screenEntityDefinition.getEntityName();
				File file = new File(packageDir, MessageFormat.format("{0}.java", entityName));
				if (file.exists()) {
					boolean override = overrideConfirmer.isOverride(file);
					if (!override) {
						continue;
					}
				}
				generateJava(screenEntityDefinition, file);

				File screenResourcesDir = new File(packageDir, entityName + "-resources");
				screenResourcesDir.mkdir();
				TerminalSnapshot snapshot = screenEntityDefinition.getOriginalSnapshot();

				generateResource(snapshot, entityName, screenResourcesDir, TerminalSnapshotTextRenderer.instance());
				generateResource(snapshot, entityName, screenResourcesDir, TerminalSnapshotImageRenderer.instance());
				generateResource(snapshot, entityName, screenResourcesDir, TerminalSnapshotXmlRenderer.instance());

			} catch (TemplateException e) {
				throw (new GenerationException(e));
			} catch (IOException e) {
				throw (new GenerationException(e));
			}
		}

		generateTest(trailFile, screenDefinitions, sourceDirectory);
	}

	private static void generateTest(File trailFile, Collection<ScreenEntityDefinition> screenDefinitions, File sourceDirectory) {
		TrailJunitGenerator generator = new TrailJunitGenerator();
		File testsDirectory = new File(sourceDirectory, "tests");
		try {
			testsDirectory.mkdir();
			String fileWithoutAnyExtension = FileUtils.fileWithoutAnyExtension(trailFile.getName());
			String testName = StringUtils.capitalize(StringUtil.toClassName(fileWithoutAnyExtension) + "Test");
			FileOutputStream fos = new FileOutputStream(new File(testsDirectory, testName + ".java"));
			generator.generate(screenDefinitions, testName, fos);
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}

	}

	private static void generateJava(ScreenEntityDefinition screenEntityDefinition, File file) throws FileNotFoundException,
			TemplateException, IOException {
		FileOutputStream fos = null;
		try {
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			new ScreenEntityJavaGenerator().generate(screenEntityDefinition, fos);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	private static void generateResource(TerminalSnapshot terminalSnapshot, String entityName, File screenResourcesDir,
			TerminalSnapshotRenderer renderer) {
		FileOutputStream fos = null;
		try {
			File screenTextFile = new File(screenResourcesDir, MessageFormat.format("{0}.{1}", entityName,
					renderer.getFileFormat()));
			fos = new FileOutputStream(screenTextFile);
			renderer.render(terminalSnapshot, fos);
		} catch (FileNotFoundException e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	private synchronized ApplicationContext getApplicationContext(File analyzerContextFile) {
		if (applicationContext == null) {
			if (analyzerContextFile != null && analyzerContextFile.exists()) {
				applicationContext = new FileSystemXmlApplicationContext(analyzerContextFile.getAbsolutePath());
			} else {
				applicationContext = new ClassPathXmlApplicationContext("/openlegacy-default-designtime-context.xml");
			}
		}
		return applicationContext;
	}

	public void generateAspect(File javaFile) {

		OutputStream fos = null;
		try {
			new ScreenPojosAjGenerator().generate(javaFile);
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

	public void generateMvcViewAndContoller(File screenEntitySourceFile, File sourceDirectory, String packageToPath,
			OverrideConfirmer overrideConfirmer) {

	}
}
