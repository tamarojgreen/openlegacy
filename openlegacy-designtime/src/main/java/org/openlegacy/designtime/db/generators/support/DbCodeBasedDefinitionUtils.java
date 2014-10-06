package org.openlegacy.designtime.db.generators.support;

import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.designtime.db.generators.DbPojoCodeModel;
import org.openlegacy.designtime.utils.JavaParserUtil;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.io.File;
import java.util.List;

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

}
