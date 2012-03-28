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

	public void createProject(ProjectCreationRequest projectCreationRequest) throws IOException;

	void generateScreens(File trailFile, File sourceDirectory, String packageDir, File templatesDir,
			OverrideConfirmer overrideConfirmer, File analyzerContextFile) throws GenerationException;

	void generateAspect(File javaFile);

	void initialize(File analyzerContextFile);

	void createWebPage(GeneratePageRequest generatePageRequest) throws GenerationException;

	void createCustomTemplatesDir(File projectPath);
}
