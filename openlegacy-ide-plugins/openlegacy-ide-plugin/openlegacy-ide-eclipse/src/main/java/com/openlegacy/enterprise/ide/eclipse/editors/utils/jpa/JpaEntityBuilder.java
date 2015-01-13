package com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaByteFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDbEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaListFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.ASTUtils;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.AbstractEntityBuilder;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.annotations.db.Action;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.annotations.db.DbEntity;
import org.openlegacy.annotations.db.DbNavigation;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.utils.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

		@SuppressWarnings("unchecked")
		public static void manageExistingActions(AST ast, CompilationUnit cu, ASTRewrite rewriter, MemberValuePair target,
				List<ActionModel> models) {

			ArrayInitializer newValue = ast.newArrayInitializer();

			// helps determine what action models should be added
			List<ActionModel> toRemove = new ArrayList<ActionModel>();

			// get pair value
			ArrayInitializer arrayLiteral = (ArrayInitializer)target.getValue();
			// get annotations from value
			List<NormalAnnotation> expressions = arrayLiteral.expressions();
			for (NormalAnnotation normalAnnotation : expressions) {
				// get annotation pairs
				List<MemberValuePair> pairs = normalAnnotation.values();
				// get current @Action attributes: action name, display name, alias, path, global
				String actionName = "";//$NON-NLS-1$
				MemberValuePair actionPair = null;
				String displayName = null;
				String alias = null;
				boolean global = true;
				String targetEntityName = void.class.getSimpleName();
				MemberValuePair targetEntityPair = null;

				for (MemberValuePair pair : pairs) {
					if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.ACTION)) {
						String[] split = ((SimpleType)((TypeLiteral)pair.getValue()).getType()).getName().getFullyQualifiedName().split(
								"\\.");//$NON-NLS-1$
						actionName = split[split.length - 1];
						actionPair = pair;
						// set displayName and alias into accordance of SimpleActionDefinition getters logic
						if (displayName == null) {
							displayName = StringUtil.toDisplayName(actionName.toLowerCase());
						}
						if (alias == null) {
							alias = actionName.toLowerCase();
						}
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.DISPLAY_NAME)) {
						displayName = ((StringLiteral)pair.getValue()).getLiteralValue();
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.ALIAS)) {
						alias = ((StringLiteral)pair.getValue()).getLiteralValue();
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.GLOBAL)) {
						global = ((BooleanLiteral)pair.getValue()).booleanValue();
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.TARGET_ENTITY)) {
						String[] split = ((SimpleType)((TypeLiteral)pair.getValue()).getType()).getName().getFullyQualifiedName().split(
								"\\.");//$NON-NLS-1$
						targetEntityName = split[split.length - 1];
						targetEntityPair = pair;
					}
				}
				for (ActionModel model : models) {
					// check, if model represents current @Action annotation
					if (actionName.equals(model.getPrevActionName()) && displayName.equals(model.getPrevDisplayName())
							&& alias.equals(model.getPrevAlias()) && (global == model.isPrevGlobal())
							&& StringUtils.equals(targetEntityName, model.getPrevTargetEntityClassName())) {
						NormalAnnotation newAnnotation = ast.newNormalAnnotation();
						newAnnotation.setTypeName(ast.newSimpleName(Action.class.getSimpleName()));
						// determine what should we do, change existing or create new @Action annotation
						// for myself: why use '||' instead '&&'? Because user can change action value and after return it back
						// before
						// saving
						if (model.getAction() == null || model.getActionName().equals(model.getPrevActionName())) {
							// get existing action type
							MemberValuePair newPair = ast.newMemberValuePair();
							newAnnotation.values().add(ASTNode.copySubtree(newPair.getAST(), actionPair));
						} else {
							// add new
							newAnnotation.values().add(
									JpaEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.ACTION,
											model.getAction().getClass()));
							ASTUtils.addImport(ast, cu, rewriter, model.getAction().getClass());
						}
						if (!model.getDisplayName().isEmpty()
								&& !StringUtils.equals(model.getDisplayName(), model.getDefaultDisplayName())) {
							newAnnotation.values().add(
									JpaEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.DISPLAY_NAME,
											model.getDisplayName()));
						}
						if (!model.getAlias().isEmpty() && !StringUtils.equals(model.getAlias(), model.getDefaultAlias())) {
							newAnnotation.values().add(
									JpaEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.ALIAS, model.getAlias()));
						}
						if (!model.isGlobal()) {
							newAnnotation.values().add(
									JpaEntityASTUtils.INSTANCE.createBooleanPair(ast, AnnotationConstants.GLOBAL,
											model.isGlobal()));
						}
						if ((model.getTargetEntity() == null || model.getTargetEntityClassName().equals(
								model.getPrevTargetEntityClassName()))
								&& targetEntityPair != null) {
							// get existing action type
							MemberValuePair newPair = ast.newMemberValuePair();
							newAnnotation.values().add(ASTNode.copySubtree(newPair.getAST(), targetEntityPair));
						} else if (model.getTargetEntity() != null
								&& !model.getTargetEntityClassName().equals(model.getPrevTargetEntityClassName())) {
							// add new
							newAnnotation.values().add(
									JpaEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.TARGET_ENTITY,
											model.getTargetEntity()));
							ASTUtils.addImport(ast, cu, rewriter, model.getTargetEntity());
						}
						newValue.expressions().add(newAnnotation);
						toRemove.add(model);
					}
				}
			}
			// add new @Action annotations
			models.removeAll(toRemove);
			for (ActionModel model : models) {
				NormalAnnotation newAnnotation = ast.newNormalAnnotation();
				newAnnotation.setTypeName(ast.newSimpleName(Action.class.getSimpleName()));
				// 'action' attribute
				newAnnotation.values().add(
						JpaEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.ACTION, model.getAction().getClass()));
				ASTUtils.addImport(ast, cu, rewriter, model.getAction().getClass());
				// 'displayName' attribute
				if (!model.getDisplayName().isEmpty()) {
					newAnnotation.values().add(
							JpaEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.DISPLAY_NAME,
									model.getDisplayName()));
				}
				// 'alias' attribute
				if (!model.getAlias().isEmpty()) {
					newAnnotation.values().add(
							JpaEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.ALIAS, model.getAlias()));
				}
				// 'global' attribute
				if (!model.isGlobal()) {
					newAnnotation.values().add(
							JpaEntityASTUtils.INSTANCE.createBooleanPair(ast, AnnotationConstants.GLOBAL, model.isGlobal()));
				}
				// 'targetEntity' attribute
				if (model.getTargetEntity() != null
						&& !model.getTargetEntity().getName().equals(model.getDefaultTargetEntity().getName())) {
					newAnnotation.values().add(
							JpaEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.TARGET_ENTITY,
									model.getTargetEntity()));
					ASTUtils.addImport(ast, cu, rewriter, model.getTargetEntity());
				}

				newValue.expressions().add(newAnnotation);
			}
			target.setValue(newValue);
		}
	}

	@SuppressWarnings("unchecked")
	public void addEntityTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			List<AbstractAction> list) {
		for (AbstractAction action : list) {
			if (!action.getAnnotationClass().equals(Table.class) && !action.getAnnotationClass().equals(DbEntity.class)
					&& !action.getAnnotationClass().equals(DbNavigation.class)
					&& !action.getAnnotationClass().equals(DbActions.class)) {
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
					} else if (node.getNodeType() == ASTNode.MARKER_ANNOTATION) {
						lastNode = node;
						// check if annotation already exist
						MarkerAnnotation markerAnnotation = (MarkerAnnotation)node;
						if (markerAnnotation.getTypeName().getFullyQualifiedName().equals(
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
			if (!action.getAnnotationClass().equals(Table.class) && !action.getAnnotationClass().equals(DbEntity.class)
					&& !action.getAnnotationClass().equals(DbNavigation.class)
					&& !action.getAnnotationClass().equals(DbActions.class)) {
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
					&& !action.getAnnotationClass().equals(Id.class) && !action.getAnnotationClass().equals(DbColumn.class)
					&& !action.getAnnotationClass().equals(ManyToOne.class)
					&& !action.getAnnotationClass().equals(JoinColumn.class)) {
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
						// if we are trying to add a few annotations at the same time we should replace new field in next
						// iterations
						field = newField;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void removeFieldAnnotations(ListRewrite listRewriter, FieldDeclaration field, List<AbstractAction> list) {

		for (AbstractAction action : list) {
			if (!action.getAnnotationClass().equals(Column.class) && !action.getAnnotationClass().equals(OneToMany.class)
					&& !action.getAnnotationClass().equals(Id.class) && !action.getAnnotationClass().equals(DbColumn.class)
					&& !action.getAnnotationClass().equals(ManyToOne.class)
					&& !action.getAnnotationClass().equals(JoinColumn.class)) {
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
						} else if (node.getNodeType() == ASTNode.MARKER_ANNOTATION) {
							MarkerAnnotation markerAnnotation = (MarkerAnnotation)node;
							if (StringUtils.equals(markerAnnotation.getTypeName().getFullyQualifiedName(),
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

	public void processJpaDbEntityAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			Annotation annotation, List<JpaDbEntityAction> list) {
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

	public void processJpaNavigationAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			Annotation annotation, List<JpaNavigationAction> list) {
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, list, JpaEntityASTUtils.INSTANCE,
				JpaEntityActionsSorter.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public void processJpaDbActionsAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, List<? extends JpaActionsAction> list) {

		NormalAnnotation newAnnotation = null;

		for (AbstractAction action : list) {
			if (!action.getActionType().equals(ActionType.MODIFY) && !action.getActionType().equals(ActionType.REMOVE)) {
				continue;
			}
			if (action.getNamedObject().getModelName().equals(annotation.getTypeName().getFullyQualifiedName())
					&& (action.getTarget() == (ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR))) {
				if (newAnnotation == null) {
					newAnnotation = ast.newNormalAnnotation();
					newAnnotation.setTypeName(ast.newSimpleName(annotation.getTypeName().getFullyQualifiedName()));
					newAnnotation.values().addAll(ASTNode.copySubtrees(ast, annotation.values()));
				}
				List<MemberValuePair> pairs = newAnnotation.values();
				Object val = action.getValue();
				MemberValuePair pair = AbstractEntityBuilder.findPair(pairs, action.getKey());
				// if pair equals null then it means that pair should be added
				if (pair == null) {
					if (val instanceof List) {
						MemberValuePair arrPair = JpaEntityASTUtils.INSTANCE.createArrayPair(ast, cu, rewriter, action.getKey(),
								(List<ActionModel>)val);
						if (arrPair != null) {
							pairs.add(arrPair);
							// add imports
							for (ActionModel model : (List<ActionModel>)val) {
								if (model.getAction() != null) {
									ASTUtils.addImport(ast, cu, rewriter, model.getAction().getClass());
								}
								if (model.getTargetEntity() != null
										&& !model.getTargetEntity().equals(model.getDefaultTargetEntity())) {
									ASTUtils.addImport(ast, cu, rewriter, model.getTargetEntity().getClass());
								}
							}
							ASTUtils.addImport(ast, cu, rewriter, Action.class);
						}
					}
					continue;
				}
				// existing
				switch (action.getActionType()) {
					case MODIFY:
						if (val instanceof List) {
							// Remark: In case with actions we cannot create array literal and replace existed in pair.
							// This situation appears because CodeBasedRpcEntityDefinition returns actions names only
							// (without instance of action)
							PrivateMethods.manageExistingActions(ast, cu, rewriter, pair, (List<ActionModel>)val);
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

}
