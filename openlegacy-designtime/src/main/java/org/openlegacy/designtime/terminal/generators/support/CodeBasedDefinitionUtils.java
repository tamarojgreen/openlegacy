package org.openlegacy.designtime.terminal.generators.support;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.definitions.support.SimpleAutoCompleteFieldTypeDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenTableDefinition;
import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Action;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Field;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.StringUtil;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CodeBasedDefinitionUtils {

	public static Map<String, ScreenFieldDefinition> getFieldsFromCodeModel(ScreenPojoCodeModel codeModel, String containerPrefix) {

		// add prefix for field parts
		containerPrefix = !StringUtil.isEmpty(containerPrefix) ? containerPrefix + "." : "";

		Collection<Field> fields = codeModel.getFields();
		Map<String, ScreenFieldDefinition> fieldDefinitions = new TreeMap<String, ScreenFieldDefinition>();
		for (Field field : fields) {
			if (!field.isScreenField()) {
				continue;
			}
			SimpleScreenFieldDefinition fieldDefinition = new SimpleScreenFieldDefinition(containerPrefix + field.getName(), null);
			fieldDefinition.setPosition(new SimpleTerminalPosition(field.getRow(), field.getColumn()));
			fieldDefinition.setEditable(field.isEditable());
			if (field.getLabelColumn() != null) {
				fieldDefinition.setLabelPosition(new SimpleTerminalPosition(field.getRow(), field.getLabelColumn()));
			}
			fieldDefinition.setDisplayName(StringUtil.stripQuotes(field.getDisplayName()));
			if (field.getEndColumn() != null) {
				fieldDefinition.setLength(field.getEndColumn() - field.getColumn() + 1);
			}
			if (field.isHasValues()) {
				SimpleAutoCompleteFieldTypeDefinition fieldTypeDefinition = new SimpleAutoCompleteFieldTypeDefinition();
				fieldTypeDefinition.setSourceScreenEntityClassName(field.getSourceScreenClassName());
				fieldDefinition.setFieldTypeDefinition(fieldTypeDefinition);
			}
			fieldDefinitions.put(field.getName(), fieldDefinition);
		}
		return fieldDefinitions;

	}

	public static ScreenEntityDefinition getEntityDefinition(CompilationUnit compilationUnit) {
		List<TypeDeclaration> types = compilationUnit.getTypes();
		CodeBasedScreenEntityDefinition screenDefinition = null;

		TypeDeclaration type = compilationUnit.getTypes().get(0);
		List<BodyDeclaration> members = type.getMembers();
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
			for (AnnotationExpr annotationExpr : annotations) {
				ScreenPojoCodeModel screenEntityCodeModel = null;
				if (JavaParserUtil.hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_ENTITY_ANNOTATION)
						|| JavaParserUtil.hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)) {
					screenEntityCodeModel = new DefaultScreenPojoCodeModel(compilationUnit,
							(ClassOrInterfaceDeclaration)typeDeclaration, typeDeclaration.getName(), null);
					screenDefinition = new CodeBasedScreenEntityDefinition(screenEntityCodeModel);
				}
				if (JavaParserUtil.hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_PART_ANNOTATION)) {
					screenEntityCodeModel = new DefaultScreenPojoCodeModel(compilationUnit,
							(ClassOrInterfaceDeclaration)typeDeclaration, typeDeclaration.getName(), null);
					CodeBasedScreenPartDefinition partDefinition = new CodeBasedScreenPartDefinition(screenEntityCodeModel);
					screenDefinition.getPartsDefinitions().put(partDefinition.getPartName(), partDefinition);
				}
				if (JavaParserUtil.hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
					screenEntityCodeModel = new DefaultScreenPojoCodeModel(compilationUnit,
							(ClassOrInterfaceDeclaration)typeDeclaration, typeDeclaration.getName(), null);
					CodeBasedScreenTableDefinition tableDefinition = new CodeBasedScreenTableDefinition(screenEntityCodeModel);
					screenDefinition.getTableDefinitions().put(tableDefinition.getTableEntityName(), tableDefinition);
				}
			}
		}

		return screenDefinition;

	}

	public static List<ActionDefinition> getActionsFromCodeModel(ScreenPojoCodeModel codeModel) {
		List<Action> actions = codeModel.getActions();
		List<ActionDefinition> actionDefinitions = new ArrayList<ActionDefinition>();
		for (Action action : actions) {
			String actionName = StringUtil.toClassName(action.getActionName());
			SimpleActionDefinition actionDefinition = new SimpleActionDefinition(actionName,
					StringUtil.stripQuotes(action.getDisplayName()));
			actionDefinition.setAlias(StringUtil.stripQuotes(action.getAlias()));
			actionDefinitions.add(actionDefinition);
		}

		return actionDefinitions;
	}
}
