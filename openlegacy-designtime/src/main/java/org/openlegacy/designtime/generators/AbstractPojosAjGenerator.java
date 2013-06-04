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
package org.openlegacy.designtime.generators;

import org.apache.commons.lang.CharEncoding;
import org.openlegacy.exceptions.GenerationException;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;

public abstract class AbstractPojosAjGenerator implements PojosAjGenerator {

	public void generate(File javaFile) throws GenerationException {
		try {
			FileInputStream input = new FileInputStream(javaFile);
			CompilationUnit compilationUnit = JavaParser.parse(input, CharEncoding.UTF_8);
			generate(javaFile, compilationUnit);
		} catch (GenerationException e) {
			throw (e);
		} catch (Exception e) {
			throw (new GenerationException(e));
		}
	}

	protected abstract void generate(File javaFile, CompilationUnit compilationUnit);
}
