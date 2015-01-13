package com.openlegacy.enterprise.ide.eclipse.editors.utils.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.IdentifierModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.ASTUtils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenEntityASTUtils extends ASTUtils {

	public static ScreenEntityASTUtils INSTANCE = new ScreenEntityASTUtils();

	private ScreenEntityASTUtils() {}

	// ****************************** literals ********************************

	@Override
	protected <T> NormalAnnotation getAnnotationForArrayLiteral(AST ast, CompilationUnit cu, ASTRewrite rewriter, T item) {

		if (FieldAssignDefinition.class.isInstance(item)) {
			return getAssignFieldAnnotation(ast, (FieldAssignDefinition)item);
		} else if (item.getClass().isAssignableFrom(IdentifierModel.class)) {
			return getIdentifierAnnotation(ast, (IdentifierModel)item);
		} else if (item.getClass().isAssignableFrom(ActionModel.class)) {
			return getActionAnnotation(ast, cu, rewriter, (ActionModel)item);
		} else if (item.getClass().isAssignableFrom(TableActionModel.class)) {
			return getTableActionAnnotation(ast, cu, rewriter, (TableActionModel)item);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private NormalAnnotation getIdentifierAnnotation(AST ast, IdentifierModel model) {
		NormalAnnotation annotation = null;
		if (model.getRow() == 0 || model.getColumn() == 0 || model.getText().trim().equals("")) {
			return annotation;
		}
		annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName(Identifier.class.getSimpleName()));
		annotation.values().add(createNumberPair(ast, ScreenAnnotationConstants.ROW, model.getRow()));
		annotation.values().add(createNumberPair(ast, ScreenAnnotationConstants.COLUMN, model.getColumn()));
		annotation.values().add(createStringPair(ast, ScreenAnnotationConstants.VALUE, model.getText()));
		if (model.getAttribute() != null && !model.getAttribute().equals(model.getDefaultAttribute())) {
			annotation.values().add(createEnumPair(ast, ScreenAnnotationConstants.ATTRIBUTE, model.getAttribute()));
		}
		return annotation;
	}

	@SuppressWarnings("unchecked")
	private NormalAnnotation getAssignFieldAnnotation(AST ast, FieldAssignDefinition definition) {
		NormalAnnotation annotation = null;
		if (definition.getName() == null || definition.getName().trim().equals("")) {
			return annotation;
		}
		annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName(AssignedField.class.getSimpleName()));
		annotation.values().add(createStringPair(ast, ScreenAnnotationConstants.FIELD, definition.getName()));
		annotation.values().add(createStringPair(ast, ScreenAnnotationConstants.VALUE, definition.getValue()));
		return annotation;
	}

	@SuppressWarnings("unchecked")
	private NormalAnnotation getActionAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ActionModel model) {
		NormalAnnotation annotation = null;
		if (model.getActionName() == null || model.getActionName().isEmpty()) {
			return annotation;
		}
		annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName(Action.class.getSimpleName()));
		annotation.values().add(createTypePair(ast, AnnotationConstants.ACTION, model.getAction().getClass()));
		if (!StringUtils.isEmpty(model.getDisplayName()) && !model.getDefaultDisplayName().equals(model.getDisplayName())) {
			annotation.values().add(createStringPair(ast, AnnotationConstants.DISPLAY_NAME, model.getDisplayName()));
		}
		if (!StringUtils.isEmpty(model.getAlias()) && !model.getDefaultAlias().equals(model.getAlias())) {
			annotation.values().add(createStringPair(ast, AnnotationConstants.ALIAS, model.getAlias()));
		}
		if (model.getDefaultRow() != model.getRow()) {
			annotation.values().add(createNumberPair(ast, AnnotationConstants.ROW, model.getRow()));
		}
		if (model.getDefaultColumn() != model.getColumn()) {
			annotation.values().add(createNumberPair(ast, AnnotationConstants.COLUMN, model.getColumn()));
		}
		if (model.getDefaultLength() != model.getLength()) {
			annotation.values().add(createNumberPair(ast, AnnotationConstants.LENGTH, model.getLength()));
		}
		if (!StringUtils.isEmpty(model.getWhen())) {
			annotation.values().add(createStringPair(ast, AnnotationConstants.WHEN, model.getWhen()));
		}

		if (!StringUtils.isEmpty(model.getFocusField()) && !model.getDefaultFocusField().equals(model.getFocusField())) {
			annotation.values().add(createStringPair(ast, ScreenAnnotationConstants.FOCUS_FIELD, model.getFocusField()));
		}
		if (model.getType() != null && !model.getDefaultActionType().equals(model.getType())) {
			annotation.values().add(createEnumPair(ast, ScreenAnnotationConstants.TYPE, model.getType()));
			addImport(ast, cu, rewriter, model.getType().getClass());
		}
		if (model.getTargetEntity() != null
				&& !model.getDefaultTargetEntity().getName().equals(model.getTargetEntity().getName())) {
			annotation.values().add(createTypePair(ast, ScreenAnnotationConstants.TARGET_ENTITY, model.getTargetEntity()));
			addImport(ast, cu, rewriter, model.getTargetEntity());
		}
		if (model.getDefaultSleep() != model.getSleep()) {
			annotation.values().add(createNumberPair(ast, ScreenAnnotationConstants.SLEEP, model.getSleep()));
		}
		if (model.getDefaultGlobal() != model.isGlobal()) {
			annotation.values().add(createBooleanPair(ast, ScreenAnnotationConstants.GLOBAL, model.isGlobal()));
		}

		if (model.getKeyboardKey() != null && !model.getDefaultKeyboardKey().getName().equals(model.getKeyboardKey().getName())) {
			annotation.values().add(createTypePair(ast, AnnotationConstants.KEYBOARD_KEY, model.getKeyboardKey()));
			addImport(ast, cu, rewriter, model.getKeyboardKey());
		}

		return annotation;
	}

	/**
	 * @param ast
	 * @param item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private NormalAnnotation getTableActionAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, TableActionModel model) {
		NormalAnnotation annotation = null;
		if (model.getDisplayName() == null || model.getDisplayName().isEmpty()) {
			return annotation;
		}
		annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName(TableAction.class.getSimpleName()));
		addImport(ast, cu, rewriter, TableAction.class);
		if (model.getActionValue() != null) {
			annotation.values().add(createStringPair(ast, AnnotationConstants.ACTION_VALUE, model.getActionValue()));
		}
		annotation.values().add(createStringPair(ast, AnnotationConstants.DISPLAY_NAME, model.getDisplayName()));
		annotation.values().add(createTypePair(ast, AnnotationConstants.TARGET_ENTITY, model.getTargetEntity()));
		addImport(ast, cu, rewriter, model.getTargetEntity());
		annotation.values().add(createBooleanPair(ast, AnnotationConstants.DEFAULT_ACTION, model.isDefaultAction()));
		annotation.values().add(createTypePair(ast, AnnotationConstants.ACTION, model.getAction()));
		addImport(ast, cu, rewriter, model.getAction());
		annotation.values().add(createStringPair(ast, AnnotationConstants.ALIAS, model.getAlias()));

		if (model.getDefaultRow() != model.getRow()) {
			annotation.values().add(createNumberPair(ast, AnnotationConstants.ROW, model.getRow()));
		}
		if (model.getDefaultColumn() != model.getColumn()) {
			annotation.values().add(createNumberPair(ast, AnnotationConstants.COLUMN, model.getColumn()));
		}
		if (model.getDefaultLength() != model.getLength()) {
			annotation.values().add(createNumberPair(ast, AnnotationConstants.LENGTH, model.getLength()));
		}
		if (!StringUtils.equals(model.getDefaultWhen(), model.getWhen())) {
			annotation.values().add(createStringPair(ast, AnnotationConstants.WHEN, model.getWhen()));
		}
		return annotation;
	}

}
