package com.openlegacy.enterprise.ide.eclipse.editors.utils;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.List;

import javax.persistence.CascadeType;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractEntityBuilder {

	@SuppressWarnings("unchecked")
	protected NormalAnnotation processSimpleAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			ListRewrite listRewriter, Annotation annotation, List<? extends AbstractAction> list, ASTUtils astUtils,
			IEntityActionsSorter actionsSorter) {

		NormalAnnotation newAnnotation = null;

		for (AbstractAction action : list) {
			if (!action.getActionType().equals(ActionType.MODIFY) && !action.getActionType().equals(ActionType.REMOVE)) {
				continue;
			}
			if ((action.getTarget() == (ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR))
					&& action.getAnnotationClass().getSimpleName().equals(annotation.getTypeName().getFullyQualifiedName())) {
				if (newAnnotation == null) {
					newAnnotation = ast.newNormalAnnotation();
					newAnnotation.setTypeName(ast.newSimpleName(annotation.getTypeName().getFullyQualifiedName()));
					if (annotation instanceof NormalAnnotation) {
						newAnnotation.values().addAll(ASTNode.copySubtrees(ast, ((NormalAnnotation)annotation).values()));
					}
				}
				List<MemberValuePair> pairs = newAnnotation.values();
				Object val = action.getValue();
				MemberValuePair pair = findPair(pairs, action.getKey(true));
				// if pair equals null then it means that pair should be added
				if (pair == null) {
					int index = actionsSorter.getOrderIndex(action, list.size());
					if (index > pairs.size()) {
						index = pairs.size();
					}
					if (val instanceof Boolean) {
						pairs.add(index, astUtils.createBooleanPair(ast, action.getKey(), (Boolean)val));
					}
					if (val instanceof String) {
						pairs.add(index, astUtils.createStringPair(ast, action.getKey(), (String)val));
					}
					if (val instanceof Integer) {
						pairs.add(index, astUtils.createNumberPair(ast, action.getKey(true), (Integer)val));
					}
					if (val instanceof Class) {
						pairs.add(index, astUtils.createTypePair(ast, action.getKey(), (Class<?>)val));
						ASTUtils.addImport(ast, cu, rewriter, (Class<?>)val);
					}
					if (val instanceof Enum) {
						pairs.add(index, astUtils.createEnumPair(ast, action.getKey(), (Enum<?>)val));
						ASTUtils.addImport(ast, cu, rewriter, ((Enum<?>)val).getClass());
					}
					if (val instanceof Double) {
						pairs.add(index, astUtils.createDoublePair(ast, action.getKey(), (Double)val));
					}
					if (val instanceof CascadeType[]) {
						pairs.add(index, astUtils.createArrayPair(ast, action.getKey(), (CascadeType[])val));
						ASTUtils.addImport(ast, cu, rewriter, CascadeType.class);
					}
					continue;
				}
				// existing
				switch (action.getActionType()) {
					case MODIFY:
						if (val instanceof Boolean) {
							pair.setValue(astUtils.createBooleanLiteral(ast, (Boolean)val));
						}
						if (val instanceof String) {
							pair.setValue(astUtils.createStringLiteral(ast, (String)val));
						}
						if (val instanceof Integer) {
							pair.setValue(astUtils.createNumberLiteral(ast, (Integer)val));
						}
						if (val instanceof Class) {
							pair.setValue(astUtils.createTypeLiteral(ast, (Class<?>)val));
							ASTUtils.addImport(ast, cu, rewriter, (Class<?>)val);
						}
						if (val instanceof Enum) {
							pair.setValue(astUtils.createEnumLiteral(ast, (Enum<?>)val));
						}
						if (val instanceof Double) {
							pair.setValue(astUtils.createDoubleLiteral(ast, (Double)val));
						}
						if (val instanceof CascadeType[]) {
							pair.setValue(astUtils.createArrayLiteral(ast, (CascadeType[])val));
						}
						break;
					case REMOVE:
						pairs.remove(pair);
						break;
					default:
						break;
				}
			}
		}
		if ((newAnnotation != null) && (listRewriter != null)) {
			listRewriter.replace(annotation, newAnnotation, null);
		}
		return newAnnotation;
	}

	protected static MemberValuePair findPair(List<MemberValuePair> list, String name) {
		MemberValuePair pair = null;
		for (MemberValuePair memberValuePair : list) {
			if (memberValuePair.getName().getFullyQualifiedName().equals(name)) {
				return memberValuePair;
			}
		}
		return pair;
	}

}
