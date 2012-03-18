package org.openlegacy.designtime.mains;

import org.openlegacy.exceptions.GenerationException;

import java.io.File;
import java.io.IOException;

/**
 * A interface for OpenLegacy design-time common UI actions which should is invoked for any IDE (currently eclipse only)
 * 
 */
public interface DesignTimeExecuter {

	public static final String MOCK_PROVIDER = "openlegacy-impl";
	public static final String ANALYZER_FILE_NAME = "openlegacy-analyzer-context.xml";
	public static final String ANALYZER_DEFAULT_PATH = "src/main/resources/META-INF/spring/" + ANALYZER_FILE_NAME;
	public static final String ASPECT_SUFFIX = "_Aspect.aj";
	public static final String RESOURCES_FOLDER_SUFFIX = "-resources";

	void createProject(String templateName, File baseDir, String projectName, String providerName, String defaultPackage)
			throws IOException;

	void generateScreens(File trailFile, File sourceDirectory, String packageDir, File templatesDir,
			OverrideConfirmer overrideConfirmer, File analyzerContextFile) throws GenerationException;

	void generateAspect(File javaFile);

	void initialize(File analyzerContextFile);

	/**
	 * 
	 * @param projectDir
	 *            The project root directory
	 * @param screenEntitySourceFile
	 *            The screen entity java source which the page should be based on
	 * @param sourceDirectory
	 *            The java source directory where Controller code should be generated
	 * @param packageToPath
	 *            The package folder e.g: "com/test/web"
	 * @param overrideConfirmer
	 *            An interface for UI interaction to allow user to select whether to override an existing file
	 * @throws GenerationException
	 */
	void createWebPage(File projectDir, File screenEntitySourceFile, File sourceDirectory, String packageToPath,
			File templatesDir, OverrideConfirmer overrideConfirmer) throws GenerationException;

	void createCustomTemplatesDir(File projectPath);
}
