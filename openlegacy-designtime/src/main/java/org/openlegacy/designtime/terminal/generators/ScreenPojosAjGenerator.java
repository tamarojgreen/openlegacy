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
package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.generators.AbstractPojosAjGenerator;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.exceptions.GenerationException;
import org.springframework.stereotype.Component;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

/**
 * A generator which generate for screen pojos annotation with @ScreenEntity, @ScreenPart, @ScreenTable (a.k.a
 * "main screen annotation") Work closely with ScreenPojoCodeModelImpl and freemarker template engine Performs the following
 * operations:<br/>
 * <ul>
 * <li>1.Generate an aspect file (.aj) for each pojo</li>
 * <li>2. Add getters and setters from the aspect for class member if not exists</li>
 * <li>3. Add terminal field for each member, if the class main screen annotation with supportedTerminalData=true attribute</li>
 * <li>4. For @ScreenEntity Add implementation for interface org.openlegacy.terminal.ScreenEntity, and add focusField member</li>
 * </ul>
 * 
 */
@Component
public class ScreenPojosAjGenerator extends AbstractPojosAjGenerator {

	private final static Log logger = LogFactory.getLog(ScreenPojosAjGenerator.class);

	@Inject
	private GenerateUtil generateUtil;

	@Override
	public void generate(File javaFile, CompilationUnit compilationUnit) throws GenerationException {

		List<TypeDeclaration> types = compilationUnit.getTypes();

		if (types == null || types.size() == 0) {
			logger.warn(MessageFormat.format("No types detected for {0}. skipping file", javaFile.getName()));
			return;
		}

		String parentClassName = types.get(0).getName();

		List<BodyDeclaration> members = types.get(0).getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			// look for inner classes
			if (bodyDeclaration instanceof ClassOrInterfaceDeclaration) {
				types.add((TypeDeclaration)bodyDeclaration);
			}
		}

		for (TypeDeclaration typeDeclaration : types) {
			List<AnnotationExpr> annotations = typeDeclaration.getAnnotations();
			if (annotations == null) {
				continue;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (AnnotationExpr annotationExpr : annotations) {
				ScreenPojoCodeModel screenEntityCodeModel = null;
				try {
					if (JavaParserUtil.hasAnnotation(annotationExpr, ScreenAnnotationConstants.SCREEN_ENTITY_ANNOTATION)
							|| JavaParserUtil.hasAnnotation(annotationExpr,
									ScreenAnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)) {
						screenEntityCodeModel = generateEntity(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
								baos);
					}
					if (JavaParserUtil.hasAnnotation(annotationExpr, ScreenAnnotationConstants.SCREEN_PART_ANNOTATION)) {
						screenEntityCodeModel = generateScreenPart(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
								baos, parentClassName);
					}
					if (JavaParserUtil.hasAnnotation(annotationExpr, ScreenAnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
						screenEntityCodeModel = generateScreenTable(compilationUnit,
								(ClassOrInterfaceDeclaration)typeDeclaration, baos, parentClassName);
					}
					if (screenEntityCodeModel != null && screenEntityCodeModel.isRelevant()) {
						GenerateUtil.writeToFile(javaFile, baos, screenEntityCodeModel, parentClassName);
					}

				} catch (Exception e) {
					throw (new GenerationException(e));
				}
			}
		}

	}

	public ScreenPojoCodeModel generateEntity(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration typeDeclaration,
			OutputStream out) throws IOException, TemplateException, ParseException {
		return generate(out, compilationUnit, typeDeclaration, "Screen_Aspect.aj.template", null);
	}

	public ScreenPojoCodeModel generateScreenPart(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration typeDeclaration,
			OutputStream out, String parentClass) throws IOException, TemplateException, ParseException {
		return generate(out, compilationUnit, typeDeclaration, "ScreenPart_Aspect.aj.template", parentClass);
	}

	public ScreenPojoCodeModel generateScreenTable(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration typeDeclaration,
			OutputStream out, String parentClass) throws IOException, TemplateException, ParseException {
		return generate(out, compilationUnit, typeDeclaration, "ScreenTable_Aspect.aj.template", parentClass);
	}

	public ScreenPojoCodeModel generate(OutputStream out, CompilationUnit compilationUnit,
			ClassOrInterfaceDeclaration typeDeclaration, String templateFileName, String parentClass) throws IOException,
			TemplateException, ParseException {

		ScreenPojoCodeModel screenEntityCodeModel = new DefaultScreenPojoCodeModel(compilationUnit, typeDeclaration,
				typeDeclaration.getName(), parentClass);

		if (!screenEntityCodeModel.isRelevant()) {
			return screenEntityCodeModel;
		}

		generateUtil.generate(screenEntityCodeModel, out, templateFileName);

		return screenEntityCodeModel;
	}
}
