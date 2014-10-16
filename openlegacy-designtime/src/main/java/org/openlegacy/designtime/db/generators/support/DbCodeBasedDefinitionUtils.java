package org.openlegacy.designtime.db.generators.support;

import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;
import org.openlegacy.designtime.db.generators.DbPojoCodeModel;
import org.openlegacy.designtime.db.generators.support.DefaultDbPojoCodeModel.ColumnField;
import org.openlegacy.designtime.utils.JavaParserUtil;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Ivan Bort
 * 
 */
public class DbCodeBasedDefinitionUtils {

	public static DbEntityDefinition getEntityDefinition(CompilationUnit compilationUnit, File packageDir) {
		CodeBasedDbEntityDefinition entityDefinition = null;

		List<TypeDeclaration> types = compilationUnit.getTypes();
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
				DbPojoCodeModel dbPojoCodeModel = null;
				if (JavaParserUtil.hasAnnotation(annotationExpr, DbAnnotationConstants.DB_ENTITY_ANNOTATION)
						|| JavaParserUtil.hasAnnotation(annotationExpr, DbAnnotationConstants.DB_ENTITY_SUPER_CLASS_ANNOTATION)) {
					dbPojoCodeModel = new DefaultDbPojoCodeModel(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
							typeDeclaration.getName(), null);
					entityDefinition = new CodeBasedDbEntityDefinition(dbPojoCodeModel, packageDir);
				}
			}
		}
		return entityDefinition;
	}

	public static Map<String, DbFieldDefinition> getColumnFieldsFromCodeModel(DbPojoCodeModel codeModel) {
		Collection<ColumnField> columnFields = codeModel.getColumnFields().values();

		Map<String, DbFieldDefinition> fieldDefinitions = new TreeMap<String, DbFieldDefinition>();
		for (ColumnField javaColumnField : columnFields) {
			SimpleDbColumnFieldDefinition definition = new SimpleDbColumnFieldDefinition(javaColumnField.getFieldName(),
					javaColumnField.getFieldTypeArgs());
			definition.setJavaTypeName(javaColumnField.getFieldType());
			definition.setNameAttr(javaColumnField.getName());
			definition.setUnique(javaColumnField.isUnique());
			definition.setNullable(javaColumnField.isNullable());
			definition.setInsertable(javaColumnField.isInsertable());
			definition.setUpdatable(javaColumnField.isUpdatable());
			definition.setColumnDefinition(javaColumnField.getColumnDefinition());
			definition.setTable(javaColumnField.getTable());
			definition.setLength(javaColumnField.getLength());
			definition.setPrecision(javaColumnField.getPrecision());
			definition.setScale(javaColumnField.getScale());
			definition.setOneToManyDefinition(javaColumnField.getOneToManyDefinition());
			definition.setKey(javaColumnField.isKey());

			fieldDefinitions.put(javaColumnField.getFieldName(), definition);
		}

		return fieldDefinitions;
	}

}
