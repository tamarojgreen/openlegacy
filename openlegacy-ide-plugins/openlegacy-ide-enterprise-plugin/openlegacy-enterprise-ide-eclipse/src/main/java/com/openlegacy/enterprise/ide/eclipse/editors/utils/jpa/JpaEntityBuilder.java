package com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaByteFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaListFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.ASTUtils;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.AbstractEntityBuilder;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntityBuilder extends AbstractEntityBuilder {

	public static final JpaEntityBuilder INSTANCE = new JpaEntityBuilder();

	private JpaEntityBuilder() {}

	private static class PrivateMethods {

		private static MemberValuePair findPair(List<MemberValuePair> list, String name) {
			MemberValuePair pair = null;
			for (MemberValuePair memberValuePair : list) {
				if (memberValuePair.getName().getFullyQualifiedName().equals(name)) {
					return memberValuePair;
				}
			}
			return pair;
		}

		private static boolean isActionAvailableForField(AbstractAction action, String rootName) {
			if (action instanceof JpaFieldAction) {
				NamedObject parentModel = ((JpaFieldModel)action.getNamedObject()).getParent();
				String parentClassName = "";
				if (parentModel instanceof JpaEntityModel) {
					parentClassName = ((JpaEntityModel)parentModel).getClassName();
				}
				if (parentClassName.equals(rootName)) {
					return true;
				}
			}
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public void addEntityTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			List<AbstractAction> list) {
		for (AbstractAction action : list) {
			if (!action.getAnnotationClass().equals(Table.class)) {
				continue;
			}
			if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.NORMAL_ANNOTATION)) {
				NormalAnnotation annotation = ast.newNormalAnnotation();
				annotation.setTypeName(ast.newSimpleName(action.getAnnotationClass().getSimpleName()));
				boolean isAnnotationExist = false;
				// retrieve last annotation
				List<ASTNode> originalList = listRewriter.getOriginalList();
				ASTNode lastNode = null;
				for (ASTNode node : originalList) {
					if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
						lastNode = node;
						// check if annotation already exist
						NormalAnnotation normalAnnotation = (NormalAnnotation)node;
						if (normalAnnotation.getTypeName().getFullyQualifiedName().equals(
								annotation.getTypeName().getFullyQualifiedName())) {
							isAnnotationExist = true;
							break;
						}
					}
				}
				if (!isAnnotationExist) {
					listRewriter.insertAfter(annotation, lastNode, null);
					ASTUtils.addImport(ast, cu, rewriter, action.getAnnotationClass());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void removeEntityTopLevelAnnotations(ListRewrite listRewriter, List<AbstractAction> list) {
		List<ASTNode> nodeList = listRewriter.getOriginalList();

		for (AbstractAction action : list) {
			if (!action.getAnnotationClass().equals(Table.class)) {
				continue;
			}
			if (action.getActionType().equals(ActionType.REMOVE) && (action.getTarget() == ASTNode.NORMAL_ANNOTATION)) {
				for (ASTNode node : nodeList) {
					if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
						NormalAnnotation annotation = (NormalAnnotation)node;
						if (annotation.getTypeName().getFullyQualifiedName().equals(action.getAnnotationClass().getSimpleName())) {
							listRewriter.remove(node, null);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void addFieldAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, List<AbstractAction> list) {
		for (AbstractAction action : list) {
			if (!action.getAnnotationClass().equals(Column.class) && !action.getAnnotationClass().equals(OneToMany.class)
					&& !action.getAnnotationClass().equals(Id.class)) {
				continue;
			}
			if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.NORMAL_ANNOTATION)
					&& (action.getNamedObject() instanceof JpaFieldModel)) {
				// get field name
				String fieldName = "";
				List<VariableDeclarationFragment> fragments = field.fragments();
				if (fragments.size() > 0) {
					fieldName = fragments.get(0).getName().getFullyQualifiedName();
				}
				JpaFieldModel model = (JpaFieldModel)action.getNamedObject();
				if (StringUtils.equals(model.getFieldName(), fieldName)) {

					NormalAnnotation annotation = ast.newNormalAnnotation();
					annotation.setTypeName(ast.newSimpleName(action.getAnnotationClass().getSimpleName()));
					boolean isAnnotationExist = false;
					// retrieve last annotation
					List<IExtendedModifier> modifiers = field.modifiers();
					for (IExtendedModifier modifier : modifiers) {
						if (modifier.isAnnotation() && modifier instanceof Annotation) {
							// check if annotation already exist
							Annotation annotation2 = (Annotation)modifier;
							if (StringUtils.equals(annotation2.getTypeName().getFullyQualifiedName(),
									annotation.getTypeName().getFullyQualifiedName())) {
								isAnnotationExist = true;
								break;
							}
						}
					}
					if (!isAnnotationExist) {
						VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
						fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(fieldName)));
						FieldDeclaration newField = ast.newFieldDeclaration(fragment);
						newField.setType((Type)ASTNode.copySubtree(ast, field.getType()));
						newField.modifiers().add(annotation);
						newField.modifiers().addAll(ASTNode.copySubtrees(ast, field.modifiers()));
						listRewriter.replace(field, newField, null);
						ASTUtils.addImport(ast, cu, rewriter, action.getAnnotationClass());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void removeFieldAnnotations(ListRewrite listRewriter, FieldDeclaration field, List<AbstractAction> list) {

		for (AbstractAction action : list) {
			if (!action.getAnnotationClass().equals(Column.class) && !action.getAnnotationClass().equals(OneToMany.class)
					&& !action.getAnnotationClass().equals(Id.class)) {
				continue;
			}
			if (action.getActionType().equals(ActionType.REMOVE) && (action.getTarget() == ASTNode.NORMAL_ANNOTATION)
					&& (action.getNamedObject() instanceof JpaFieldModel)) {
				// get field name
				String fieldName = "";
				List<VariableDeclarationFragment> fragments = field.fragments();
				if (fragments.size() > 0) {
					fieldName = fragments.get(0).getName().getFullyQualifiedName();
				}
				JpaFieldModel model = (JpaFieldModel)action.getNamedObject();
				if (StringUtils.equals(model.getFieldName(), fieldName)) {
					ListRewrite listRewrite = listRewriter.getASTRewrite().getListRewrite(field,
							FieldDeclaration.MODIFIERS2_PROPERTY);
					List<ASTNode> originalList = listRewrite.getOriginalList();
					for (ASTNode node : originalList) {
						if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
							NormalAnnotation normalAnnotation = (NormalAnnotation)node;
							if (StringUtils.equals(normalAnnotation.getTypeName().getFullyQualifiedName(),
									action.getAnnotationClass().getSimpleName())) {
								listRewrite.remove(node, null);
								break;
							}
						}
					}
				}
			}
		}
	}

	public void processJpaEntityAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			Annotation annotation, List<JpaEntityAction> list) {
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, list, JpaEntityASTUtils.INSTANCE,
				JpaEntityActionsSorter.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public void processJpaTableAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			Annotation annotation, List<JpaTableAction> list) {
		NormalAnnotation newAnnotation = processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, list,
				JpaEntityASTUtils.INSTANCE, JpaEntityActionsSorter.INSTANCE);

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
				MemberValuePair pair = PrivateMethods.findPair(pairs, action.getKey());
				// if pair equals null then it means that pair should be added
				if (pair == null) {
					if (val instanceof List) {
						MemberValuePair arrPair = JpaEntityASTUtils.INSTANCE.createArrayPair(ast, cu, rewriter, action.getKey(),
								(List<UniqueConstraintDefinition>)val);
						if (arrPair != null) {
							pairs.add(arrPair);
							ASTUtils.addImport(ast, cu, rewriter, UniqueConstraint.class);
						}
					}
					continue;
				}
				// existing
				switch (action.getActionType()) {
					case MODIFY:
						if (val instanceof List) {
							pair.setValue(JpaEntityASTUtils.INSTANCE.createArrayLiteral(ast, cu, rewriter,
									(List<UniqueConstraintDefinition>)val));
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
	}

	@SuppressWarnings("unchecked")
	public void removeFields(ListRewrite listRewriter, String rootName, List<AbstractAction> list) {
		List<BodyDeclaration> nodeList = listRewriter.getOriginalList();

		for (AbstractAction action : list) {
			if (PrivateMethods.isActionAvailableForField(action, rootName) && action.getActionType().equals(ActionType.REMOVE)
					&& (action.getTarget() == ASTNode.FIELD_DECLARATION)) {
				for (BodyDeclaration bd : nodeList) {
					if (bd.getNodeType() == ASTNode.FIELD_DECLARATION) {
						FieldDeclaration field = (FieldDeclaration)bd;
						List<VariableDeclarationFragment> fragments = field.fragments();
						if (fragments.size() > 0) {
							String fieldName = fragments.get(0).getName().getFullyQualifiedName();
							if (fieldName.equals(((JpaFieldModel)action.getNamedObject()).getPreviousFieldName())) {
								listRewriter.remove(bd, null);
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void createNewFields(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter, String rootName,
			List<AbstractAction> list) {

		for (AbstractAction action : list) {
			if (PrivateMethods.isActionAvailableForField(action, rootName) && action.getActionType().equals(ActionType.ADD)
					&& (action.getTarget() == ASTNode.FIELD_DECLARATION)) {
				// create empty field declaration
				VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
				String fieldName = ((JpaFieldModel)action.getNamedObject()).getFieldName();
				fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(fieldName)));

				FieldDeclaration field = ast.newFieldDeclaration(fragment);
				// create String field
				field.setType(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
				// boolean field
				if (action instanceof JpaBooleanFieldAction) {
					field.setType(ast.newPrimitiveType(PrimitiveType.BOOLEAN));
				}
				// byte[] field
				if (action instanceof JpaByteFieldAction) {
					field.setType(ast.newArrayType(ast.newPrimitiveType(PrimitiveType.BYTE)));
				}
				// Integer field
				if (action instanceof JpaIntegerFieldAction) {
					field.setType(ast.newSimpleType(ast.newSimpleName(Integer.class.getSimpleName())));
				}
				// Date field
				if (action instanceof JpaDateFieldAction) {
					field.setType(ast.newSimpleType(ast.newSimpleName(Date.class.getSimpleName())));
					ASTUtils.addImport(ast, cu, rewriter, Date.class);
				}
				// List field
				if (action instanceof JpaListFieldAction) {
					ParameterizedType listType = ast.newParameterizedType(ast.newSimpleType(ast.newSimpleName(List.class.getSimpleName())));
					listType.typeArguments().add(ast.newSimpleType(ast.newSimpleName(Object.class.getSimpleName())));
					field.setType(listType);
					ASTUtils.addImport(ast, cu, rewriter, List.class);
				}
				// skip adding annotations
				/*
				 * // add @OneToMany annotation if (action instanceof JpaListFieldAction) {
				 * field.setType(ast.newSimpleType(ast.newSimpleName(List.class.getSimpleName()))); NormalAnnotation
				 * listAnnotation = ast.newNormalAnnotation();
				 * listAnnotation.setTypeName(ast.newSimpleName(OneToMany.class.getSimpleName()));
				 * field.modifiers().add(listAnnotation); ASTUtils.addImport(ast, cu, rewriter, OneToMany.class); } // add @Column
				 * annotation NormalAnnotation annotation = ast.newNormalAnnotation();
				 * annotation.setTypeName(ast.newSimpleName(Column.class.getSimpleName())); field.modifiers().add(annotation);
				 * ASTUtils.addImport(ast, cu, rewriter, Column.class);
				 */
				// must be added as last
				field.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
				listRewriter.insertLast(field, null);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void processJpaFieldDeclaration(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, String rootName, List<JpaFieldAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		FieldDeclaration newField = null;
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName))
					&& (action.getNamedObject() instanceof JpaFieldModel)) {
				JpaFieldModel model = (JpaFieldModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getPreviousFieldName().equals(fieldName)) {
					if (newField == null) {
						VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
						fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(model.getFieldName())));
						newField = ast.newFieldDeclaration(fragment);
						Type type = ASTUtils.cloneType(ast, field.getType());
						newField.setType(type);
						break;
					}
				}
			}
		}
		if ((newField != null) && (listRewriter != null)) {
			newField.modifiers().addAll(ASTNode.copySubtrees(ast, field.modifiers()));
			listRewriter.replace(field, newField, null);
		}

	}

	@SuppressWarnings("unchecked")
	public void processJpaFieldAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, NormalAnnotation fieldAnnotation, String rootName, List<? extends JpaFieldAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName))
					&& (action.getNamedObject() instanceof JpaFieldModel)) {
				JpaFieldModel model = (JpaFieldModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getFieldName().equals(fieldName)
						&& ((action.getActionType().equals(ActionType.MODIFY) || (action.getActionType().equals(ActionType.REMOVE))))) {
					requiredActions.add(action);
				}
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, fieldAnnotation, requiredActions, JpaEntityASTUtils.INSTANCE,
				JpaEntityActionsSorter.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public void processJpaFieldParameterizedType(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, String rootName, List<JpaFieldAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		FieldDeclaration newField = null;
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName))
					&& (action.getNamedObject() instanceof JpaListFieldModel)) {
				JpaListFieldModel model = (JpaListFieldModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getPreviousFieldName().equals(fieldName)) {
					if (newField == null) {
						VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
						fragment.setName(ast.newSimpleName(fieldName));
						newField = ast.newFieldDeclaration(fragment);
						ParameterizedType listType = ast.newParameterizedType(ast.newSimpleType(ast.newSimpleName(List.class.getSimpleName())));
						listType.typeArguments().add(
								ast.newSimpleType(ast.newSimpleName(model.getFieldTypeArgsClass().getSimpleName())));
						newField.setType(listType);
						break;
					}
				}
			}
		}
		if ((newField != null) && (listRewriter != null)) {
			newField.modifiers().addAll(ASTNode.copySubtrees(ast, field.modifiers()));
			listRewriter.replace(field, newField, null);
		}
	}

}
