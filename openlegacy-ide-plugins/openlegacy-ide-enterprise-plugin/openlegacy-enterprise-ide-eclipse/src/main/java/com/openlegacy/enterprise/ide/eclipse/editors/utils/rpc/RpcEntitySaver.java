package com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcNumericFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartListAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.AbstractEntitySaver;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntitySaver extends AbstractEntitySaver {

	public static final RpcEntitySaver INSTANCE = new RpcEntitySaver();

	private RpcEntitySaver() {}

	@Override
	protected void doSave(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root, AbstractEntity entity) {
		// process top level class annotations: @RpcEntity, @RpcActions
		processEntityTopLevelAnnotations(ast, cu, rewriter, root, (RpcEntity)entity);
		// process annotations that located inside root:
		// @RpcPart, @RpcField (and related)
		processEntityInnerAnnotations(ast, cu, rewriter, root, (RpcEntity)entity);
	}

	@SuppressWarnings("unchecked")
	private static void processEntityTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, RpcEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.MODIFIERS2_PROPERTY);

		// add new annotations to entity
		RpcEntityBuilder.INSTANCE.addEntityTopLevelAnnotations(ast, cu, rewriter, listRewriter,
				RpcEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.ADD }), null);

		// remove annotations from entity
		RpcEntityBuilder.INSTANCE.removeEntityTopLevelAnnotations(listRewriter, RpcEntityUtils.<AbstractAction> getActionList(
				entity, ASTNode.NORMAL_ANNOTATION, new ActionType[] { ActionType.REMOVE }), null);

		// handle existing annotations
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
				NormalAnnotation annotation = (NormalAnnotation)node;
				String fullyQualifiedName = annotation.getTypeName().getFullyQualifiedName();
				if (org.openlegacy.annotations.rpc.RpcEntity.class.getSimpleName().equals(fullyQualifiedName)) {
					// @RpcEntity
					RpcEntityBuilder.INSTANCE.processRpcEntityAnnotation(ast, cu, rewriter, listRewriter, annotation,
							RpcEntityUtils.getActionList(entity, RpcEntityAction.class));
				} else if (RpcActions.class.getSimpleName().equals(fullyQualifiedName)) {
					// @RpcActions
					RpcEntityBuilder.INSTANCE.processRpcActionsAnnotation(ast, cu, rewriter, listRewriter, annotation,
							RpcEntityUtils.getActionList(entity, RpcActionsAction.class));
				} else if (RpcNavigation.class.getSimpleName().equals(fullyQualifiedName)) {
					// @RpcNavigation
					RpcEntityBuilder.INSTANCE.processRpcNavigationAnnotation(ast, cu, rewriter, listRewriter, annotation,
							RpcEntityUtils.getActionList(entity, RpcNavigationAction.class));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void processEntityInnerAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, RpcEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

		// process rpc fields that relate to root
		processRpcFields(ast, cu, rewriter, root, entity);

		// before creating new declarations we should try to remove declarations with the same names
		RpcEntityBuilder.INSTANCE.removeEntityInnerDeclarations(listRewriter, RpcEntityUtils.<AbstractAction> getActionList(
				entity, ASTNode.TYPE_DECLARATION, new ActionType[] { ActionType.REMOVE }));

		// add new parts
		RpcEntityBuilder.INSTANCE.createNewEntityInnerDeclarations(ast, cu, rewriter, listRewriter,
				RpcEntityUtils.<AbstractAction> getActionList(entity, ASTNode.TYPE_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// try to rename parts
		RpcEntityBuilder.INSTANCE.processEntityInnerDeclarations(ast, cu, rewriter, listRewriter,
				RpcEntityUtils.<AbstractAction> getActionList(entity, ASTNode.TYPE_DECLARATION | ASTNode.SIMPLE_NAME,
						new ActionType[] { ActionType.MODIFY }));

		// process parts
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
						if (fullyQualifiedName.equals(RpcPart.class.getSimpleName())) {
							// @RpcPart
							processRpcPart(ast, cu, rewriter, type, entity);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void processRpcFields(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root,
			RpcEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		String rootName = root.getName().getFullyQualifiedName();

		// before create new fields, we need to remove fields
		// remove fields
		RpcEntityBuilder.INSTANCE.removeFields(listRewriter, rootName, RpcEntityUtils.<AbstractAction> getActionList(entity,
				ASTNode.FIELD_DECLARATION, new ActionType[] { ActionType.REMOVE }));

		// add new fields
		RpcEntityBuilder.INSTANCE.createNewFields(ast, cu, rewriter, listRewriter, rootName,
				RpcEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// before create new fields, we need to remove fields
		// remove fields related to RpcPart
		RpcEntityBuilder.INSTANCE.removeFieldsRelatedToPart(listRewriter, rootName, RpcEntityUtils.<RpcPartAction> getActionList(
				entity, ASTNode.FIELD_DECLARATION, new ActionType[] { ActionType.REMOVE }));

		// add new simple fields, that related to RpcPart
		RpcEntityBuilder.INSTANCE.createNewFieldsRelatedToPart(ast, cu, rewriter, listRewriter, rootName,
				RpcEntityUtils.<RpcPartAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// before modifying rpc field annotation properties we should change field name and type if appropriate actions are exist
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				// handle field annotations
				ListRewrite fieldListRewrite = rewriter.getListRewrite(node, FieldDeclaration.MODIFIERS2_PROPERTY);
				List<ASTNode> fieldNodeList = fieldListRewrite.getRewrittenList();
				for (ASTNode astNode : fieldNodeList) {
					// replace marker annotations, for example: @RpcNumericField -> @RpcNumericField()
					if (astNode.getNodeType() == ASTNode.MARKER_ANNOTATION) {
						RpcEntityBuilder.INSTANCE.replaceMarkerAnnotationOnNormalAnnnotation(ast, fieldListRewrite,
								(MarkerAnnotation)astNode);
					}
				}
				FieldDeclaration field = (FieldDeclaration)node;
				// handle field declarations (in our case it is fieldName)
				RpcEntityBuilder.INSTANCE.processRpcFieldDeclaration(ast, cu, rewriter, listRewriter, field, rootName,
						RpcEntityUtils.<RpcFieldAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
								new ActionType[] { ActionType.MODIFY }));
				// if user has changed part class name we need to change type of related field
				RpcEntityBuilder.INSTANCE.processRpcPartFieldDeclaration(ast, cu, rewriter, listRewriter, field, rootName,
						RpcEntityUtils.<RpcPartAction> getActionList(entity, ASTNode.TYPE_DECLARATION | ASTNode.SIMPLE_NAME,
								new ActionType[] { ActionType.MODIFY }));
				// if user changed count of RpcPart we need to modify type of related field
				RpcEntityBuilder.INSTANCE.processRpcPartFieldDeclaration(ast, cu, rewriter, listRewriter, field, rootName,
						RpcEntityUtils.getActionList(entity, RpcPartListAction.class));
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
						if (fullyQualifiedName.equals(RpcField.class.getSimpleName())) {
							RpcEntityBuilder.INSTANCE.processRpcFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, RpcEntityUtils.getActionList(entity, RpcFieldAction.class));
						} else if (fullyQualifiedName.equals(RpcBooleanField.class.getSimpleName())) {
							RpcEntityBuilder.INSTANCE.processRpcFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, RpcEntityUtils.getActionList(entity, RpcBooleanFieldAction.class));
						} else if (fullyQualifiedName.equals(RpcNumericField.class.getSimpleName())) {
							RpcEntityBuilder.INSTANCE.processRpcFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, RpcEntityUtils.getActionList(entity, RpcNumericFieldAction.class));
						} else if (fullyQualifiedName.equals(RpcPartList.class.getSimpleName())) {
							RpcEntityBuilder.INSTANCE.processRpcPartListAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, RpcEntityUtils.getActionList(entity, RpcPartListAction.class));
						}
					}
				}
			}
		}
	}

	private static void processRpcPart(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root,
			RpcEntity entity) {

		// Note: logic looks like we are processing root in general

		// process top level screen part annotations: @RpcPart, @RpcActions
		processRpcPartTopLevelAnnotations(ast, cu, rewriter, root, entity);

		// process annotations that located inside part: @RpcField
		processRpcPartInnerAnnotations(ast, cu, rewriter, root, entity);
	}

	@SuppressWarnings("unchecked")
	private static void processRpcPartTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, RpcEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.MODIFIERS2_PROPERTY);
		String rootName = root.getName().getFullyQualifiedName();

		// add new annotations to part
		RpcEntityBuilder.INSTANCE.addEntityTopLevelAnnotations(ast, cu, rewriter, listRewriter,
				RpcEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.ADD }), RpcPartModel.class);

		// remove annotations from part
		RpcEntityBuilder.INSTANCE.removeEntityTopLevelAnnotations(listRewriter, RpcEntityUtils.<AbstractAction> getActionList(
				entity, ASTNode.NORMAL_ANNOTATION, new ActionType[] { ActionType.REMOVE }), RpcPartModel.class);

		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.MARKER_ANNOTATION) {
				RpcEntityBuilder.INSTANCE.replaceMarkerAnnotationOnNormalAnnnotation(ast, listRewriter, (MarkerAnnotation)node);
			}
		}
		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
				NormalAnnotation annotation = (NormalAnnotation)node;
				String fullyQualifiedName = annotation.getTypeName().getFullyQualifiedName();
				if (RpcPart.class.getSimpleName().equals(fullyQualifiedName)) {
					// @RpcPart
					RpcEntityBuilder.INSTANCE.processRpcPartAnnotation(ast, cu, rewriter, listRewriter, annotation, rootName,
							RpcEntityUtils.getActionList(entity, RpcPartAction.class));
				} else if (RpcActions.class.getSimpleName().equals(fullyQualifiedName)) {
					// @RpcActions
					RpcEntityBuilder.INSTANCE.processRpcActionsAnnotation(ast, cu, rewriter, listRewriter, annotation,
							RpcEntityUtils.getActionList(entity, RpcPartActionsAction.class));
				}
			}
		}
	}

	private static void processRpcPartInnerAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, RpcEntity entity) {

		// process fields that related to rpc part
		processRpcFields(ast, cu, rewriter, root, entity);
	}
}
