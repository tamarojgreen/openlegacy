/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.db.generators;

import freemarker.template.TemplateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;
import org.openlegacy.designtime.db.generators.support.DefaultDbPojoCodeModel;
import org.openlegacy.designtime.generators.AbstractPojosAjGenerator;
import org.openlegacy.designtime.generators.GenerateUtil;
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
 * A generator which generate for DB pojos annotation with @Entity Work closely with DbPojoCodeModelImpl and freemarker template
 * engine Performs the following operations:<br/>
 * <ul>
 * <li>1.Generate an aspect file (.aj) for each pojo</li>
 * <li>2. Add getters and setters from the aspect for class member if not exists</li>
 * </ul>
 * 
 */
@Component
public class DbPojosAjGenerator extends AbstractPojosAjGenerator {

	private final static Log logger = LogFactory.getLog(DbPojosAjGenerator.class);

	@Inject
	private GenerateUtil generateUtil;

	@Override
	public boolean generate(File javaFile, CompilationUnit compilationUnit) throws GenerationException {

		List<TypeDeclaration> types = compilationUnit.getTypes();

		if (types == null || types.size() == 0) {
			logger.warn(MessageFormat.format("No types detected for {0}. skipping file", javaFile.getName()));
			return false;
		}

		String parentClassName = types.get(0).getName();

		List<BodyDeclaration> members = types.get(0).getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			// look for inner classes
			if (bodyDeclaration instanceof ClassOrInterfaceDeclaration) {
				types.add((TypeDeclaration)bodyDeclaration);
			}
		}

		boolean aspectGenerated = false;
		for (TypeDeclaration typeDeclaration : types) {
			List<AnnotationExpr> annotations = typeDeclaration.getAnnotations();
			if (annotations == null) {
				continue;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (AnnotationExpr annotationExpr : annotations) {
				DbPojoCodeModel dbEntityCodeModel = null;
				try {
					if (JavaParserUtil.hasAnnotation(annotationExpr, DbAnnotationConstants.DB_JPA_ENTITY_ANNOTATION)
							|| JavaParserUtil.hasAnnotation(annotationExpr, DbAnnotationConstants.DB_ENTITY_ANNOTATION)
							|| JavaParserUtil.hasAnnotation(annotationExpr,
									DbAnnotationConstants.DB_ENTITY_SUPER_CLASS_ANNOTATION)) {
						dbEntityCodeModel = generateEntity(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration, baos);
					}
					if (dbEntityCodeModel != null && dbEntityCodeModel.isRelevant()) {
						boolean generated = GenerateUtil.writeAspectToFile(javaFile, baos, dbEntityCodeModel, parentClassName);
						if (generated) {
							aspectGenerated = true;
						}
					}

				} catch (Exception e) {
					throw (new GenerationException(e));
				}
			}
		}
		return aspectGenerated;

	}

	public DbPojoCodeModel generateEntity(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration typeDeclaration,
			OutputStream out) throws IOException, TemplateException, ParseException {
		return generate(out, compilationUnit, typeDeclaration, "Db_Aspect.aj.template", null);
	}

	public DbPojoCodeModel generate(OutputStream out, CompilationUnit compilationUnit,
			ClassOrInterfaceDeclaration typeDeclaration, String templateFileName, String parentClass) throws IOException,
			TemplateException, ParseException {

		DbPojoCodeModel dbEntityCodeModel = new DefaultDbPojoCodeModel(compilationUnit, typeDeclaration,
				typeDeclaration.getName(), parentClass);

		if (!dbEntityCodeModel.isRelevant()) {
			return dbEntityCodeModel;
		}

		generateUtil.generate(dbEntityCodeModel, out, templateFileName);

		return dbEntityCodeModel;
	}
}
