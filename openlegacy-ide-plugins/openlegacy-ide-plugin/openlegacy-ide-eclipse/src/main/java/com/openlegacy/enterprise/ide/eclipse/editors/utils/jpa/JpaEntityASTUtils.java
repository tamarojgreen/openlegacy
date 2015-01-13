package com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.ASTUtils;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.openlegacy.annotations.screen.Action;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;
import org.openlegacy.designtime.generators.AnnotationConstants;

import javax.persistence.UniqueConstraint;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntityASTUtils extends ASTUtils {

	public static final JpaEntityASTUtils INSTANCE = new JpaEntityASTUtils();

	private JpaEntityASTUtils() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.utils.ASTUtils#getAnnotationForArrayLiteral(org.eclipse.jdt.core.dom.AST,
	 * org.eclipse.jdt.core.dom.CompilationUnit, org.eclipse.jdt.core.dom.rewrite.ASTRewrite, java.lang.Object)
	 */
	@Override
	protected <T> NormalAnnotation getAnnotationForArrayLiteral(AST ast, CompilationUnit cu, ASTRewrite rewriter, T item) {
		if (UniqueConstraintDefinition.class.isInstance(item)) {
			return getUniqueConstraintAnnotation(ast, (UniqueConstraintDefinition)item);
		} else if (item.getClass().isAssignableFrom(ActionModel.class)) {
			return getActionAnnotation(ast, (ActionModel)item);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private NormalAnnotation getUniqueConstraintAnnotation(AST ast, UniqueConstraintDefinition definition) {
		NormalAnnotation annotation = null;
		if (definition.getName() == null || definition.getName().trim().equals("")) {
			return annotation;
		}
		annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName(UniqueConstraint.class.getSimpleName()));
		annotation.values().add(createStringArrayPair(ast, DbAnnotationConstants.COLUMN_NAMES, definition.getColumnNames()));
		annotation.values().add(createStringPair(ast, DbAnnotationConstants.NAME, definition.getName()));
		return annotation;
	}

	@SuppressWarnings("unchecked")
	private NormalAnnotation getActionAnnotation(AST ast, ActionModel model) {
		NormalAnnotation annotation = null;
		if (model.getActionName() == null || model.getActionName().isEmpty()) {
			return annotation;
		}
		annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName(Action.class.getSimpleName()));
		annotation.values().add(createTypePair(ast, AnnotationConstants.ACTION, model.getAction().getClass()));
		if (!model.getDisplayName().isEmpty()) {
			annotation.values().add(createStringPair(ast, AnnotationConstants.DISPLAY_NAME, model.getDisplayName()));
		}
		if (!model.getAlias().isEmpty()) {
			annotation.values().add(createStringPair(ast, AnnotationConstants.ALIAS, model.getAlias()));
		}
		if (!model.isGlobal()) {
			annotation.values().add(createBooleanPair(ast, AnnotationConstants.GLOBAL, model.isGlobal()));
		}
		if (model.getTargetEntity() != null && !model.getTargetEntity().equals(model.getDefaultTargetEntity())) {
			annotation.values().add(createTypePair(ast, AnnotationConstants.TARGET_ENTITY, model.getTargetEntity()));
		}
		return annotation;
	}

}
