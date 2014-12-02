package com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBigIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartListAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.ASTUtils;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.AbstractEntityBuilder;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcDateField;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.utils.StringUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntityBuilder extends AbstractEntityBuilder {

	public static final RpcEntityBuilder INSTANCE = new RpcEntityBuilder();

	private RpcEntityBuilder() {}

	private static class PrivateMethods {

		@SuppressWarnings("unchecked")
		private static void manageExistingActions(AST ast, CompilationUnit cu, ASTRewrite rewriter, MemberValuePair target,
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
				String path = "";//$NON-NLS-1$
				boolean global = true;
				String targetEntityName = RpcEntity.NONE.class.getSimpleName();
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
					} else if (pair.getName().getFullyQualifiedName().equals(RpcAnnotationConstants.PATH)) {
						path = ((StringLiteral)pair.getValue()).getLiteralValue();
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
							&& alias.equals(model.getPrevAlias()) && path.equals(model.getPrevPath())
							&& (global == model.isPrevGlobal())
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
									RpcEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.ACTION,
											model.getAction().getClass()));
							ASTUtils.addImport(ast, cu, rewriter, model.getAction().getClass());
						}
						if (!model.getDisplayName().isEmpty()
								&& !StringUtils.equals(model.getDisplayName(), model.getDefaultDisplayName())) {
							newAnnotation.values().add(
									RpcEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.DISPLAY_NAME,
											model.getDisplayName()));
						}
						if (!model.getAlias().isEmpty() && !StringUtils.equals(model.getAlias(), model.getDefaultAlias())) {
							newAnnotation.values().add(
									RpcEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.ALIAS, model.getAlias()));
						}
						newAnnotation.values().add(
								RpcEntityASTUtils.INSTANCE.createStringPair(ast, RpcAnnotationConstants.PATH,
										model.getProgramPath()));
						if (!model.isGlobal()) {
							newAnnotation.values().add(
									RpcEntityASTUtils.INSTANCE.createBooleanPair(ast, AnnotationConstants.GLOBAL,
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
									RpcEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.TARGET_ENTITY,
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
						RpcEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.ACTION, model.getAction().getClass()));
				ASTUtils.addImport(ast, cu, rewriter, model.getAction().getClass());
				// 'displayName' attribute
				if (!model.getDisplayName().isEmpty()) {
					newAnnotation.values().add(
							RpcEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.DISPLAY_NAME,
									model.getDisplayName()));
				}
				// 'alias' attribute
				if (!model.getAlias().isEmpty()) {
					newAnnotation.values().add(
							RpcEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.ALIAS, model.getAlias()));
				}
				// 'path' attribute
				newAnnotation.values().add(
						RpcEntityASTUtils.INSTANCE.createStringPair(ast, RpcAnnotationConstants.PATH, model.getProgramPath()));
				// 'global' attribute
				if (!model.isGlobal()) {
					newAnnotation.values().add(
							RpcEntityASTUtils.INSTANCE.createBooleanPair(ast, AnnotationConstants.GLOBAL, model.isGlobal()));
				}
				// 'targetEntity' attribute
				if (model.getTargetEntity() != null
						&& !model.getTargetEntity().getName().equals(model.getDefaultTargetEntity().getName())) {
					newAnnotation.values().add(
							RpcEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.TARGET_ENTITY,
									model.getTargetEntity()));
					ASTUtils.addImport(ast, cu, rewriter, model.getTargetEntity());
				}

				newValue.expressions().add(newAnnotation);
			}
			target.setValue(newValue);
		}

		@SuppressWarnings("unchecked")
		private static FieldDeclaration getNewPartFieldDeclaration(AST ast, CompilationUnit cu, ASTRewrite rewriter,
				RpcPartModel model, FieldDeclaration field) {
			// should return: private ${RpcPartClass} ${rpcPartClass} = new ${RpcPartClass}()
			// or if count > 1 should return:
			// private List<${RpcPartClass}> ${rpcPartClass} = new ArrayList<RpcPartClass>() annotated with @RpcPartList(count=..)
			if (model.getCount() == 1) {
				VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
				fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(model.getClassName())));
				// create initializer
				ClassInstanceCreation instanceCreationExp = ast.newClassInstanceCreation();
				instanceCreationExp.setType(ast.newSimpleType(ast.newSimpleName(model.getClassName())));
				fragment.setInitializer(instanceCreationExp);

				FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
				fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName(model.getClassName())));
				fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
				return fieldDeclaration;
			} else {
				// create fragment
				VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
				fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(model.getClassName())));
				// create ArrayList type
				ParameterizedType initializerParameterizedType = ast.newParameterizedType(ast.newSimpleType(ast.newSimpleName(ArrayList.class.getSimpleName())));
				ASTUtils.addImport(ast, cu, rewriter, ArrayList.class);
				// add argument to List type
				initializerParameterizedType.typeArguments().add(ast.newSimpleType(ast.newSimpleName(model.getClassName())));
				// create initializer
				ClassInstanceCreation instanceCreationExp = ast.newClassInstanceCreation();
				instanceCreationExp.setType(initializerParameterizedType);
				fragment.setInitializer(instanceCreationExp);
				// create field declaration
				FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
				// create List type
				ParameterizedType parameterizedType = ast.newParameterizedType(ast.newSimpleType(ast.newSimpleName(List.class.getSimpleName())));
				ASTUtils.addImport(ast, cu, rewriter, List.class);
				// add argument to List type
				parameterizedType.typeArguments().add(ast.newSimpleType(ast.newSimpleName(model.getClassName())));
				// set List to field
				fieldDeclaration.setType(parameterizedType);
				// add @RpcPartList annotation
				NormalAnnotation partListAnnotation = null;
				if (field != null) {
					// check if modifiers contain RpcPartList annotation
					List<ASTNode> modifiers = field.modifiers();
					for (ASTNode modifier : modifiers) {
						if (modifier instanceof NormalAnnotation) {
							NormalAnnotation normalAnnotation = (NormalAnnotation)modifier;
							if (normalAnnotation.getTypeName().getFullyQualifiedName().equals(RpcPartList.class.getSimpleName())) {
								partListAnnotation = (NormalAnnotation)ASTNode.copySubtree(ast, modifier);
							}
						}
					}
				}
				if (partListAnnotation == null) {
					partListAnnotation = ast.newNormalAnnotation();
					partListAnnotation.setTypeName(ast.newSimpleName(RpcPartList.class.getSimpleName()));
					ASTUtils.addImport(ast, cu, rewriter, RpcPartList.class);
				}
				fieldDeclaration.modifiers().add(partListAnnotation);
				fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
				return fieldDeclaration;
			}
		}

		private static <T extends AbstractAction> boolean isActionAvailableForField(AbstractAction action, String rootName,
				Class<T> actionType) {
			if (actionType.isAssignableFrom(action.getClass())) {
				NamedObject parentModel = action.getNamedObject().getParent();
				String parentClassName = "";
				if (parentModel instanceof RpcEntityModel) {
					parentClassName = ((RpcEntityModel)parentModel).getClassName();
				} else if (parentModel instanceof RpcPartModel) {
					parentClassName = ((RpcPartModel)parentModel).getClassName();
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
			List<AbstractAction> list, Class<?> ownerClass) {

		for (AbstractAction action : list) {
			boolean skipAction = !action.getAnnotationClass().equals(RpcActions.class)
					&& !action.getAnnotationClass().equals(RpcNavigation.class);
			skipAction = skipAction && ownerClass != null && action.getNamedObject().getParent() != null
					&& !action.getNamedObject().getParent().getClass().isAssignableFrom(ownerClass);
			skipAction = skipAction || ((ownerClass == null) ^ (action.getNamedObject().getParent() == null));
			if (skipAction) {
				continue;
			}
			if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.NORMAL_ANNOTATION)) {
				NormalAnnotation annotation = ast.newNormalAnnotation();
				annotation.setTypeName(ast.newSimpleName(action.getAnnotationClass().getSimpleName()));
				// retrieve last annotation
				List<ASTNode> originalList = listRewriter.getOriginalList();
				ASTNode lastNode = null;
				for (ASTNode node : originalList) {
					if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
						lastNode = node;
					}
				}
				listRewriter.insertAfter(annotation, lastNode, null);
				ASTUtils.addImport(ast, cu, rewriter, action.getAnnotationClass());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void removeEntityTopLevelAnnotations(ListRewrite listRewriter, List<AbstractAction> list, Class<?> ownerClass) {
		List<ASTNode> nodeList = listRewriter.getOriginalList();

		for (AbstractAction action : list) {
			boolean skipAction = !action.getAnnotationClass().equals(RpcActions.class)
					&& !action.getAnnotationClass().equals(RpcNavigation.class);
			skipAction = skipAction && ownerClass != null && action.getNamedObject().getParent() != null
					&& !action.getNamedObject().getParent().getClass().isAssignableFrom(ownerClass);
			skipAction = skipAction || ((ownerClass == null) ^ (action.getNamedObject().getParent() == null));
			if (skipAction) {
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

	public void processRpcEntityAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, List<RpcEntityAction> list) {

		processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, list, RpcEntityASTUtils.INSTANCE,
				RpcEntityActionsSorter.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public void processRpcActionsAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, List<? extends RpcActionsAction> list) {

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
						MemberValuePair arrPair = RpcEntityASTUtils.INSTANCE.createArrayPair(ast, cu, rewriter, action.getKey(),
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

	@SuppressWarnings("unchecked")
	public void processEntityInnerDeclarations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			List<AbstractAction> list) {

		if (list.isEmpty()) {
			return;
		}
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			// try to rename part
			if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
				String fullyQualifiedName = ((TypeDeclaration)node).getName().getFullyQualifiedName();
				for (AbstractAction action : list) {
					if (action.getActionType().equals(ActionType.MODIFY)
							&& (action.getTarget() == (ASTNode.TYPE_DECLARATION | ASTNode.SIMPLE_NAME))) {
						NamedObject model = null;
						String className = null;
						if (action instanceof RpcPartAction) {
							model = action.getNamedObject();
							if (((RpcPartModel)model).getPreviousClassName().equals(fullyQualifiedName)) {
								className = ((RpcPartModel)model).getClassName();
							}
						}
						if ((model != null) && (className != null)) {
							TypeDeclaration declaration = (TypeDeclaration)ASTNode.copySubtree(ast, node);
							declaration.setName(ast.newSimpleName(className));
							listRewriter.replace(node, declaration, null);
						}
					}
				}
			} else if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				// try to remove field which referenced to part
				String fullyQualifiedName = ((VariableDeclarationFragment)((FieldDeclaration)node).fragments().get(0)).getName().getFullyQualifiedName();
				for (AbstractAction action : list) {
					if (action.getActionType().equals(ActionType.MODIFY)
							&& (action.getTarget() == (ASTNode.TYPE_DECLARATION | ASTNode.SIMPLE_NAME))) {
						if (action instanceof RpcPartAction) {
							String previousClassName = ((RpcPartModel)action.getNamedObject()).getPreviousClassName();
							if (fullyQualifiedName.equals(StringUtils.uncapitalize(previousClassName))) {
								listRewriter.remove(node, null);
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
			if (PrivateMethods.isActionAvailableForField(action, rootName, RpcFieldAction.class)
					&& action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.FIELD_DECLARATION)) {
				// create empty field declaration
				VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
				fragment.setName(ast.newSimpleName(((RpcFieldModel)action.getNamedObject()).getFieldName()));

				FieldDeclaration field = ast.newFieldDeclaration(fragment);
				// create String field
				field.setType(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
				// add @RpcBooleanField annotation
				if (action instanceof RpcBooleanFieldAction) {
					field.setType(ast.newSimpleType(ast.newSimpleName(Boolean.class.getSimpleName())));
					NormalAnnotation boolAnnotation = ast.newNormalAnnotation();
					boolAnnotation.setTypeName(ast.newSimpleName(RpcBooleanField.class.getSimpleName()));
					field.modifiers().add(boolAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, RpcBooleanField.class);
				}
				// add @RpcNumericField annotation with Integer field
				if (action instanceof RpcIntegerFieldAction) {
					field.setType(ast.newSimpleType(ast.newSimpleName(Integer.class.getSimpleName())));
					// BigInteger field
					if (action instanceof RpcBigIntegerFieldAction) {
						field.setType(ast.newSimpleType(ast.newSimpleName(BigInteger.class.getSimpleName())));
						ASTUtils.addImport(ast, cu, rewriter, BigInteger.class);
					}
					NormalAnnotation intAnnotation = ast.newNormalAnnotation();
					intAnnotation.setTypeName(ast.newSimpleName(RpcNumericField.class.getSimpleName()));
					field.modifiers().add(intAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, RpcNumericField.class);
				}
				// add @RpcDateField annotation
				if (action instanceof RpcDateFieldAction) {
					field.setType(ast.newSimpleType(ast.newSimpleName(Date.class.getSimpleName())));
					ASTUtils.addImport(ast, cu, rewriter, Date.class);
					NormalAnnotation boolAnnotation = ast.newNormalAnnotation();
					boolAnnotation.setTypeName(ast.newSimpleName(RpcDateField.class.getSimpleName()));
					field.modifiers().add(boolAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, RpcDateField.class);
				}
				// add enum field
				if (action instanceof RpcEnumFieldAction) {
					RpcEnumFieldModel model = (RpcEnumFieldModel)action.getNamedObject();
					if (model.getPrevJavaTypeName().isEmpty()) {
						field.setType(ast.newSimpleType(ast.newSimpleName(StringUtils.capitalize(model.getFieldName()))));
					} else {
						field.setType(ast.newSimpleType(ast.newSimpleName(model.getJavaTypeName())));
					}
					field.setProperty(Constants.ENUM_FIELD_ENTRIES, true);
					if (model.getType() != null) {
						ASTUtils.addImport(ast, cu, rewriter, model.getType());
					}
				}
				// add @RpcField annotation
				NormalAnnotation annotation = ast.newNormalAnnotation();
				annotation.setTypeName(ast.newSimpleName(RpcField.class.getSimpleName()));
				field.modifiers().add(annotation);
				ASTUtils.addImport(ast, cu, rewriter, RpcField.class);
				// must be added as last
				field.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
				listRewriter.insertLast(field, null);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void removeFields(ListRewrite listRewriter, String rootName, List<AbstractAction> list) {

		List<BodyDeclaration> nodeList = listRewriter.getOriginalList();

		for (AbstractAction action : list) {
			if (PrivateMethods.isActionAvailableForField(action, rootName, RpcFieldAction.class)
					&& action.getActionType().equals(ActionType.REMOVE) && (action.getTarget() == ASTNode.FIELD_DECLARATION)) {
				for (BodyDeclaration bd : nodeList) {
					if (bd.getNodeType() == ASTNode.FIELD_DECLARATION) {
						FieldDeclaration field = (FieldDeclaration)bd;
						List<VariableDeclarationFragment> fragments = field.fragments();
						if (fragments.size() > 0) {
							String fieldName = fragments.get(0).getName().getFullyQualifiedName();
							if (fieldName.equals(((RpcFieldModel)action.getNamedObject()).getPreviousFieldName())) {
								listRewriter.remove(bd, null);
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void processRpcFieldAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, NormalAnnotation fieldAnnotation, String rootName, List<? extends RpcFieldAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName, RpcFieldAction.class))
					&& (action.getNamedObject() instanceof RpcFieldModel)) {
				RpcFieldModel model = (RpcFieldModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getFieldName().equals(fieldName)
						&& ((action.getActionType().equals(ActionType.MODIFY) || (action.getActionType().equals(ActionType.REMOVE))))) {
					requiredActions.add(action);
				}
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, fieldAnnotation, requiredActions, RpcEntityASTUtils.INSTANCE,
				RpcEntityActionsSorter.INSTANCE);
	}

	public void processRpcPartAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, String rootName, List<RpcPartAction> list) {

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (RpcPartAction action : list) {
			RpcPartModel model = (RpcPartModel)action.getNamedObject();
			if (rootName.equals(model.getClassName())
					&& (action.getActionType().equals(ActionType.MODIFY) || action.getActionType().equals(ActionType.REMOVE))) {
				requiredActions.add(action);
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, requiredActions, RpcEntityASTUtils.INSTANCE,
				RpcEntityActionsSorter.INSTANCE);
	}

	public void replaceMarkerAnnotationOnNormalAnnnotation(AST ast, ListRewrite listRewriter, MarkerAnnotation markerAnnotation) {

		NormalAnnotation newNormalAnnotation = ast.newNormalAnnotation();
		newNormalAnnotation.setTypeName(ast.newSimpleName(markerAnnotation.getTypeName().getFullyQualifiedName()));

		listRewriter.replace(markerAnnotation, newNormalAnnotation, null);
	}

	@SuppressWarnings("unchecked")
	public void processRpcFieldDeclaration(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, String rootName, List<RpcFieldAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		FieldDeclaration newField = null;
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName, RpcFieldAction.class))
					&& (action.getNamedObject() instanceof RpcFieldModel)) {
				RpcFieldModel model = (RpcFieldModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getPreviousFieldName().equals(fieldName)) {
					if (newField == null) {
						VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
						fragment.setName(ast.newSimpleName(model.getFieldName()));
						newField = ast.newFieldDeclaration(fragment);
						Type type = ASTUtils.cloneType(ast, field.getType());
						newField.setType(type);
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
	public void removeEntityInnerDeclarations(ListRewrite listRewriter, List<AbstractAction> list) {

		if (list.isEmpty()) {
			return;
		}

		List<BodyDeclaration> nodeList = listRewriter.getRewrittenList();

		for (AbstractAction action : list) {
			if (action.getActionType().equals(ActionType.REMOVE) && (action.getTarget() == ASTNode.TYPE_DECLARATION)) {
				for (BodyDeclaration bd : nodeList) {
					if (bd.getNodeType() == ASTNode.TYPE_DECLARATION) {
						// try to remove parts
						TypeDeclaration declaration = (TypeDeclaration)bd;
						String declarationName = declaration.getName().getFullyQualifiedName();
						String modelDeclarationName = "";
						if (action instanceof RpcPartAction) {
							modelDeclarationName = ((RpcPartModel)action.getNamedObject()).getClassName();
						}
						if (declarationName.equals(modelDeclarationName)) {
							listRewriter.remove(bd, null);
						}
					} else if (bd.getNodeType() == ASTNode.FIELD_DECLARATION) {
						// try to remove fields which referenced to part
						FieldDeclaration declaration = (FieldDeclaration)bd;
						String fieldName = ((VariableDeclarationFragment)declaration.fragments().get(0)).getName().getFullyQualifiedName();
						String modelDeclarationName = "";
						if (action instanceof RpcPartAction) {
							modelDeclarationName = StringUtils.uncapitalize(((RpcPartModel)action.getNamedObject()).getPreviousClassName());
						}
						if (fieldName.equals(modelDeclarationName)) {
							listRewriter.remove(bd, null);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void createNewEntityInnerDeclarations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			List<AbstractAction> list) {

		for (AbstractAction action : list) {
			if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.TYPE_DECLARATION)) {
				TypeDeclaration newTypeDeclaration = null;
				if (action instanceof RpcPartAction) {
					RpcPartModel model = (RpcPartModel)action.getNamedObject();

					newTypeDeclaration = ast.newTypeDeclaration();
					newTypeDeclaration.setName(ast.newSimpleName(model.getClassName()));

					NormalAnnotation newAnnotation = ast.newNormalAnnotation();
					newAnnotation.setTypeName(ast.newSimpleName(RpcPart.class.getSimpleName()));

					newTypeDeclaration.modifiers().add(newAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, RpcPart.class);
				}
				if (newTypeDeclaration != null) {
					newTypeDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
					newTypeDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
					listRewriter.insertLast(newTypeDeclaration, null);
				}
			} else if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.ENUM_DECLARATION)
					&& (action instanceof RpcEnumFieldAction)) {
				RpcEnumFieldModel model = (RpcEnumFieldModel)action.getNamedObject();
				EnumDeclaration enumDeclaration = getNewEnumDeclaration(ast, model.getFieldName());
				// add import of interface
				ASTUtils.addImport(ast, cu, rewriter, EnumGetValue.class);
				listRewriter.insertLast(enumDeclaration, null);
			}
		}
	}

	public void createNewFieldsRelatedToPart(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			String rootName, List<RpcPartAction> list) {

		for (AbstractAction action : list) {
			if (PrivateMethods.isActionAvailableForField(action, rootName, RpcPartAction.class)
					&& action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.FIELD_DECLARATION)) {

				FieldDeclaration field = PrivateMethods.getNewPartFieldDeclaration(ast, cu, rewriter,
						(RpcPartModel)action.getNamedObject(), null);
				listRewriter.insertLast(field, null);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void removeFieldsRelatedToPart(ListRewrite listRewriter, String rootName, List<RpcPartAction> list) {
		List<BodyDeclaration> nodeList = listRewriter.getOriginalList();
		for (AbstractAction action : list) {
			if (PrivateMethods.isActionAvailableForField(action, rootName, RpcPartAction.class)
					&& action.getActionType().equals(ActionType.REMOVE) && (action.getTarget() == ASTNode.FIELD_DECLARATION)) {
				for (BodyDeclaration bd : nodeList) {
					if (bd.getNodeType() == ASTNode.FIELD_DECLARATION) {
						FieldDeclaration field = (FieldDeclaration)bd;
						List<VariableDeclarationFragment> fragments = field.fragments();
						if (fragments.size() > 0) {
							String fieldName = fragments.get(0).getName().getFullyQualifiedName();
							String templateName = StringUtils.uncapitalize(((RpcPartModel)action.getNamedObject()).getPreviousClassName());
							if (fieldName.equals(templateName)) {
								listRewriter.remove(bd, null);
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void processRpcPartListAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, NormalAnnotation fieldAnnotation, String rootName, List<RpcPartListAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName, RpcPartListAction.class))
					&& (action.getNamedObject() instanceof RpcPartModel)) {
				RpcPartModel model = (RpcPartModel)action.getNamedObject();
				// check whether is an action for current field
				if (StringUtils.uncapitalize(model.getClassName()).equals(fieldName)
						&& ((action.getActionType().equals(ActionType.MODIFY) || (action.getActionType().equals(ActionType.REMOVE))))) {
					requiredActions.add(action);
				}
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, fieldAnnotation, requiredActions, RpcEntityASTUtils.INSTANCE,
				RpcEntityActionsSorter.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public void processRpcPartFieldDeclaration(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, String rootName, List<? extends RpcPartAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		FieldDeclaration newField = null;
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName, RpcPartAction.class))
					&& (action.getNamedObject() instanceof RpcPartModel)) {
				RpcPartModel model = (RpcPartModel)action.getNamedObject();
				// check whether is an action for current field
				if (StringUtils.uncapitalize(model.getPreviousClassName()).equals(fieldName)) {
					if (newField == null) {
						newField = PrivateMethods.getNewPartFieldDeclaration(ast, cu, rewriter, model, field);
					}
				}
			}
		}
		if ((newField != null) && (listRewriter != null)) {
			listRewriter.replace(field, newField, null);
		}

	}

	public void processRpcNavigationAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, List<RpcNavigationAction> actionList) {

		processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, actionList, RpcEntityASTUtils.INSTANCE,
				RpcEntityActionsSorter.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public void removeEnumDeclaration(ListRewrite listRewriter, String rootName, List<AbstractAction> list) {
		List<BodyDeclaration> nodeList = listRewriter.getOriginalList();

		for (AbstractAction action : list) {
			if (action.getActionType().equals(ActionType.REMOVE) && (action.getTarget() == ASTNode.ENUM_DECLARATION)) {
				for (BodyDeclaration bd : nodeList) {
					if (bd.getNodeType() == ASTNode.ENUM_DECLARATION) {
						EnumDeclaration declaration = (EnumDeclaration)bd;
						String fullyQualifiedName = declaration.getName().getFullyQualifiedName();
						NamedObject namedObject = action.getNamedObject();
						if (namedObject instanceof RpcEnumFieldModel) {
							RpcEnumFieldModel model = (RpcEnumFieldModel)namedObject;
							if (fullyQualifiedName.equals(model.getJavaTypeName())) {
								listRewriter.remove(bd, null);
							}
						}
					}
				}
			}
		}
	}

}
