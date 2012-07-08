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
	public static final String CUSTOM_DESIGNTIME_CONTEXT_FILE_NAME = "openlegacy-designtime-context.xml";
	public static final String CUSTOM_DESIGNTIME_CONTEXT_RELATIVE_PATH = "src/main/resources/META-INF/spring/"
			+ CUSTOM_DESIGNTIME_CONTEXT_FILE_NAME;
	public static final String DEFAULT_DESIGNTIME_CONTEXT_FILE_NAME = "/openlegacy-default-designtime-context.xml";
	public static final String ASPECT_SUFFIX = "_Aspect.aj";
	public static final String RESOURCES_FOLDER_SUFFIX = "-resources";

	public static final int DEFAULT_PORT = 23;
	public static final String DEFAULT_CODE_PAGE = "037";

	public void createProject(ProjectCreationRequest projectCreationRequest) throws IOException;

	void generateScreens(GenerateScreenRequest generateScreenRequest) throws GenerationException;

	void generateAspect(File javaFile);

	void initialize(File analyzerContextFile);

	void generateWebPage(GeneratePageRequest generatePageRequest) throws GenerationException;

	void copyCodeGenerationTemplates(File projectPath);

	String getPerferences(File projectPath, String key);

	void savePerference(File projectPath, String key, String value);

	public void copyDesigntimeContext(File projectPath);
}
