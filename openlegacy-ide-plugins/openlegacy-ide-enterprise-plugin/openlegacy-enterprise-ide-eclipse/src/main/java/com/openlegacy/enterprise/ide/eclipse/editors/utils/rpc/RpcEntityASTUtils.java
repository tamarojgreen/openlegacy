package com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.ASTUtils;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.openlegacy.annotations.screen.Action;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntityASTUtils extends ASTUtils {

	public static RpcEntityASTUtils INSTANCE = new RpcEntityASTUtils();

	private RpcEntityASTUtils() {}

	@Override
	protected <T> NormalAnnotation getAnnotationForArrayLiteral(AST ast, CompilationUnit cu, ASTRewrite rewriter, T item) {
		if (item.getClass().isAssignableFrom(ActionModel.class)) {
			return getActionAnnotation(ast, (ActionModel)item);
		}
		return null;
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
		annotation.values().add(createStringPair(ast, RpcAnnotationConstants.PATH, model.getProgramPath()));
		if (!model.isGlobal()) {
			annotation.values().add(createBooleanPair(ast, AnnotationConstants.GLOBAL, model.isGlobal()));
		}
		return annotation;
	}

}
