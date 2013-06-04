package org.openlegacy.designtime.generators;

import org.openlegacy.exceptions.GenerationException;

import java.io.File;

public interface PojosAjGenerator {

	void generate(File javaFile) throws GenerationException;
}
