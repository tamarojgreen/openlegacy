package org.openlegacy.designtime.terminal.generators;

import org.openlegacy.designtime.mains.OverrideConfirmer;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.File;
import java.io.OutputStream;

public interface ScreenEntityWebGenerator {

	void generateAll(File projectDir, ScreenEntityDefinition screenEntityDefinition, File sourceDirectory,
			String packageDirectoryName, OverrideConfirmer overrideConfirmer) throws GenerationException;

	void generatePage(PageDefinition pageDefinition, OutputStream output) throws GenerationException;

	public void generateController(PageDefinition pageDefinition, OutputStream output) throws GenerationException;

	public void generateControllerAspect(PageDefinition pageDefinition, OutputStream output) throws GenerationException;
}
