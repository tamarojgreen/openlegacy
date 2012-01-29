package org.openlegacy.designtime.mains;

import org.openlegacy.exceptions.UnableToGenerateSnapshotException;

import java.io.File;
import java.io.IOException;

public interface DesignTimeExecuter {

	public static final String MOCK_PROVIDER = "openlegacy-impl";

	void createProject(String templateName, File baseDir, String projectName, String providerName, String defaultPackage)
			throws IOException;

	void generateScreens(File trailFile, File sourceDirectory, String packageDir, OverrideConfirmer overrideConfirmer)
			throws UnableToGenerateSnapshotException;

	void generateAspect(File javaFile);

	void initialize();
}
