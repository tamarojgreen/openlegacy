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
package org.openlegacy.designtime.rpc.generators.support;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Action;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Field;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.exceptions.EntityNotAccessibleException;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcActionDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.utils.StringUtil;
import org.springframework.util.Assert;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RpcCodeBasedDefinitionUtils {

	private final static Log logger = LogFactory.getLog(RpcCodeBasedDefinitionUtils.class);

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
			fieldDefinition.setDisplayName(javaFieldModel.getDisplayName());
			fieldDefinition.setFieldTypeDefinition(javaFieldModel.getFieldTypeDefiniton());
			fieldDefinition.setKey(javaFieldModel.isKey());
			fieldDefinition.setHelpText(javaFieldModel.getHelpText());

			fieldDefinition.setFieldTypeName(javaFieldModel.getFieldTypeName());
			fieldDefinition.setPassword(javaFieldModel.isPassword());
			fieldDefinition.setSampleValue(javaFieldModel.getSampleValue());
			fieldDefinition.setJavaTypeName(javaFieldModel.getType());
			fieldDefinition.setHelpText(javaFieldModel.getHelpText());
			fieldDefinition.setRightToLeft(javaFieldModel.isRightToLeft());
			fieldDefinition.setRuntimeName(javaFieldModel.getRuntimeName());
			fieldDefinition.setCount(javaFieldModel.getCount());
			fieldDefinition.setDirection(javaFieldModel.getDirection());

			fieldDefinition.setOriginalName(javaFieldModel.getOriginalName());
			fieldDefinition.setDirection(javaFieldModel.getDirection());
			fieldDefinition.setDefaultValue(javaFieldModel.getDefaultValue());
			fieldDefinition.setLength(javaFieldModel.getLength());
			fieldDefinition.setExpression(javaFieldModel.getExpression());
			fieldDefinition.setOrder(javaFieldModel.getOrder());

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
							typeDeclaration.getName(), null, (ClassOrInterfaceDeclaration)type);
					entityDefinition = new CodeBasedRpcEntityDefinition(entityCodeModel, packageDir);
				}
				if (JavaParserUtil.hasAnnotation(annotationExpr, RpcAnnotationConstants.RPC_PART_ANNOTATION)) {
					entityCodeModel = new DefaultRpcPojoCodeModel(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
							typeDeclaration.getName(), null, (ClassOrInterfaceDeclaration)type);
					CodeBasedRpcPartDefinition partDefinition = new CodeBasedRpcPartDefinition(entityCodeModel, packageDir);
					Assert.notNull(entityDefinition, "Compliation unit doesn't contain @RpcEntity. Unable to build");
					parts.put(partDefinition.getPartName(), partDefinition);
				}
			}
		}
		// attach part definition by entity fields
		Collection<Field> entityFields = entityDefinition.getCodeModel().getFields();
		for (Field field : entityFields) {
			if (!field.isPrimitiveType()) {
				CodeBasedRpcPartDefinition part = parts.get(StringUtil.toClassName(field.getName()));
				if (part != null) {
					// note: part can be null if field type is enum
					// TODO need to find a way to keep in field level and not part level, in case part is shared
					part.setCount(field.getCount());
					part.setRuntimeName(field.getRuntimeName());
					entityDefinition.getPartsDefinitions().put(field.getName(), part);
				}
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
						// TODO need to find a way to keep in field level and not part level, in case part is shared
						part.setCount(field.getCount());
						part.setRuntimeName(field.getRuntimeName());
						rpcPartEntityDefinition.getInnerPartsDefinitions().put(field.getName(), part);
					}
				}
			}
		}
		return entityDefinition;

	}

	public static List<ActionDefinition> getActionsFromCodeModel(RpcPojoCodeModel codeModel, File packageDir) {
		List<Action> actions = codeModel.getActions();
		List<ActionDefinition> actionDefinitions = new ArrayList<ActionDefinition>();
		for (Action action : actions) {
			String actionName = StringUtil.toClassName(action.getActionName());
			SimpleRpcActionDefinition actionDefinition = new SimpleRpcActionDefinition(actionName, action.getDisplayName());
			actionDefinition.setProgramPath(action.getPath());
			actionDefinition.setGlobal(action.isGlobal());
			if (action.getAlias() != null) {
				actionDefinition.setAlias(action.getAlias());
			}
			if (action.getTargetEntityName() != null) {
				RpcEntityDefinition targetDefinition = getEntityDefinition(action.getTargetEntityName(), packageDir);
				actionDefinition.setTargetEntityDefinition(targetDefinition);
			}

			actionDefinitions.add(actionDefinition);
		}

		return actionDefinitions;
	}

	private static RpcEntityDefinition getEntityDefinition(String entityName, File packageDir) {
		File entityFile = new File(packageDir, entityName + ".java");
		if (!entityFile.exists()) {
			logger.debug(MessageFormat.format("Source file for entity {0} is not defined. Unable to find file {1}",
					entityFile.getName(), entityFile.getAbsoluteFile()));
		}
		CompilationUnit compilationUnit = null;
		try {
			compilationUnit = JavaParser.parse(entityFile, CharEncoding.UTF_8);
		} catch (ParseException e) {
			logger.warn("Failed parsing java file:" + e.getMessage());
		} catch (IOException e) {
			throw (new EntityNotAccessibleException(e));
		}
		RpcEntityDefinition rpcDefinition = getEntityDefinition(compilationUnit, packageDir);
		return rpcDefinition;
	}
}
