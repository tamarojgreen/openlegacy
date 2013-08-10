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
package org.openlegacy.designtime.rpc.generators.support;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Action;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Field;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;
import org.openlegacy.utils.StringUtil;
import org.springframework.util.Assert;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RpcCodeBasedDefinitionUtils {

	public static Map<String, RpcFieldDefinition> getFieldsFromCodeModel(RpcPojoCodeModel codeModel, String containerPrefix) {

		// add prefix for field parts
		containerPrefix = !StringUtil.isEmpty(containerPrefix) ? containerPrefix + "." : "";

		Collection<Field> fields = codeModel.getFields();

		Map<String, RpcFieldDefinition> fieldDefinitions = new LinkedHashMap<String, RpcFieldDefinition>();
		for (Field javaFieldModel : fields) {
			if (!javaFieldModel.isRpcField()) {
				continue;
			}
			SimpleRpcFieldDefinition fieldDefinition = new SimpleRpcFieldDefinition(containerPrefix + javaFieldModel.getName(),
					null);
			fieldDefinition.setEditable(javaFieldModel.isEditable());
			fieldDefinition.setDisplayName(StringUtil.stripQuotes(javaFieldModel.getDisplayName()));
			fieldDefinition.setFieldTypeDefinition(javaFieldModel.getFieldTypeDefiniton());
			fieldDefinition.setKey(javaFieldModel.isKey());
			fieldDefinition.setHelpText(javaFieldModel.getHelpText());

			fieldDefinition.setFieldTypeName(javaFieldModel.getFieldTypeName());
			fieldDefinition.setPassword(javaFieldModel.isPassword());
			fieldDefinition.setSampleValue(javaFieldModel.getSampleValue());
			fieldDefinition.setJavaTypeName(javaFieldModel.getType());
			fieldDefinition.setHelpText(javaFieldModel.getHelpText());
			fieldDefinition.setRightToLeft(javaFieldModel.isRightToLeft());

			fieldDefinitions.put(javaFieldModel.getName(), fieldDefinition);
		}
		return fieldDefinitions;

	}

	/**
	 * 
	 * @param compilationUnit
	 * @param packageDir
	 *            the package source folder. Used for finding related compilation units. May be null in test mode only
	 * @return
	 */
	public static RpcEntityDefinition getEntityDefinition(CompilationUnit compilationUnit, File packageDir) {
		List<TypeDeclaration> types = compilationUnit.getTypes();
		CodeBasedRpcEntityDefinition entityDefinition = null;

		TypeDeclaration type = compilationUnit.getTypes().get(0);
		List<BodyDeclaration> members = type.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			// look for inner classes
			if (bodyDeclaration instanceof ClassOrInterfaceDeclaration) {
				types.add((TypeDeclaration)bodyDeclaration);
			}
		}

		Map<String, CodeBasedRpcPartDefinition> parts = new HashMap<String, CodeBasedRpcPartDefinition>();

		for (TypeDeclaration typeDeclaration : types) {
			List<AnnotationExpr> annotations = typeDeclaration.getAnnotations();
			if (annotations == null) {
				continue;
			}
			for (AnnotationExpr annotationExpr : annotations) {
				RpcPojoCodeModel entityCodeModel = null;
				if (JavaParserUtil.hasAnnotation(annotationExpr, RpcAnnotationConstants.RPC_ENTITY_ANNOTATION)
						|| JavaParserUtil.hasAnnotation(annotationExpr, RpcAnnotationConstants.RPC_ENTITY_SUPER_CLASS_ANNOTATION)) {
					entityCodeModel = new DefaultRpcPojoCodeModel(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
							typeDeclaration.getName(), null);
					entityDefinition = new CodeBasedRpcEntityDefinition(entityCodeModel, packageDir);
				}
				if (JavaParserUtil.hasAnnotation(annotationExpr, RpcAnnotationConstants.RPC_PART_ANNOTATION)) {
					entityCodeModel = new DefaultRpcPojoCodeModel(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
							typeDeclaration.getName(), null);
					CodeBasedRpcPartDefinition partDefinition = new CodeBasedRpcPartDefinition(entityCodeModel);
					Assert.notNull(entityDefinition, "Compliation unit doesn't contain @RpcEntity. Unable to build");
					parts.put(partDefinition.getPartName(), partDefinition);
				}
			}
		}
		// attach part definition by entity fields
		Collection<Field> entityFields = entityDefinition.getCodeModel().getFields();
		for (Field field : entityFields) {
			if (!field.isPrimitiveType()) {
				entityDefinition.getPartsDefinitions().put(field.getName(), parts.get(StringUtil.toClassName(field.getName())));
			}
		}
		Collection<CodeBasedRpcPartDefinition> partsList = parts.values();

		// attach inner part definition by entity fields
		for (CodeBasedRpcPartDefinition rpcPartEntityDefinition : partsList) {
			Collection<Field> fields = rpcPartEntityDefinition.getCodeModel().getFields();
			for (Field field : fields) {
				if (!field.isPrimitiveType()) {
					CodeBasedRpcPartDefinition part = parts.get(StringUtil.toClassName(field.getName()));
					if (part != null) {
						rpcPartEntityDefinition.getInnerPartsDefinitions().put(field.getName(), part);
					}
				}
			}
		}
		return entityDefinition;

	}

	public static List<ActionDefinition> getActionsFromCodeModel(RpcPojoCodeModel codeModel) {
		List<Action> actions = codeModel.getActions();
		List<ActionDefinition> actionDefinitions = new ArrayList<ActionDefinition>();
		for (Action action : actions) {
			String actionName = StringUtil.toClassName(action.getActionName());
			SimpleTerminalActionDefinition actionDefinition = new SimpleTerminalActionDefinition(actionName,
					StringUtil.stripQuotes(action.getDisplayName()));
			if (action.getAlias() != null) {
				actionDefinition.setAlias(StringUtil.stripQuotes(action.getAlias()));
			}
			actionDefinitions.add(actionDefinition);
		}

		return actionDefinitions;
	}

}
