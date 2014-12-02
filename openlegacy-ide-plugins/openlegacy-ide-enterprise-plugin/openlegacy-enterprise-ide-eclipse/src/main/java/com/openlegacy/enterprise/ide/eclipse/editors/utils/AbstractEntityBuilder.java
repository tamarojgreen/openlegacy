package com.openlegacy.enterprise.ide.eclipse.editors.utils;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.enums.IEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.EnumEntryModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.IEnumFieldModel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.definitions.EnumGetValue;

import java.util.ArrayList;
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

	/**
	 * @param ast
	 * @param rewriter
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static EnumDeclaration getNewEnumDeclaration(AST ast, String fieldName) {
		// create new enum declaration
		EnumDeclaration enumDeclaration = ast.newEnumDeclaration();
		enumDeclaration.setName(ast.newSimpleName(StringUtils.capitalize(fieldName)));
		// set modifiers
		enumDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		// add interface
		enumDeclaration.superInterfaceTypes().add(ast.newSimpleType(ast.newSimpleName(EnumGetValue.class.getSimpleName())));
		// add 'value' field: private String value;
		VariableDeclarationFragment valueFragment = ast.newVariableDeclarationFragment();
		valueFragment.setName(ast.newSimpleName("value"));//$NON-NLS-1$
		FieldDeclaration valueField = ast.newFieldDeclaration(valueFragment);
		valueField.setType(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
		valueField.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
		enumDeclaration.bodyDeclarations().add(valueField);
		// add 'display' field: private String display;
		VariableDeclarationFragment displayFragment = ast.newVariableDeclarationFragment();
		displayFragment.setName(ast.newSimpleName("display"));//$NON-NLS-1$
		FieldDeclaration displayField = ast.newFieldDeclaration(displayFragment);
		displayField.setType(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
		displayField.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
		enumDeclaration.bodyDeclarations().add(displayField);
		// add constructor
		MethodDeclaration constructorDeclaration = ast.newMethodDeclaration();
		constructorDeclaration.setConstructor(true);
		constructorDeclaration.setName(ast.newSimpleName(enumDeclaration.getName().getFullyQualifiedName()));
		// value parameter for constructor
		SingleVariableDeclaration singleValueDeclaration = ast.newSingleVariableDeclaration();
		singleValueDeclaration.setName(ast.newSimpleName(valueFragment.getName().getFullyQualifiedName()));
		singleValueDeclaration.setType(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
		constructorDeclaration.parameters().add(singleValueDeclaration);
		// display parameter for constructor
		SingleVariableDeclaration singleDisplayDeclaration = ast.newSingleVariableDeclaration();
		singleDisplayDeclaration.setName(ast.newSimpleName(displayFragment.getName().getFullyQualifiedName()));
		singleDisplayDeclaration.setType(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
		constructorDeclaration.parameters().add(singleDisplayDeclaration);

		// create body for constructor
		Block constructorBody = ast.newBlock();
		// 'this.value = value'
		FieldAccess valueFieldAccess = ast.newFieldAccess();
		valueFieldAccess.setExpression(ast.newThisExpression());
		valueFieldAccess.setName(ast.newSimpleName(valueFragment.getName().getFullyQualifiedName()));
		Assignment valueAssignment = ast.newAssignment();
		valueAssignment.setLeftHandSide(valueFieldAccess);
		valueAssignment.setOperator(Assignment.Operator.ASSIGN);
		valueAssignment.setRightHandSide(ast.newSimpleName(singleValueDeclaration.getName().getFullyQualifiedName()));
		constructorBody.statements().add(ast.newExpressionStatement(valueAssignment));
		// 'this.display = display'
		FieldAccess displayFieldAccess = ast.newFieldAccess();
		displayFieldAccess.setExpression(ast.newThisExpression());
		displayFieldAccess.setName(ast.newSimpleName(displayFragment.getName().getFullyQualifiedName()));
		Assignment displayAssignment = ast.newAssignment();
		displayAssignment.setLeftHandSide(displayFieldAccess);
		displayAssignment.setOperator(Assignment.Operator.ASSIGN);
		displayAssignment.setRightHandSide(ast.newSimpleName(singleDisplayDeclaration.getName().getFullyQualifiedName()));
		constructorBody.statements().add(ast.newExpressionStatement(displayAssignment));
		// add body to constructor
		constructorDeclaration.setBody(constructorBody);

		// add getter for value field
		MethodDeclaration getValueDeclaration = ast.newMethodDeclaration();
		getValueDeclaration.setName(ast.newSimpleName("getValue"));//$NON-NLS-1$
		getValueDeclaration.setReturnType2(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
		// create body for getter
		Block getValueBody = ast.newBlock();
		// add return statement to body
		ReturnStatement getValueReturn = ast.newReturnStatement();
		getValueReturn.setExpression(ast.newSimpleName(singleValueDeclaration.getName().getFullyQualifiedName()));
		getValueBody.statements().add(getValueReturn);
		// add body to getter
		getValueDeclaration.setBody(getValueBody);
		getValueDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		// add toString method + @Override
		MethodDeclaration toStringDeclaration = ast.newMethodDeclaration();
		toStringDeclaration.setName(ast.newSimpleName("toString"));//$NON-NLS-1$
		toStringDeclaration.setReturnType2(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
		// create body for toString
		Block toStringBody = ast.newBlock();
		// add return statement to body
		ReturnStatement toStringReturn = ast.newReturnStatement();
		toStringReturn.setExpression(ast.newSimpleName(singleDisplayDeclaration.getName().getFullyQualifiedName()));
		toStringBody.statements().add(toStringReturn);
		// add body to toString
		toStringDeclaration.setBody(toStringBody);
		// add @Override annotation to toString
		MarkerAnnotation overrideAnnotation = ast.newMarkerAnnotation();
		overrideAnnotation.setTypeName(ast.newSimpleName(Override.class.getSimpleName()));
		toStringDeclaration.modifiers().add(overrideAnnotation);
		toStringDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		enumDeclaration.bodyDeclarations().add(constructorDeclaration);
		enumDeclaration.bodyDeclarations().add(getValueDeclaration);
		enumDeclaration.bodyDeclarations().add(toStringDeclaration);
		return enumDeclaration;
	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param enumDeclaration
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processEnumDeclaration(AST ast, ASTRewrite rewriter, EnumDeclaration enumDeclaration, List<IEnumFieldAction> list) {

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (IEnumFieldAction action : list) {
			String enumName = enumDeclaration.getName().getFullyQualifiedName();
			String enumNameFromModel = ((IEnumFieldModel)action.getNamedObject()).getJavaTypeName();
			if (((IEnumFieldModel)action.getNamedObject()).getPrevJavaTypeName().isEmpty()) {
				enumNameFromModel = StringUtils.capitalize(((IEnumFieldModel)action.getNamedObject()).getFieldName());
			}
			if (action.getActionType().equals(ActionType.MODIFY) && (action.getTarget() == ASTNode.ENUM_CONSTANT_DECLARATION)
					&& (enumName.equals(enumNameFromModel))) {
				requiredActions.add((AbstractAction)action);
			}
		}

		// idea is that only one action should be present for modifying enum entries.
		AbstractAction action = requiredActions.size() == 1 ? requiredActions.get(0) : null;
		if (action != null) {
			// get constant rewriter
			ListRewrite enumListRewriter = rewriter.getListRewrite(enumDeclaration, EnumDeclaration.ENUM_CONSTANTS_PROPERTY);
			// remove all existing constants
			List<EnumConstantDeclaration> nodeList = enumListRewriter.getRewrittenList();
			for (EnumConstantDeclaration declaration : nodeList) {
				enumListRewriter.remove(declaration, null);
			}
			// add constants from model
			List<EnumEntryModel> entries = ((IEnumFieldModel)action.getNamedObject()).getEntries();
			for (EnumEntryModel entry : entries) {
				if (entry.isAllowToSave()) {
					EnumConstantDeclaration constantDeclaration = ast.newEnumConstantDeclaration();
					constantDeclaration.setName(ast.newSimpleName(entry.getName()));
					// create value literal
					StringLiteral valueLiteral = ast.newStringLiteral();
					valueLiteral.setLiteralValue(entry.getValue());
					constantDeclaration.arguments().add(0, valueLiteral);
					// create display name literal
					StringLiteral displayLiteral = ast.newStringLiteral();
					displayLiteral.setLiteralValue(entry.getDisplayName());
					constantDeclaration.arguments().add(1, displayLiteral);
					enumListRewriter.insertLast(constantDeclaration, null);
				}
			}
		}

	}

}
