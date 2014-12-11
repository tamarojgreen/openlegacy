package com.openlegacy.enterprise.ide.eclipse.editors.utils.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.enums.IEnumFieldAction;
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
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.SortTableActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.TableActionAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.AbstractEntitySaver;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.annotations.screen.PartPosition;
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

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenEntitySaver extends AbstractEntitySaver {

	public static ScreenEntitySaver INSTANCE = new ScreenEntitySaver();

	private ScreenEntitySaver() {}

	@Override
	protected void doSave(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root, AbstractEntity entity) {
		// process top level class annotations: @ScreenEntity, @ScreenNavigation, @ScreenIdentifiers, @ScreenActions
		processEntityTopLevelAnnotations(ast, cu, rewriter, root, (ScreenEntity)entity);

		// process annotations that located inside root:
		// @ScreenTable, @ScreenPart, @PartPosition, @ScreenField (and related)
		processEntityInnerAnnotations(ast, cu, rewriter, root, (ScreenEntity)entity);
	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param root
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	private static void processEntityTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, ScreenEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.MODIFIERS2_PROPERTY);

		// add new annotations to entity
		ScreenEntityBuilder.INSTANCE.addEntityTopLevelAnnotations(ast, cu, rewriter, listRewriter,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.ADD }));

		// remove annotations from entity
		ScreenEntityBuilder.INSTANCE.removeEntityTopLevelAnnotations(listRewriter,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.REMOVE }));

		// handle existing annotations
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
				NormalAnnotation annotation = (NormalAnnotation)node;
				String fullyQualifiedName = annotation.getTypeName().getFullyQualifiedName();
				if (org.openlegacy.annotations.screen.ScreenEntity.class.getSimpleName().equals(fullyQualifiedName)) {
					// @ScreenEntity
					ScreenEntityBuilder.INSTANCE.processScreenEntityAnnotation(ast, cu, rewriter, listRewriter, annotation,
							ScreenEntityUtils.getActionList(entity, ScreenEntityAction.class));
				} else if (ScreenNavigation.class.getSimpleName().equals(fullyQualifiedName)) {
					// @ScreenNavigation
					ScreenEntityBuilder.INSTANCE.processScreenNavigationAnnotation(ast, cu, rewriter, listRewriter, annotation,
							ScreenEntityUtils.getActionList(entity, ScreenNavigationAction.class));
				} else if (ScreenIdentifiers.class.getSimpleName().equals(fullyQualifiedName)) {
					// @ScreenIdentifiers
					ScreenEntityBuilder.INSTANCE.processScreenIdentifiersAnnotation(ast, cu, rewriter, listRewriter, annotation,
							ScreenEntityUtils.getActionList(entity, ScreenIdentifiersAction.class));
				} else if (ScreenActions.class.getSimpleName().equals(fullyQualifiedName)) {
					// @ScreenActions
					ScreenEntityBuilder.INSTANCE.processScreenActionsAnnotation(ast, cu, rewriter, listRewriter, annotation,
							ScreenEntityUtils.getActionList(entity, ScreenActionsAction.class));
				}
			}
		}
	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param root
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	private static void processEntityInnerAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, ScreenEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

		// before handling fields we should add enum declarations for enum field
		ScreenEntityBuilder.INSTANCE.createNewEntityInnerDeclarations(ast, cu, rewriter, listRewriter,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.ENUM_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// process screen fields that relate to root
		processScreenFields(ast, cu, rewriter, root, entity);

		// remove child entities
		ScreenEntityBuilder.INSTANCE.removeEntityInnerDeclarations(listRewriter,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.REMOVE }));
		// add child entities
		ScreenEntityBuilder.INSTANCE.createNewEntityInnerDeclarations(ast, cu, rewriter, listRewriter,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// before creating new declarations we should try to remove declarations with the same names
		ScreenEntityBuilder.INSTANCE.removeEntityInnerDeclarations(listRewriter,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.TYPE_DECLARATION,
						new ActionType[] { ActionType.REMOVE }));
		// we should try to remove aspectJ files if proper actions are exist
		FileRemover.removeAspectJFiles(cu, ScreenEntityUtils.<AbstractAction> getActionList(entity, Constants.ASPECTJ_FILE,
				new ActionType[] { ActionType.REMOVE }));

		ScreenEntityBuilder.INSTANCE.createNewEntityInnerDeclarations(ast, cu, rewriter, listRewriter,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.TYPE_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// try to rename tables and parts
		ScreenEntityBuilder.INSTANCE.processEntityInnerDeclarations(ast, cu, rewriter, listRewriter,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.TYPE_DECLARATION | ASTNode.SIMPLE_NAME,
						new ActionType[] { ActionType.MODIFY }));

		// process tables and parts
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
				TypeDeclaration type = (TypeDeclaration)node;
				List<IExtendedModifier> modifiers = type.modifiers();
				for (IExtendedModifier modifier : modifiers) {
					if (modifier.isAnnotation()) {
						String fullyQualifiedName = "";
						if (modifier instanceof MarkerAnnotation) {
							fullyQualifiedName = ((MarkerAnnotation)modifier).getTypeName().getFullyQualifiedName();
						} else if (modifier instanceof NormalAnnotation) {
							fullyQualifiedName = ((NormalAnnotation)modifier).getTypeName().getFullyQualifiedName();
						}
						if (fullyQualifiedName.equals(ScreenTable.class.getSimpleName())) {
							// @ScreenTable
							processScreenTable(ast, cu, rewriter, listRewriter, type, entity);
						} else if (fullyQualifiedName.equals(ScreenPart.class.getSimpleName())) {
							// @ScreenPart
							processScreenPart(ast, cu, rewriter, type, entity);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void processScreenFields(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root,
			ScreenEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		String rootName = root.getName().getFullyQualifiedName();

		// remove fields before adding new
		ScreenEntityBuilder.INSTANCE.removeFields(listRewriter, root.getName().getFullyQualifiedName(),
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.REMOVE }));

		// try to remove enum declaration
		ScreenEntityBuilder.INSTANCE.removeEnumDeclaration(listRewriter, root.getName().getFullyQualifiedName(),
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.ENUM_DECLARATION,
						new ActionType[] { ActionType.REMOVE }));

		// add new fields
		ScreenEntityBuilder.INSTANCE.createNewFields(ast, cu, rewriter, listRewriter, rootName,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// before modifying screen field annotation properties we should change field name if appropriate actions are exist
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration)node;
				// handle field declarations (in our case it is fieldName)
				ScreenEntityBuilder.INSTANCE.processScreenFieldDeclaration(ast, cu, rewriter, listRewriter, field, rootName,
						ScreenEntityUtils.<ScreenFieldAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
								new ActionType[] { ActionType.MODIFY }));
			}
		}
		// before modifying screen field annotation properties we should change enum type if appropriate actions are exist
		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration)node;
				// handle field declarations (in our case it is fieldName)
				if (field.getType().isSimpleType()) {
					ScreenEntityBuilder.INSTANCE.processScreenEnumField(ast, cu, rewriter, listRewriter, field, rootName,
							ScreenEntityUtils.getActionList(entity, ScreenEnumFieldAction.class));
					ScreenEntityBuilder.INSTANCE.addRemoveScreenDescriptionFieldAnnotation(ast, cu, rewriter, field,
							ScreenEntityUtils.getActionList(entity, ScreenDescriptionFieldAction.class));
				}
			}
		}

		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration)node;
				// handle field annotations
				ListRewrite fieldListRewrite = rewriter.getListRewrite(field, FieldDeclaration.MODIFIERS2_PROPERTY);
				List<ASTNode> fieldNodeList = fieldListRewrite.getRewrittenList();
				for (ASTNode astNode : fieldNodeList) {
					if (astNode.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
						NormalAnnotation fieldAnnotation = (NormalAnnotation)astNode;
						String fullyQualifiedName = fieldAnnotation.getTypeName().getFullyQualifiedName();
						if (fullyQualifiedName.equals(ScreenField.class.getSimpleName())) {
							ScreenEntityBuilder.INSTANCE.processScreenFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, ScreenEntityUtils.getActionList(entity, ScreenFieldAction.class));
						} else if (fullyQualifiedName.equals(ScreenBooleanField.class.getSimpleName())) {
							ScreenEntityBuilder.INSTANCE.processScreenFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName,
									ScreenEntityUtils.getActionList(entity, ScreenBooleanFieldAction.class));
						} else if (fullyQualifiedName.equals(ScreenDateField.class.getSimpleName())) {
							ScreenEntityBuilder.INSTANCE.processScreenFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName,
									ScreenEntityUtils.getActionList(entity, ScreenDateFieldAction.class));
						} else if (fullyQualifiedName.equals(ScreenFieldValues.class.getSimpleName())) {
							ScreenEntityBuilder.INSTANCE.processScreenFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName,
									ScreenEntityUtils.getActionList(entity, ScreenFieldValuesAction.class));
						} else if (fullyQualifiedName.equals(ScreenDescriptionField.class.getSimpleName())) {
							ScreenEntityBuilder.INSTANCE.processScreenFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName,
									ScreenEntityUtils.getActionList(entity, ScreenDescriptionFieldAction.class));
							// SourceUtils.processScreenDescriptionField(ast, cu, rewriter, fieldListRewrite, field,
							// fieldAnnotation, rootName,
							// EntityUtils.getActionList(entity, ScreenDescriptionFieldAction.class));
						}
					}
				}
			} else if (node.getNodeType() == ASTNode.ENUM_DECLARATION) {
				EnumDeclaration enumDeclaration = (EnumDeclaration)node;
				ScreenEntityBuilder.INSTANCE.processEnumDeclaration(ast, rewriter, enumDeclaration,
						ScreenEntityUtils.getActionList(entity, IEnumFieldAction.class));
			}
		}
	}

	private static void processScreenTable(AST ast, CompilationUnit cu, ASTRewrite rewriter, ListRewrite listRewriter,
			AbstractTypeDeclaration root, ScreenEntity entity) {

		// Note: logic looks like we are processing root in general

		// process top level table annotations: @ScreenTable, @ScreenTableActions
		processScreenTableTopLevelAnnotations(ast, cu, rewriter, root, entity);

		// process annotations that located inside table: @ScreenColumn
		processScreenTableInnerAnnotations(ast, cu, rewriter, root, entity);

	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param root
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	private static void processScreenTableTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, ScreenEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.MODIFIERS2_PROPERTY);
		String rootName = root.getName().getFullyQualifiedName();

		// add new annotations to table declaration
		ScreenEntityBuilder.INSTANCE.addTableTopLevelAnnotations(ast, cu, rewriter, listRewriter, rootName,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.ADD }));
		// remove annotations from table declaration
		ScreenEntityBuilder.INSTANCE.removeTableTopLevelAnnotations(listRewriter, rootName,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.REMOVE }));

		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
				NormalAnnotation annotation = (NormalAnnotation)node;
				String fullyQualifiedName = annotation.getTypeName().getFullyQualifiedName();
				if (ScreenTable.class.getSimpleName().equals(fullyQualifiedName)) {
					// @ScreenTable
					ScreenEntityBuilder.INSTANCE.processScreenTableAnnotation(ast, cu, rewriter, listRewriter, annotation,
							rootName, ScreenEntityUtils.getActionList(entity, ScreenTableAction.class));
				} else if (ScreenTableActions.class.getSimpleName().equals(fullyQualifiedName)) {
					// @ScreenTableActions
					ScreenEntityBuilder.INSTANCE.processScreenTableActionsAnnotation(ast, cu, rewriter, listRewriter, annotation,
							rootName, ScreenEntityUtils.getActionList(entity, TableActionAction.class));
				} else if (PartPosition.class.getSimpleName().equals(fullyQualifiedName)) {
					// @PartPosition
					ScreenEntityBuilder.INSTANCE.processPartPositionAnnotation(ast, cu, rewriter, listRewriter, annotation,
							rootName, ScreenEntityUtils.getActionList(entity, PartPositionAction.class));
				}
			}
		}

		// sort @TableAction annotations in @ScreenTableActions annotation
		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
				NormalAnnotation annotation = (NormalAnnotation)node;
				String fullyQualifiedName = annotation.getTypeName().getFullyQualifiedName();
				if (ScreenTableActions.class.getSimpleName().equals(fullyQualifiedName)) {
					// @ScreenTableActions
					ScreenEntityBuilder.INSTANCE.sortScreenTableActionsAnnotation(ast, cu, rewriter, listRewriter, annotation,
							rootName, ScreenEntityUtils.getActionList(entity, SortTableActionsAction.class));
				}
			}
		}
	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param root
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	private static void processScreenTableInnerAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, ScreenEntity entity) {

		String rootName = root.getName().getFullyQualifiedName();
		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

		// add new columns
		ScreenEntityBuilder.INSTANCE.createNewTableColumns(ast, cu, rewriter, listRewriter, rootName,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// remove columns
		ScreenEntityBuilder.INSTANCE.removeTableColumns(listRewriter, rootName, ScreenEntityUtils.<AbstractAction> getActionList(
				entity, ASTNode.FIELD_DECLARATION, new ActionType[] { ActionType.REMOVE }));

		// modify columns
		// before modifying screen column annotation properties we should change field name if appropriate actions are exist
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration)node;
				// handle field declarations (in our case it is fieldName)
				ScreenEntityBuilder.INSTANCE.processScreenColumnDeclaration(
						ast,
						listRewriter,
						field,
						rootName,
						ScreenEntityUtils.<ScreenColumnAction> getActionList(entity, ASTNode.FIELD_DECLARATION
								| ASTNode.MEMBER_VALUE_PAIR, new ActionType[] { ActionType.MODIFY }));
			}
		}
		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration)node;
				// handle column annotations
				ListRewrite columnListRewrite = rewriter.getListRewrite(field, FieldDeclaration.MODIFIERS2_PROPERTY);
				List<ASTNode> columnNodeList = columnListRewrite.getRewrittenList();
				for (ASTNode astNode : columnNodeList) {
					if (astNode.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
						NormalAnnotation columnAnnotation = (NormalAnnotation)astNode;
						String fullyQualifiedName = columnAnnotation.getTypeName().getFullyQualifiedName();
						if (fullyQualifiedName.equals(ScreenColumn.class.getSimpleName())) {
							ScreenEntityBuilder.INSTANCE.processScreenColumnAnnotation(ast, cu, rewriter, columnListRewrite,
									field, columnAnnotation, rootName,
									ScreenEntityUtils.getActionList(entity, ScreenColumnAction.class));
						}
					}
				}
			}
		}
	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param root
	 * @param entity
	 */
	private static void processScreenPart(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root,
			ScreenEntity entity) {

		// Note: logic looks like we are processing root in general

		// process top level screen part annotations: @ScreenPart, @PartPosition
		processScreenPartTopLevelAnnotations(ast, cu, rewriter, root, entity);

		// process annotations that located inside table: @ScreenField
		processScreenPartInnerAnnotations(ast, cu, rewriter, root, entity);

	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param root
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	private static void processScreenPartTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, ScreenEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.MODIFIERS2_PROPERTY);
		String rootName = root.getName().getFullyQualifiedName();

		// remove annotations from part declaration
		ScreenEntityBuilder.INSTANCE.removeScreenPartTopLevelAnnotations(listRewriter, rootName,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.REMOVE }));
		// add new annotations to part declaration
		ScreenEntityBuilder.INSTANCE.addScreenPartTopLevelAnnotations(ast, cu, rewriter, listRewriter, rootName,
				ScreenEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.ADD }));

		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.MARKER_ANNOTATION) {
				ScreenEntityBuilder.INSTANCE.replaceMarkerAnnotationOnNormalAnnnotation(ast, listRewriter, (MarkerAnnotation)node);
			}
		}
		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
				NormalAnnotation annotation = (NormalAnnotation)node;
				String fullyQualifiedName = annotation.getTypeName().getFullyQualifiedName();
				if (ScreenPart.class.getSimpleName().equals(fullyQualifiedName)) {
					// @ScreenPart
					ScreenEntityBuilder.INSTANCE.processScreenPartAnnotation(ast, cu, rewriter, listRewriter, annotation,
							rootName, ScreenEntityUtils.getActionList(entity, ScreenPartAction.class));
				} else if (PartPosition.class.getSimpleName().equals(fullyQualifiedName)) {
					// @PartPosition
					ScreenEntityBuilder.INSTANCE.processPartPositionAnnotation(ast, cu, rewriter, listRewriter, annotation,
							rootName, ScreenEntityUtils.getActionList(entity, PartPositionAction.class));
				}
			}
		}
	}

	/**
	 * @param ast
	 * @param cu
	 * @param rewriter
	 * @param root
	 * @param entity
	 */
	private static void processScreenPartInnerAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, ScreenEntity entity) {

		// process fields that related to screen part
		processScreenFields(ast, cu, rewriter, root, entity);
	}

}
