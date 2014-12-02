package com.openlegacy.enterprise.ide.eclipse.editors.utils.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ChildEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.PartPositionAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenDateFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenDescriptionFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenFieldValuesAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenIdentifiersAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenTableActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.TableActionAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ChildEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.IdentifierModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.PartPositionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;
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
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenBooleanField;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.annotations.screen.ScreenDescriptionField;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.utils.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenEntityBuilder extends AbstractEntityBuilder {

	public static final ScreenEntityBuilder INSTANCE = new ScreenEntityBuilder();

	private ScreenEntityBuilder() {}

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

		private static boolean isActionAvailableForColumn(AbstractAction action, String rootName) {
			if (action instanceof ScreenColumnAction) {
				ScreenTableModel parentModel = (ScreenTableModel)((ScreenColumnModel)action.getNamedObject()).getParent();
				if (parentModel.getClassName().equals(rootName)) {
					return true;
				}
			}
			return false;
		}

		private static boolean isActionAvailableForField(AbstractAction action, String rootName) {
			if (action instanceof ScreenFieldAction) {
				NamedObject parentModel = ((ScreenFieldModel)action.getNamedObject()).getParent();
				String parentClassName = "";
				if (parentModel instanceof ScreenEntityModel) {
					parentClassName = ((ScreenEntityModel)parentModel).getClassName();
				} else if (parentModel instanceof ScreenPartModel) {
					parentClassName = ((ScreenPartModel)parentModel).getClassName();
				}
				if (parentClassName.equals(rootName)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * @param action
		 * @param rootName
		 * @return
		 */
		private static boolean isActionAvailableForPart(AbstractAction action, String rootName) {
			boolean isAvailableAction = false;
			if ((action instanceof ScreenPartAction) || (action instanceof PartPositionAction)) {
				isAvailableAction = true;
			}
			// check if action for current table
			String partClassName = "";
			if (action.getNamedObject() instanceof ScreenPartModel) {
				partClassName = ((ScreenPartModel)action.getNamedObject()).getClassName();
			} else if (action.getNamedObject() instanceof PartPositionModel) {
				if (((PartPositionModel)action.getNamedObject()).getParent() instanceof ScreenPartModel) {
					partClassName = ((ScreenPartModel)((PartPositionModel)action.getNamedObject()).getParent()).getClassName();
				}
			}
			if (partClassName.equals(rootName) && isAvailableAction) {
				return true;
			}
			return false;
		}

		private static boolean isActionAvailableForTable(AbstractAction action, String rootName) {
			boolean isAvailableAction = false;
			if ((action instanceof ScreenTableAction) || (action instanceof PartPositionAction)
					|| (action instanceof ScreenTableActionsAction)) {
				isAvailableAction = true;
			}
			// check if action for current table
			String tableClassName = "";
			if (action.getNamedObject() instanceof TableActionModel) {
				tableClassName = ((ScreenTableModel)((TableActionModel)action.getNamedObject()).getParent()).getClassName();
			} else if (action.getNamedObject() instanceof PartPositionModel) {
				if (((PartPositionModel)action.getNamedObject()).getParent() instanceof ScreenTableModel) {
					tableClassName = ((ScreenTableModel)((PartPositionModel)action.getNamedObject()).getParent()).getClassName();
				}
			}
			if (tableClassName.equals(rootName) && isAvailableAction) {
				return true;
			}
			return false;
		}

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
				// get current @Action attributes: action name, display name, alias, additionalKey
				String actionName = "";//$NON-NLS-1$
				MemberValuePair actionPair = null;
				String displayName = "";//$NON-NLS-1$
				String alias = "";//$NON-NLS-1$
				String additionalKey = "";//$NON-NLS-1$
				String when = null;
				int row = ScreenAnnotationConstants.ROW_DEFAULT_VALUE;
				int column = ScreenAnnotationConstants.COLUMN_DEFAULT_VALUE;
				int length = ScreenAnnotationConstants.ROW_DEFAULT_VALUE;
				String focusField = "";//$NON-NLS-1$
				String typeName = org.openlegacy.annotations.screen.Action.ActionType.GENERAL.toString();
				String targetEntityName = ScreenEntity.NONE.class.getSimpleName();
				MemberValuePair targetEntityPair = null;
				boolean global = true;
				int sleep = 0;
				String keyboardKeyName = TerminalActions.NONE.class.getSimpleName();
				MemberValuePair keyboardKeyPair = null;

				for (MemberValuePair pair : pairs) {
					if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.ACTION)) {
						String[] split = ((SimpleType)((TypeLiteral)pair.getValue()).getType()).getName().getFullyQualifiedName().split(
								"\\.");//$NON-NLS-1$
						actionName = split[split.length - 1];
						actionPair = pair;
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.DISPLAY_NAME)) {
						displayName = ((StringLiteral)pair.getValue()).getLiteralValue();
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.ALIAS)) {
						alias = ((StringLiteral)pair.getValue()).getLiteralValue();
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.WHEN)) {
						when = ((StringLiteral)pair.getValue()).getLiteralValue();
					} else if (pair.getName().getFullyQualifiedName().equals(ScreenAnnotationConstants.ADDITIONAL_KEY)) {
						additionalKey = ((QualifiedName)pair.getValue()).getFullyQualifiedName().split("\\.")[1];
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.ROW)) {
						row = Integer.parseInt(((NumberLiteral)pair.getValue()).getToken());
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.COLUMN)) {
						column = Integer.parseInt(((NumberLiteral)pair.getValue()).getToken());
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.LENGTH)) {
						length = Integer.parseInt(((NumberLiteral)pair.getValue()).getToken());
					} else if (pair.getName().getFullyQualifiedName().equals(ScreenAnnotationConstants.FOCUS_FIELD)) {
						focusField = ((StringLiteral)pair.getValue()).getLiteralValue();
					} else if (pair.getName().getFullyQualifiedName().equals(ScreenAnnotationConstants.TYPE)) {
						typeName = ((QualifiedName)pair.getValue()).getFullyQualifiedName().split("\\.")[1];
					} else if (pair.getName().getFullyQualifiedName().equals(ScreenAnnotationConstants.TARGET_ENTITY)) {
						String[] split = ((SimpleType)((TypeLiteral)pair.getValue()).getType()).getName().getFullyQualifiedName().split(
								"\\.");//$NON-NLS-1$
						targetEntityName = split[split.length - 1];
						targetEntityPair = pair;
					} else if (pair.getName().getFullyQualifiedName().equals(ScreenAnnotationConstants.SLEEP)) {
						sleep = Integer.parseInt(((NumberLiteral)pair.getValue()).getToken());
					} else if (pair.getName().getFullyQualifiedName().equals(ScreenAnnotationConstants.GLOBAL)) {
						global = ((BooleanLiteral)pair.getValue()).booleanValue();
					} else if (pair.getName().getFullyQualifiedName().equals(AnnotationConstants.KEYBOARD_KEY)) {
						String[] split = ((SimpleType)((TypeLiteral)pair.getValue()).getType()).getName().getFullyQualifiedName().split(
								"\\.");//$NON-NLS-1$
						keyboardKeyName = split[split.length - 1];
						keyboardKeyPair = pair;
					}
				}
				for (ActionModel model : models) {
					// check, if model represents current @Action annotation
					if (actionName.equals(model.getPreviousActionName()) && displayName.equals(model.getPrevDisplayName())
							&& alias.equals(model.getPrevAlias()) && StringUtils.equals(when, model.getPrevWhen())
							&& row == model.getPrevRow() && column == model.getPrevColumn() && length == model.getPrevLength()
							&& (additionalKey.isEmpty() || additionalKey.equals(model.getPrevAdditionalKey().toString()))
							&& StringUtils.equals(focusField, model.getPrevFocusField())
							&& typeName.equals(model.getPrevType().toString())
							&& StringUtils.equals(targetEntityName, model.getPrevTargetEntityClassName())
							&& sleep == model.getPrevSleep() && global == model.isPrevGlobal()
							&& StringUtils.equals(keyboardKeyName, model.getPrevKeyboardKeyName())) {
						NormalAnnotation newAnnotation = ast.newNormalAnnotation();
						newAnnotation.setTypeName(ast.newSimpleName(Action.class.getSimpleName()));
						// determine what should we do, change existing or create new @Action annotation
						// for myself: why use '||' instead '&&'? Because user can change action value and after return it back
						// before
						// saving
						if (model.getAction() == null || model.getActionName().equals(model.getPreviousActionName())) {
							// get existing action type
							MemberValuePair newPair = ast.newMemberValuePair();
							newAnnotation.values().add(ASTNode.copySubtree(newPair.getAST(), actionPair));
						} else {
							// add new
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.ACTION,
											model.getAction().getClass()));
							ASTUtils.addImport(ast, cu, rewriter, model.getAction().getClass());
						}
						if (!StringUtils.isEmpty(model.getDisplayName())) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.DISPLAY_NAME,
											model.getDisplayName()));
						}
						if (!StringUtils.isEmpty(model.getAlias())) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.ALIAS,
											model.getAlias()));
						}
						if (!model.getAdditionalKey().equals(model.getDefaultAdditionalKey())) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createEnumPair(ast, ScreenAnnotationConstants.ADDITIONAL_KEY,
											model.getAdditionalKey()));
							ASTUtils.addImport(ast, cu, rewriter, model.getAdditionalKey().getClass());
						}
						if (model.getRow() != model.getDefaultRow()) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createNumberPair(ast, AnnotationConstants.ROW, model.getRow()));
						}

						if (model.getColumn() != model.getDefaultColumn()) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createNumberPair(ast, AnnotationConstants.COLUMN,
											model.getColumn()));
						}

						if (model.getLength() != model.getDefaultLength()) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createNumberPair(ast, AnnotationConstants.LENGTH,
											model.getLength()));
						}

						if (model.getWhen() != null) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.WHEN, model.getWhen()));
						}

						if (!StringUtils.isEmpty(model.getFocusField())) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createStringPair(ast, ScreenAnnotationConstants.FOCUS_FIELD,
											model.getFocusField()));
						}
						if (!model.getType().equals(model.getDefaultActionType())) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createEnumPair(ast, ScreenAnnotationConstants.TYPE,
											model.getType()));
							ASTUtils.addImport(ast, cu, rewriter, model.getType().getClass());
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
									ScreenEntityASTUtils.INSTANCE.createTypePair(ast, ScreenAnnotationConstants.TARGET_ENTITY,
											model.getTargetEntity()));
							ASTUtils.addImport(ast, cu, rewriter, model.getTargetEntity());
						}
						if (model.getSleep() != model.getDefaultSleep()) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createNumberPair(ast, ScreenAnnotationConstants.SLEEP,
											model.getSleep()));
						}

						if (model.isGlobal() != model.getDefaultGlobal()) {
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createBooleanPair(ast, ScreenAnnotationConstants.GLOBAL,
											model.isGlobal()));
						}
						if ((model.getKeyboardKey() == null || model.getKeyboardKeyName().equals(model.getPrevKeyboardKeyName()))
								&& keyboardKeyPair != null) {
							// get existing action type
							MemberValuePair newPair = ast.newMemberValuePair();
							newAnnotation.values().add(ASTNode.copySubtree(newPair.getAST(), keyboardKeyPair));
						} else if (model.getKeyboardKey() != null
								&& !model.getKeyboardKeyName().equals(model.getPrevKeyboardKeyName())
								&& !model.getKeyboardKey().getName().equals(model.getDefaultKeyboardKey().getName())) {
							// add new
							newAnnotation.values().add(
									ScreenEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.KEYBOARD_KEY,
											model.getKeyboardKey()));
							ASTUtils.addImport(ast, cu, rewriter, model.getKeyboardKey());
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
						ScreenEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.ACTION,
								model.getAction().getClass()));
				ASTUtils.addImport(ast, cu, rewriter, model.getAction().getClass());
				if (!StringUtils.isEmpty(model.getDisplayName())) {
					// 'displayName' attribute
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.DISPLAY_NAME,
									model.getDisplayName()));
				}
				if (!StringUtils.isEmpty(model.getAlias())) {
					// 'alias' attribute
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.ALIAS, model.getAlias()));
				}
				// 'additionalKey' attribute
				if (!model.getAdditionalKey().equals(model.getDefaultAdditionalKey())) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createEnumPair(ast, ScreenAnnotationConstants.ADDITIONAL_KEY,
									model.getAdditionalKey()));
					ASTUtils.addImport(ast, cu, rewriter, model.getAdditionalKey().getClass());
				}

				if (model.getRow() != model.getDefaultRow()) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createNumberPair(ast, AnnotationConstants.ROW, model.getRow()));
				}

				if (model.getColumn() != model.getDefaultColumn()) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createNumberPair(ast, AnnotationConstants.COLUMN, model.getColumn()));
				}

				if (model.getLength() != model.getDefaultLength()) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createNumberPair(ast, AnnotationConstants.LENGTH, model.getLength()));
				}
				if (model.getWhen() != null) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createStringPair(ast, AnnotationConstants.WHEN, model.getWhen()));
				}
				if (!StringUtils.isEmpty(model.getFocusField())) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createStringPair(ast, ScreenAnnotationConstants.FOCUS_FIELD,
									model.getFocusField()));
				}
				if (!model.getType().equals(model.getDefaultActionType())) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createEnumPair(ast, ScreenAnnotationConstants.TYPE, model.getType()));
					ASTUtils.addImport(ast, cu, rewriter, model.getType().getClass());
				}
				if (model.getTargetEntity() != null
						&& !model.getTargetEntity().getName().equals(model.getDefaultTargetEntity().getName())) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createTypePair(ast, ScreenAnnotationConstants.TARGET_ENTITY,
									model.getTargetEntity()));
					ASTUtils.addImport(ast, cu, rewriter, model.getTargetEntity());
				}
				if (model.getSleep() != model.getDefaultSleep()) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createNumberPair(ast, ScreenAnnotationConstants.SLEEP, model.getSleep()));
				}
				if (model.isGlobal() != model.getDefaultGlobal()) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createBooleanPair(ast, ScreenAnnotationConstants.GLOBAL,
									model.isGlobal()));
				}
				if (model.getKeyboardKey() != null
						&& !model.getKeyboardKey().getName().equals(model.getDefaultKeyboardKey().getName())) {
					newAnnotation.values().add(
							ScreenEntityASTUtils.INSTANCE.createTypePair(ast, AnnotationConstants.KEYBOARD_KEY,
									model.getKeyboardKey()));
					ASTUtils.addImport(ast, cu, rewriter, model.getKeyboardKey());
				}

				newValue.expressions().add(newAnnotation);
			}
			target.setValue(newValue);
		}

		@SuppressWarnings("unchecked")
		private static FieldDeclaration getNewTableFieldDeclaration(AST ast, ScreenTableModel model) {
			// should return: private List<${ScreenTableClass}> ${screenTableClass}s
			VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
			fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(model.getClassName() + "s")));//$NON-NLS-1$

			FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
			ParameterizedType parameterizedType = ast.newParameterizedType(ast.newSimpleType(ast.newSimpleName(List.class.getSimpleName())));
			parameterizedType.typeArguments().add(ast.newSimpleType(ast.newSimpleName(model.getClassName())));
			fieldDeclaration.setType(parameterizedType);

			fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
			return fieldDeclaration;
		}

		@SuppressWarnings("unchecked")
		private static FieldDeclaration getNewPartFieldDeclaration(AST ast, ScreenPartModel model) {
			// should return: private ${ScreenPartClass} ${screenPartClass} = new ${ScreenPartClass}()
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
		}
	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void addEntityTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			List<AbstractAction> list) {

		for (AbstractAction action : list) {
			if (!action.getAnnotationClass().equals(ScreenNavigation.class)
					&& !action.getAnnotationClass().equals(ScreenIdentifiers.class)
					&& !action.getAnnotationClass().equals(ScreenActions.class)) {
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void addScreenPartTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			String rootName, List<AbstractAction> list) {

		Set<String> annotationNames = new HashSet<String>();
		// retrieve names of annotations that already exist
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
				NormalAnnotation normalAnnotation = (NormalAnnotation)node;
				annotationNames.add(normalAnnotation.getTypeName().getFullyQualifiedName());
			} else if (node.getNodeType() == ASTNode.MARKER_ANNOTATION) {
				MarkerAnnotation markerAnnotation = (MarkerAnnotation)node;
				annotationNames.add(markerAnnotation.getTypeName().getFullyQualifiedName());
			}
		}

		for (AbstractAction action : list) {
			if (!PrivateMethods.isActionAvailableForPart(action, rootName)) {
				continue;
			}
			// check if action has a required parameters
			if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.NORMAL_ANNOTATION)) {
				// if annotation exist already, no need to add new
				if (annotationNames.contains(action.getAnnotationClass().getSimpleName())) {
					continue;
				}
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void addTableTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			String rootName, List<AbstractAction> list) {

		for (AbstractAction action : list) {
			if (!PrivateMethods.isActionAvailableForTable(action, rootName)) {
				continue;
			}
			// check if action has a required parameters
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void createNewEntityInnerDeclarations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			List<AbstractAction> list) {

		for (AbstractAction action : list) {
			if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.TYPE_DECLARATION)) {
				TypeDeclaration newTypeDeclaration = null;
				FieldDeclaration newFieldDeclaration = null;
				if (action instanceof ScreenTableAction) {
					ScreenTableModel model = (ScreenTableModel)action.getNamedObject();

					newTypeDeclaration = ast.newTypeDeclaration();
					newTypeDeclaration.setName(ast.newSimpleName(model.getClassName()));

					NormalAnnotation newAnnotation = ast.newNormalAnnotation();
					newAnnotation.setTypeName(ast.newSimpleName(ScreenTable.class.getSimpleName()));

					newTypeDeclaration.modifiers().add(newAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, ScreenTable.class);
					newFieldDeclaration = PrivateMethods.getNewTableFieldDeclaration(ast, model);
					ASTUtils.addImport(ast, cu, rewriter, List.class);
				} else if (action instanceof ScreenPartAction) {
					ScreenPartModel model = (ScreenPartModel)action.getNamedObject();

					newTypeDeclaration = ast.newTypeDeclaration();
					newTypeDeclaration.setName(ast.newSimpleName(model.getClassName()));

					NormalAnnotation newAnnotation = ast.newNormalAnnotation();
					newAnnotation.setTypeName(ast.newSimpleName(ScreenPart.class.getSimpleName()));

					newTypeDeclaration.modifiers().add(newAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, ScreenPart.class);
					newFieldDeclaration = PrivateMethods.getNewPartFieldDeclaration(ast, model);
				}
				if (newTypeDeclaration != null) {
					newTypeDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
					newTypeDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
					listRewriter.insertLast(newTypeDeclaration, null);
					if (newFieldDeclaration != null) {
						listRewriter.insertBefore(newFieldDeclaration, newTypeDeclaration, null);
					}
				}
			} else if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.FIELD_DECLARATION)
					&& (action instanceof ChildEntityAction)) {

				ChildEntityModel model = (ChildEntityModel)action.getNamedObject();
				if (model.getClazz() == null) {
					continue;
				}
				VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
				fragment.setName(ast.newSimpleName(StringUtil.toVariableName(model.getClassName(), false)));

				FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
				fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName(model.getClassName())));
				ASTUtils.addImport(ast, cu, rewriter, model.getClazz());
				// must be added as last
				fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
				listRewriter.insertLast(fieldDeclaration, null);
			} else if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.ENUM_DECLARATION)
					&& (action instanceof ScreenEnumFieldAction)) {

				ScreenEnumFieldModel model = (ScreenEnumFieldModel)action.getNamedObject();

				EnumDeclaration enumDeclaration = getNewEnumDeclaration(ast, model.getFieldName());
				// add import of interface
				ASTUtils.addImport(ast, cu, rewriter, EnumGetValue.class);
				listRewriter.insertLast(enumDeclaration, null);
			}
		}
	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void createNewFields(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter, String rootName,
			List<AbstractAction> list) {

		for (AbstractAction action : list) {
			if (PrivateMethods.isActionAvailableForField(action, rootName) && action.getActionType().equals(ActionType.ADD)
					&& (action.getTarget() == ASTNode.FIELD_DECLARATION)) {
				// create empty field declaration
				VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
				String fieldName = ((ScreenFieldModel)action.getNamedObject()).getFieldName();
				fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(fieldName)));

				FieldDeclaration field = ast.newFieldDeclaration(fragment);
				// create String field
				field.setType(ast.newSimpleType(ast.newSimpleName(String.class.getSimpleName())));
				// add @ScreenBooleanField annotation
				if (action instanceof ScreenBooleanFieldAction) {
					field.setType(ast.newSimpleType(ast.newSimpleName(Boolean.class.getSimpleName())));
					NormalAnnotation boolAnnotation = ast.newNormalAnnotation();
					boolAnnotation.setTypeName(ast.newSimpleName(ScreenBooleanField.class.getSimpleName()));
					field.modifiers().add(boolAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, ScreenBooleanField.class);
				}
				// add @ScreenDateField annotation
				if (action instanceof ScreenDateFieldAction) {
					field.setType(ast.newSimpleType(ast.newSimpleName(Date.class.getSimpleName())));
					ASTUtils.addImport(ast, cu, rewriter, Date.class);
					NormalAnnotation dateAnnotation = ast.newNormalAnnotation();
					dateAnnotation.setTypeName(ast.newSimpleName(ScreenDateField.class.getSimpleName()));
					field.modifiers().add(dateAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, ScreenDateField.class);
				}
				// add enum field
				if (action instanceof ScreenEnumFieldAction) {
					ScreenEnumFieldModel model = (ScreenEnumFieldModel)action.getNamedObject();
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
				// add Integer field
				if (action instanceof ScreenIntegerFieldAction) {
					field.setType(ast.newSimpleType(ast.newSimpleName(Integer.class.getSimpleName())));
				}
				// add @ScreenFieldValues annotation
				if (action instanceof ScreenFieldValuesAction) {
					NormalAnnotation dateAnnotation = ast.newNormalAnnotation();
					dateAnnotation.setTypeName(ast.newSimpleName(ScreenFieldValues.class.getSimpleName()));
					field.modifiers().add(dateAnnotation);
					ASTUtils.addImport(ast, cu, rewriter, ScreenFieldValues.class);
				}
				// add @ScreenField annotation
				NormalAnnotation annotation = ast.newNormalAnnotation();
				annotation.setTypeName(ast.newSimpleName(ScreenField.class.getSimpleName()));
				field.modifiers().add(annotation);
				ASTUtils.addImport(ast, cu, rewriter, ScreenField.class);
				// must be added as last
				field.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
				listRewriter.insertLast(field, null);
			}
		}
	}

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void createNewTableColumns(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			String rootName, List<AbstractAction> list) {

		List<ScreenColumnAction> requiredActions = new ArrayList<ScreenColumnAction>();
		for (AbstractAction action : list) {
			if (action.getActionType().equals(ActionType.ADD) && (action.getTarget() == ASTNode.FIELD_DECLARATION)) {
				if (action instanceof ScreenColumnAction) {
					ScreenColumnModel columnModel = (ScreenColumnModel)action.getNamedObject();
					ScreenTableModel tableModel = (ScreenTableModel)columnModel.getParent();
					if (rootName.equals(tableModel.getClassName())) {
						requiredActions.add((ScreenColumnAction)action);
					}
				}
			}
		}

		for (ScreenColumnAction action : requiredActions) {
			ScreenColumnModel columnModel = (ScreenColumnModel)action.getNamedObject();
			// create empty field declaration
			VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
			fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(columnModel.getFieldName())));

			FieldDeclaration field = ast.newFieldDeclaration(fragment);
			field.setType(ast.newSimpleType(ast.newSimpleName(columnModel.getJavaTypeName())));
			// add @ScreenColumn annotation
			NormalAnnotation annotation = ast.newNormalAnnotation();
			annotation.setTypeName(ast.newSimpleName(ScreenColumn.class.getSimpleName()));
			field.modifiers().add(annotation);
			ASTUtils.addImport(ast, cu, rewriter, ScreenColumn.class);
			// must be added as last
			field.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
			listRewriter.insertLast(field, null);
		}
	}

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param annotation
	 * @param rootName
	 * @param list
	 */
	public void processPartPositionAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, String rootName, List<PartPositionAction> list) {

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (PartPositionAction action : list) {
			if ((PrivateMethods.isActionAvailableForTable(action, rootName) || PrivateMethods.isActionAvailableForPart(action,
					rootName))
					&& (action.getActionType().equals(ActionType.MODIFY) || action.getActionType().equals(ActionType.REMOVE))) {
				requiredActions.add(action);
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, requiredActions, ScreenEntityASTUtils.INSTANCE,
				ScreenEntityActionsSorter.INSTANCE);
	}

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param annotation
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenActionsAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, List<ScreenActionsAction> list) {

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
				MemberValuePair pair = PrivateMethods.findPair(pairs, action.getKey());
				// if pair equals null then it means that pair should be added
				if (pair == null) {
					if (val instanceof List) {
						MemberValuePair arrPair = ScreenEntityASTUtils.INSTANCE.createArrayPair(ast, cu, rewriter,
								action.getKey(), (List<ActionModel>)val);
						if (arrPair != null) {
							pairs.add(arrPair);
							// add imports
							for (ActionModel model : (List<ActionModel>)val) {
								if (model.getAction() != null) {
									ASTUtils.addImport(ast, cu, rewriter, model.getAction().getClass());
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
							// This situation appears because CodeBasedScreenEntityDefinition returns actions names only
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param field
	 * @param columnAnnotation
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenColumnAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, NormalAnnotation columnAnnotation, String rootName, List<ScreenColumnAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForColumn(action, rootName))
					&& (action.getNamedObject() instanceof ScreenColumnModel)) {
				ScreenColumnModel model = (ScreenColumnModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getFieldName().equals(fieldName)
						&& ((action.getActionType().equals(ActionType.MODIFY) || (action.getActionType().equals(ActionType.REMOVE))))) {
					requiredActions.add(action);
				}
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, columnAnnotation, requiredActions,
				ScreenEntityASTUtils.INSTANCE, ScreenEntityActionsSorter.INSTANCE);

	}

	/**
	 * 
	 * @param ast
	 * @param listRewriter
	 * @param field
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenColumnDeclaration(AST ast, ListRewrite listRewriter, FieldDeclaration field, String rootName,
			List<ScreenColumnAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		FieldDeclaration newField = null;
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForColumn(action, rootName))
					&& (action.getNamedObject() instanceof ScreenColumnModel)) {
				ScreenColumnModel model = (ScreenColumnModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getPreviousFieldName().equals(fieldName)) {
					if (newField == null) {
						VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
						fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(model.getFieldName())));
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param annotation
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenEntityAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, List<ScreenEntityAction> list) {

		NormalAnnotation newAnnotation = processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, list,
				ScreenEntityASTUtils.INSTANCE, ScreenEntityActionsSorter.INSTANCE);

		for (AbstractAction action : list) {
			if (!action.getActionType().equals(ActionType.MODIFY) && !action.getActionType().equals(ActionType.REMOVE)) {
				continue;
			}
			if ((action.getTarget() == (ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR))
					&& action.getAnnotationClass().getSimpleName().equals(annotation.getTypeName().getFullyQualifiedName())) {
				if (newAnnotation == null) {
					newAnnotation = ast.newNormalAnnotation();
					newAnnotation.setTypeName(ast.newSimpleName(annotation.getTypeName().getFullyQualifiedName()));
					newAnnotation.values().addAll(ASTNode.copySubtrees(ast, annotation.values()));
				}
				List<MemberValuePair> pairs = newAnnotation.values();
				Object val = action.getValue();
				MemberValuePair pair = PrivateMethods.findPair(pairs, action.getKey());
				// if pair equals null then it means that pair should be added
				if (pair == null) {
					if (val instanceof List) {
						MemberValuePair arrPair = ScreenEntityASTUtils.INSTANCE.createStringArrayPair(ast, action.getKey(),
								(List<String>)val);
						if (arrPair != null) {
							pairs.add(arrPair);
						}
					}
					continue;
				}
				// existing
				switch (action.getActionType()) {
					case MODIFY:
						if (val instanceof List) {
							pair.setValue(ScreenEntityASTUtils.INSTANCE.createStringArrayLiteral(ast, (List<String>)val));
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param field
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenEnumField(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, String rootName, List<ScreenEnumFieldAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName))
					&& (action.getNamedObject() instanceof ScreenFieldModel)) {
				ScreenFieldModel model = (ScreenFieldModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getFieldName().equals(fieldName) && (action.getActionType().equals(ActionType.MODIFY))) {
					requiredActions.add(action);
				}
			}
		}

		for (AbstractAction action : requiredActions) {
			// try to modify java type of field
			if (action.getTarget() == (ASTNode.FIELD_DECLARATION | ASTNode.SIMPLE_TYPE)) {
				// create empty field declaration
				VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
				fragment.setName(ast.newSimpleName(((ScreenFieldModel)action.getNamedObject()).getFieldName()));

				FieldDeclaration newField = ast.newFieldDeclaration(fragment);
				if (action.getValue() != null) {
					newField.setType(ast.newSimpleType(ast.newSimpleName(((Class<?>)action.getValue()).getSimpleName())));
					ASTUtils.addImport(ast, cu, rewriter, (Class<?>)action.getValue());
				} else {
					newField.setType(ast.newSimpleType(ast.newSimpleName(StringUtils.capitalize(fragment.getName().getFullyQualifiedName()))));
				}
				newField.modifiers().addAll(ASTNode.copySubtrees(ast, field.modifiers()));
				listRewriter.replace(field, newField, null);
			}
		}
	}

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param field
	 * @param fieldAnnotation
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenFieldAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, NormalAnnotation fieldAnnotation, String rootName, List<? extends ScreenFieldAction> list) {

		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName))
					&& (action.getNamedObject() instanceof ScreenFieldModel)) {
				ScreenFieldModel model = (ScreenFieldModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getFieldName().equals(fieldName)
						&& ((action.getActionType().equals(ActionType.MODIFY) || (action.getActionType().equals(ActionType.REMOVE))))) {
					requiredActions.add(action);
				}
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, fieldAnnotation, requiredActions, ScreenEntityASTUtils.INSTANCE,
				ScreenEntityActionsSorter.INSTANCE);
	}

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param field
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenFieldDeclaration(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			FieldDeclaration field, String rootName, List<? extends ScreenFieldAction> list) {
		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		FieldDeclaration newField = null;
		for (AbstractAction action : list) {
			if ((PrivateMethods.isActionAvailableForField(action, rootName))
					&& (action.getNamedObject() instanceof ScreenFieldModel)) {
				ScreenFieldModel model = (ScreenFieldModel)action.getNamedObject();
				// check whether is an action for current field
				if (model.getPreviousFieldName().equals(fieldName)) {
					if (newField == null) {
						VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
						fragment.setName(ast.newSimpleName(StringUtils.uncapitalize(model.getFieldName())));
						newField = ast.newFieldDeclaration(fragment);
						Type type = ASTUtils.cloneType(ast, field.getType());
						if (model instanceof ScreenEnumFieldModel) {
							if (!Enum.class.getSimpleName().equals(((ScreenEnumFieldModel)model).getJavaTypeName())) {
								type = ast.newSimpleType(ast.newSimpleName(((ScreenEnumFieldModel)model).getJavaTypeName()));
							}
							Class<?> clazz = ((ScreenEnumFieldModel)model).getType();
							if (clazz != null) {
								ASTUtils.addImport(ast, cu, rewriter, clazz);
							}
						}
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param annotation
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenIdentifiersAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, List<ScreenIdentifiersAction> list) {

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
				MemberValuePair pair = PrivateMethods.findPair(pairs, action.getKey());
				// if pair equals null then it means that pair should be added
				if (pair == null) {
					if (val instanceof List) {
						MemberValuePair arrPair = ScreenEntityASTUtils.INSTANCE.createArrayPair(ast, cu, rewriter,
								action.getKey(), (List<IdentifierModel>)val);
						if (arrPair != null) {
							pairs.add(arrPair);
							ASTUtils.addImport(ast, cu, rewriter, Identifier.class);
							ASTUtils.addImport(ast, cu, rewriter, FieldAttributeType.class);
						}
					}
					continue;
				}
				// existing
				switch (action.getActionType()) {
					case MODIFY:
						if (val instanceof List) {
							pair.setValue(ScreenEntityASTUtils.INSTANCE.createArrayLiteral(ast, cu, rewriter,
									(List<IdentifierModel>)val));
							ASTUtils.addImport(ast, cu, rewriter, FieldAttributeType.class);
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param annotation
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenNavigationAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, List<ScreenNavigationAction> list) {

		NormalAnnotation newAnnotation = processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, list,
				ScreenEntityASTUtils.INSTANCE, ScreenEntityActionsSorter.INSTANCE);

		for (AbstractAction action : list) {
			if (!action.getActionType().equals(ActionType.MODIFY) && !action.getActionType().equals(ActionType.REMOVE)) {
				continue;
			}
			if ((action.getTarget() == (ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR))
					&& action.getAnnotationClass().getSimpleName().equals(annotation.getTypeName().getFullyQualifiedName())) {
				if (newAnnotation == null) {
					newAnnotation = ast.newNormalAnnotation();
					newAnnotation.setTypeName(ast.newSimpleName(annotation.getTypeName().getFullyQualifiedName()));
					newAnnotation.values().addAll(ASTNode.copySubtrees(ast, annotation.values()));
				}
				List<MemberValuePair> pairs = newAnnotation.values();
				Object val = action.getValue();
				MemberValuePair pair = PrivateMethods.findPair(pairs, action.getKey());
				// if pair equals null then it means that pair should be added
				if (pair == null) {
					if (val instanceof List) {
						MemberValuePair arrPair = ScreenEntityASTUtils.INSTANCE.createArrayPair(ast, cu, rewriter,
								action.getKey(), (List<FieldAssignDefinition>)val);
						if (arrPair != null) {
							pairs.add(arrPair);
							ASTUtils.addImport(ast, cu, rewriter, AssignedField.class);
						}
					}
					continue;
				}
				// existing
				switch (action.getActionType()) {
					case MODIFY:
						if (val instanceof List) {
							pair.setValue(ScreenEntityASTUtils.INSTANCE.createArrayLiteral(ast, cu, rewriter,
									(List<FieldAssignDefinition>)val));
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

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param annotation
	 * @param rootName
	 * @param list
	 */
	public void processScreenPartAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, String rootName, List<ScreenPartAction> list) {

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (ScreenPartAction action : list) {
			ScreenPartModel model = (ScreenPartModel)action.getNamedObject();
			if (rootName.equals(model.getClassName())
					&& (action.getActionType().equals(ActionType.MODIFY) || action.getActionType().equals(ActionType.REMOVE))) {
				requiredActions.add(action);
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, requiredActions, ScreenEntityASTUtils.INSTANCE,
				ScreenEntityActionsSorter.INSTANCE);
	}

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param annotation
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processScreenTableActionsAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, String rootName, List<TableActionAction> list) {

		List<TableActionAction> requiredActions = new ArrayList<TableActionAction>();
		// find AbstractActions that related with current table
		for (AbstractAction action : list) {
			// check if action is table action
			if (action instanceof TableActionAction) {
				// get table model for class name comparison
				ScreenTableModel model = (ScreenTableModel)((TableActionModel)action.getNamedObject()).getParent();
				// compare class names
				if (rootName.equals(model.getClassName())) {
					requiredActions.add((TableActionAction)action);
				}
			}
		}

		if (requiredActions.isEmpty()) {
			return;
		}

		// Workflow:
		// 1) extract existing @TableAction annotations that will be not modified by actions
		// 2) create array literal for table actions (using TableActionModel inside action)
		// 3) add 2) to 1)
		// 4) create new array pair
		// 5) add 3) to 4)
		// 6) create new @ScreenTableActions annotation
		// 7) add 5) to 6)
		// 8) replace annotation in list rewriter

		// 1)
		List<NormalAnnotation> toSave = new ArrayList<NormalAnnotation>();
		MemberValuePair actionsPair = PrivateMethods.findPair(annotation.values(), AnnotationConstants.ACTIONS);
		if (actionsPair != null) {
			List<NormalAnnotation> existedTableActions = ((ArrayInitializer)actionsPair.getValue()).expressions();
			for (NormalAnnotation existedAnnotation : existedTableActions) {
				MemberValuePair actionValuePair = PrivateMethods.findPair(existedAnnotation.values(),
						AnnotationConstants.ACTION_VALUE);
				if (actionValuePair == null) {
					continue;
				}
				boolean isExist = false;
				String actionValue = ((StringLiteral)actionValuePair.getValue()).getLiteralValue();
				for (TableActionAction tableAction : requiredActions) {
					TableActionModel model = (TableActionModel)tableAction.getNamedObject();
					// model.actionValue as identifier
					if ((model.getPreviousActionValue().equals(actionValue))
							&& (tableAction.getActionType().equals(ActionType.MODIFY))
							&& (tableAction.getTarget() == (ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR))) {
						isExist = true;
					} else if ((model.getPreviousActionValue().equals(actionValue))
							&& (tableAction.getActionType().equals(ActionType.REMOVE))
							&& ((tableAction.getTarget() == ASTNode.NORMAL_ANNOTATION) || (tableAction.getTarget() == (ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR)))) {
						isExist = true;
					}
				}
				if (!isExist && !toSave.contains(existedAnnotation)) {
					toSave.add(existedAnnotation);
				}
			}
		}
		// 2)
		List<TableActionModel> modelList = new ArrayList<TableActionModel>();
		for (TableActionAction tableAction : requiredActions) {
			TableActionModel model = (TableActionModel)tableAction.getNamedObject();
			if (!modelList.contains(model)
					&& (!(tableAction.getActionType().equals(ActionType.REMOVE) && (tableAction.getTarget() == ASTNode.NORMAL_ANNOTATION)))) {
				modelList.add(model);
			}
		}
		ArrayInitializer arrayLiteral = ScreenEntityASTUtils.INSTANCE.createArrayLiteral(ast, cu, rewriter, modelList);
		// 3)
		toSave.addAll(arrayLiteral.expressions());
		// 4)
		// create literal
		ArrayInitializer newArrayLiteral = ast.newArrayInitializer();
		for (NormalAnnotation item : toSave) {
			newArrayLiteral.expressions().add(ASTNode.copySubtree(ast, item));
		}
		// create pair
		MemberValuePair newPair = ast.newMemberValuePair();
		newPair.setName(ast.newSimpleName(AnnotationConstants.ACTIONS));
		// 5)
		newPair.setValue(newArrayLiteral);
		// 6)
		NormalAnnotation newAnnotation = ast.newNormalAnnotation();
		newAnnotation.setTypeName(ast.newSimpleName(ScreenTableActions.class.getSimpleName()));
		// 7)
		newAnnotation.values().add(newPair);
		// 8)
		listRewriter.replace(annotation, newAnnotation, null);

	}

	/**
	 * 
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param listRewriter
	 * @param annotation
	 * @param rootName
	 * @param list
	 */
	public void processScreenTableAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			NormalAnnotation annotation, String rootName, List<ScreenTableAction> list) {

		List<AbstractAction> requiredActions = new ArrayList<AbstractAction>();
		for (ScreenTableAction action : list) {
			ScreenTableModel model = (ScreenTableModel)action.getNamedObject();
			if (rootName.equals(model.getClassName())
					&& (action.getActionType().equals(ActionType.MODIFY) || action.getActionType().equals(ActionType.REMOVE))) {
				requiredActions.add(action);
			}
		}
		processSimpleAnnotation(ast, cu, rewriter, listRewriter, annotation, requiredActions, ScreenEntityASTUtils.INSTANCE,
				ScreenEntityActionsSorter.INSTANCE);
	}

	/**
	 * 
	 * @param listRewriter
	 * @param list
	 */
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
						// try to remove tables or parts
						TypeDeclaration declaration = (TypeDeclaration)bd;
						String declarationName = declaration.getName().getFullyQualifiedName();
						String modelDeclarationName = "";
						if (action instanceof ScreenTableAction) {
							modelDeclarationName = ((ScreenTableModel)action.getNamedObject()).getClassName();
						} else if (action instanceof ScreenPartAction) {
							modelDeclarationName = ((ScreenPartModel)action.getNamedObject()).getClassName();
						}
						if (declarationName.equals(modelDeclarationName)) {
							listRewriter.remove(bd, null);
						}
					} else if (bd.getNodeType() == ASTNode.FIELD_DECLARATION) {
						// try to remove fields which referenced to table or part
						FieldDeclaration declaration = (FieldDeclaration)bd;
						String fieldName = ((VariableDeclarationFragment)declaration.fragments().get(0)).getName().getFullyQualifiedName();
						String modelDeclarationName = "";
						if (action instanceof ScreenTableAction) {
							modelDeclarationName = StringUtils.uncapitalize(((ScreenTableModel)action.getNamedObject()).getPreviousClassName()
									+ "s");
						} else if (action instanceof ScreenPartAction) {
							modelDeclarationName = StringUtils.uncapitalize(((ScreenPartModel)action.getNamedObject()).getPreviousClassName());
						}
						if (fieldName.equals(modelDeclarationName)) {
							listRewriter.remove(bd, null);
						}
					}
				}
			} else if (action.getActionType().equals(ActionType.REMOVE) && (action.getTarget() == ASTNode.FIELD_DECLARATION)
					&& (action instanceof ChildEntityAction)) {
				// remove child entities
				for (BodyDeclaration bd : nodeList) {
					if (bd.getNodeType() == ASTNode.FIELD_DECLARATION) {
						FieldDeclaration declaration = (FieldDeclaration)bd;
						if (declaration.getType().isSimpleType()) {
							String qualifiedName = ((SimpleType)declaration.getType()).getName().getFullyQualifiedName();
							ChildEntityModel model = (ChildEntityModel)action.getNamedObject();
							if (model.getPreviousClassName().equals(qualifiedName)) {
								listRewriter.remove(bd, null);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param listRewriter
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void removeEntityTopLevelAnnotations(ListRewrite listRewriter, List<AbstractAction> list) {

		List<ASTNode> nodeList = listRewriter.getOriginalList();

		for (AbstractAction action : list) {
			if (!action.getAnnotationClass().equals(ScreenNavigation.class)
					&& !action.getAnnotationClass().equals(ScreenIdentifiers.class)
					&& !action.getAnnotationClass().equals(ScreenActions.class)) {
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

	/**
	 * 
	 * @param listRewriter
	 * @param rootName
	 * @param list
	 */
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
							if (fieldName.equals(((ScreenFieldModel)action.getNamedObject()).getPreviousFieldName())) {
								listRewriter.remove(bd, null);
							}
						}
					}
				}
			}
		}
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
						if (namedObject instanceof ScreenEnumFieldModel) {
							ScreenEnumFieldModel model = (ScreenEnumFieldModel)namedObject;
							if (fullyQualifiedName.equals(model.getJavaTypeName())) {
								listRewriter.remove(bd, null);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param listRewriter
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void removeScreenPartTopLevelAnnotations(ListRewrite listRewriter, String rootName, List<AbstractAction> list) {

		List<ASTNode> nodeList = listRewriter.getRewrittenList();

		for (AbstractAction action : list) {
			if (!PrivateMethods.isActionAvailableForPart(action, rootName)) {
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

	/**
	 * 
	 * @param listRewriter
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void removeTableColumns(ListRewrite listRewriter, String rootName, List<AbstractAction> list) {

		List<BodyDeclaration> nodeList = listRewriter.getOriginalList();

		for (AbstractAction action : list) {
			if (!(action instanceof ScreenColumnAction)) {
				continue;
			}
			ScreenTableModel tableModel = (ScreenTableModel)((ScreenColumnModel)action.getNamedObject()).getParent();
			if (!rootName.equals(tableModel.getClassName())) {
				continue;
			}
			if (action.getActionType().equals(ActionType.REMOVE) && (action.getTarget() == ASTNode.FIELD_DECLARATION)) {
				for (BodyDeclaration bd : nodeList) {
					if (bd.getNodeType() == ASTNode.FIELD_DECLARATION) {
						FieldDeclaration field = (FieldDeclaration)bd;
						List<VariableDeclarationFragment> fragments = field.fragments();
						if (fragments.size() > 0) {
							String fieldName = fragments.get(0).getName().getFullyQualifiedName();
							if (fieldName.equals(((ScreenColumnModel)action.getNamedObject()).getPreviousFieldName())) {
								listRewriter.remove(bd, null);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param listRewriter
	 * @param rootName
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void removeTableTopLevelAnnotations(ListRewrite listRewriter, String rootName, List<AbstractAction> list) {

		List<ASTNode> nodeList = listRewriter.getOriginalList();

		for (AbstractAction action : list) {
			if (!PrivateMethods.isActionAvailableForTable(action, rootName)) {
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

	/**
	 * 
	 * @param ast
	 * @param listRewriter
	 * @param markerAnnotation
	 */
	public void replaceMarkerAnnotationOnNormalAnnnotation(AST ast, ListRewrite listRewriter, MarkerAnnotation markerAnnotation) {

		NormalAnnotation newNormalAnnotation = ast.newNormalAnnotation();
		newNormalAnnotation.setTypeName(ast.newSimpleName(markerAnnotation.getTypeName().getFullyQualifiedName()));

		listRewriter.replace(markerAnnotation, newNormalAnnotation, null);
	}

	/**
	 * @param ast
	 * @param rewriter
	 * @param cu
	 * @param listRewriter
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void processEntityInnerDeclarations(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			List<AbstractAction> list) {
		if (list.isEmpty()) {
			return;
		}
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			// try to rename table or part
			if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
				String fullyQualifiedName = ((TypeDeclaration)node).getName().getFullyQualifiedName();
				for (AbstractAction action : list) {
					if (action.getActionType().equals(ActionType.MODIFY)
							&& (action.getTarget() == (ASTNode.TYPE_DECLARATION | ASTNode.SIMPLE_NAME))) {
						NamedObject model = null;
						String className = null;
						FieldDeclaration fieldDeclaration = null;
						if (action instanceof ScreenTableAction) {
							model = action.getNamedObject();
							if (((ScreenTableModel)model).getPreviousClassName().equals(fullyQualifiedName)) {
								className = ((ScreenTableModel)model).getClassName();
								fieldDeclaration = PrivateMethods.getNewTableFieldDeclaration(ast, (ScreenTableModel)model);
								ASTUtils.addImport(ast, cu, rewriter, List.class);
							}
						} else if (action instanceof ScreenPartAction) {
							model = action.getNamedObject();
							if (((ScreenPartModel)model).getPreviousClassName().equals(fullyQualifiedName)) {
								className = ((ScreenPartModel)model).getClassName();
								fieldDeclaration = PrivateMethods.getNewPartFieldDeclaration(ast, (ScreenPartModel)model);
							}
						}
						if ((model != null) && (className != null)) {
							TypeDeclaration declaration = (TypeDeclaration)ASTNode.copySubtree(ast, node);
							declaration.setName(ast.newSimpleName(className));
							// insert field with new name
							listRewriter.insertBefore(fieldDeclaration, node, null);
							listRewriter.replace(node, declaration, null);
						}
					}
				}
			} else if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				// try to remove field which referenced to table or part
				String fullyQualifiedName = ((VariableDeclarationFragment)((FieldDeclaration)node).fragments().get(0)).getName().getFullyQualifiedName();
				for (AbstractAction action : list) {
					if (action.getActionType().equals(ActionType.MODIFY)
							&& (action.getTarget() == (ASTNode.TYPE_DECLARATION | ASTNode.SIMPLE_NAME))) {
						if (action instanceof ScreenTableAction) {
							String previousClassName = ((ScreenTableModel)action.getNamedObject()).getPreviousClassName();
							if (fullyQualifiedName.equals(StringUtils.uncapitalize(previousClassName + "s"))) {
								listRewriter.remove(node, null);
							}
						} else if (action instanceof ScreenPartAction) {
							String previousClassName = ((ScreenPartModel)action.getNamedObject()).getPreviousClassName();
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
	public void addRemoveScreenDescriptionFieldAnnotation(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			FieldDeclaration field, List<ScreenDescriptionFieldAction> actionList) {

		if (actionList.isEmpty()) {
			return;
		}
		// get field name
		String fieldName = "";
		List<VariableDeclarationFragment> fragments = field.fragments();
		if (fragments.size() > 0) {
			fieldName = fragments.get(0).getName().getFullyQualifiedName();
		}

		ListRewrite fieldListRewrite = rewriter.getListRewrite(field, FieldDeclaration.MODIFIERS2_PROPERTY);
		List<ASTNode> fieldNodeList = fieldListRewrite.getRewrittenList();
		for (ScreenDescriptionFieldAction action : actionList) {
			ScreenFieldModel fieldModel = (ScreenFieldModel)action.getNamedObject();
			if (!fieldName.equals(fieldModel.getFieldName())) {
				continue;
			}
			// if action type equal to REMOVE and column is null or 0 then remove annotation if exist
			if (action.getActionType().equals(ActionType.REMOVE)
					&& (action.getTarget() == (ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR))
					&& (action.getKey().equals(Constants.DESC_COLUMN))
					&& ((fieldModel.getDescriptionFieldModel().getColumn() == null) || (fieldModel.getDescriptionFieldModel().getColumn() == 0))) {

				for (ASTNode astNode : fieldNodeList) {
					if (astNode.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
						NormalAnnotation fieldAnnotation = (NormalAnnotation)astNode;
						String fullyQualifiedName = fieldAnnotation.getTypeName().getFullyQualifiedName();
						if (fullyQualifiedName.equals(ScreenDescriptionField.class.getSimpleName())) {
							fieldListRewrite.remove(astNode, null);
							break;
						}
					}
				}
			} else if (action.getActionType().equals(ActionType.MODIFY)
					&& (action.getTarget() == (ASTNode.NORMAL_ANNOTATION | ASTNode.MEMBER_VALUE_PAIR))
					&& (action.getKey().equals(Constants.DESC_COLUMN))
					&& (fieldModel.getDescriptionFieldModel().getColumn() != null)
					&& (fieldModel.getDescriptionFieldModel().getColumn() != 0)) {
				boolean annotationExist = false;
				for (ASTNode astNode : fieldNodeList) {
					if (astNode.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
						NormalAnnotation fieldAnnotation = (NormalAnnotation)astNode;
						String fullyQualifiedName = fieldAnnotation.getTypeName().getFullyQualifiedName();
						if (fullyQualifiedName.equals(ScreenDescriptionField.class.getSimpleName())) {
							annotationExist = true;
							break;
						}
					}
				}
				if (!annotationExist) {
					NormalAnnotation annotation = ast.newNormalAnnotation();
					annotation.setTypeName(ast.newSimpleName(action.getAnnotationClass().getSimpleName()));
					fieldListRewrite.insertFirst(annotation, null);
					ASTUtils.addImport(ast, cu, rewriter, action.getAnnotationClass());
				}
			}
		}
	}
}
