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
package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.generators.AbstractPojosAjGenerator;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;
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
 * A generator which generate for screen pojos annotation with @RpcEntity, @RpcPart, (a.k.a "main rpc annotation") Work closely
 * with RPcPojoCodeModelImpl and freemarker template engine Performs the following operations:<br/>
 * <ul>
 * <li>1.Generate an aspect file (.aj) for each pojo</li>
 * <li>2. Add getters and setters from the aspect for class member if not exists</li>
 * <li>4. For @RpcEntity Add implementation for interface org.openlegacy.rpc.RpcEntity</li>
 * </ul>
 * 
 */
@Component
public class RpcPojosAjGenerator extends AbstractPojosAjGenerator {

	private final static Log logger = LogFactory.getLog(RpcPojosAjGenerator.class);

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
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				for (AnnotationExpr annotationExpr : annotations) {
					RpcPojoCodeModel screenEntityCodeModel = null;
					if (JavaParserUtil.hasAnnotation(annotationExpr, RpcAnnotationConstants.RPC_ENTITY_ANNOTATION)) {
						screenEntityCodeModel = generateEntity(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
								baos);
					}
					// TODO RPC parts
					if (screenEntityCodeModel != null && screenEntityCodeModel.isRelevant()) {
						GenerateUtil.writeToFile(javaFile, baos, screenEntityCodeModel, parentClassName);
					}
				}
			} catch (Exception e) {
				throw (new GenerationException(e));
			}
		}

	}

	public RpcPojoCodeModel generateEntity(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration typeDeclaration,
			OutputStream out) throws IOException, TemplateException, ParseException {
		return generate(out, compilationUnit, typeDeclaration, "Rpc_Aspect.aj.template", null);
	}

	public RpcPojoCodeModel generate(OutputStream out, CompilationUnit compilationUnit,
			ClassOrInterfaceDeclaration typeDeclaration, String templateFileName, String parentClass) throws IOException,
			TemplateException, ParseException {

		RpcPojoCodeModel rpcEntityCodeModel = new DefaultRpcPojoCodeModel(compilationUnit, typeDeclaration,
				typeDeclaration.getName(), parentClass);

		if (!rpcEntityCodeModel.isRelevant()) {
			return rpcEntityCodeModel;
		}

		generateUtil.generate(rpcEntityCodeModel, out, templateFileName);

		return rpcEntityCodeModel;
	}

}
