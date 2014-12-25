package com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDbColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDbEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaListFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaNavigationAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.AbstractEntitySaver;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.annotations.db.DbEntity;
import org.openlegacy.annotations.db.DbNavigation;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntitySaver extends AbstractEntitySaver {

	public static JpaEntitySaver INSTANCE = new JpaEntitySaver();

	private JpaEntitySaver() {}

	@Override
	protected void doSave(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root, AbstractEntity entity) {
		// process top level class annotations: @Entity, @Table, @DbEntity
		processEntityTopLevelAnnotations(ast, cu, rewriter, root, (JpaEntity)entity);
		// process annotations that located inside root:
		// @Column, @OneToMany, @Id, @DbColumn
		processEntityInnerAnnotations(ast, cu, rewriter, root, (JpaEntity)entity);
	}

	@SuppressWarnings("unchecked")
	private static void processEntityTopLevelAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, JpaEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.MODIFIERS2_PROPERTY);

		// add new annotations to entity
		JpaEntityBuilder.INSTANCE.addEntityTopLevelAnnotations(ast, cu, rewriter, listRewriter,
				JpaEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
						new ActionType[] { ActionType.ADD }));

		// remove annotations from entity
		JpaEntityBuilder.INSTANCE.removeEntityTopLevelAnnotations(listRewriter, JpaEntityUtils.<AbstractAction> getActionList(
				entity, ASTNode.NORMAL_ANNOTATION, new ActionType[] { ActionType.REMOVE }));

		// handle existing annotations
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.NORMAL_ANNOTATION || node.getNodeType() == ASTNode.MARKER_ANNOTATION) {
				Annotation annotation = (Annotation)node;
				String fullyQualifiedName = annotation.getTypeName().getFullyQualifiedName();
				if (Entity.class.getSimpleName().equals(fullyQualifiedName)) {
					// @Entity
					JpaEntityBuilder.INSTANCE.processJpaEntityAnnotation(ast, cu, rewriter, listRewriter, annotation,
							JpaEntityUtils.getActionList(entity, JpaEntityAction.class));
				} else if (Table.class.getSimpleName().equals(fullyQualifiedName)) {
					// @Table
					JpaEntityBuilder.INSTANCE.processJpaTableAnnotation(ast, cu, rewriter, listRewriter, annotation,
							JpaEntityUtils.getActionList(entity, JpaTableAction.class));
				} else if (DbEntity.class.getSimpleName().equals(fullyQualifiedName)) {
					// @DbEntity
					JpaEntityBuilder.INSTANCE.processJpaDbEntityAnnotation(ast, cu, rewriter, listRewriter, annotation,
							JpaEntityUtils.getActionList(entity, JpaDbEntityAction.class));
				} else if (DbNavigation.class.getSimpleName().equals(fullyQualifiedName)) {
					// @DbNavigation
					JpaEntityBuilder.INSTANCE.processJpaNavigationAnnotation(ast, cu, rewriter, listRewriter, annotation,
							JpaEntityUtils.getActionList(entity, JpaNavigationAction.class));
				} else if (DbActions.class.getSimpleName().equals(fullyQualifiedName)) {
					// @DbActions
					JpaEntityBuilder.INSTANCE.processJpaDbActionsAnnotation(ast, cu, rewriter, listRewriter,
							(NormalAnnotation)annotation, JpaEntityUtils.getActionList(entity, JpaActionsAction.class));
				}
			}
		}
	}

	private static void processEntityInnerAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, JpaEntity entity) {

		// process screen fields that relate to root
		processJpaFields(ast, cu, rewriter, root, entity);

	}

	@SuppressWarnings("unchecked")
	private static void processJpaFields(AST ast, CompilationUnit cu, ASTRewrite rewriter, AbstractTypeDeclaration root,
			JpaEntity entity) {
		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		String rootName = root.getName().getFullyQualifiedName();

		// remove fields before adding new
		JpaEntityBuilder.INSTANCE.removeFields(listRewriter, root.getName().getFullyQualifiedName(),
				JpaEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.REMOVE }));
		// add new fields
		JpaEntityBuilder.INSTANCE.createNewFields(ast, cu, rewriter, listRewriter, rootName,
				JpaEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// before modifying screen field annotation properties we should change field name if appropriate actions are exist
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration)node;
				// handle field declarations (in our case it is fieldName)
				JpaEntityBuilder.INSTANCE.processJpaFieldDeclaration(ast, cu, rewriter, listRewriter, field, rootName,
						JpaEntityUtils.<JpaFieldAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
								new ActionType[] { ActionType.MODIFY }));
				// handle field parameterized type
				JpaEntityBuilder.INSTANCE.processJpaFieldParameterizedType(
						ast,
						cu,
						rewriter,
						listRewriter,
						field,
						rootName,
						JpaEntityUtils.<JpaFieldAction> getActionList(entity, ASTNode.FIELD_DECLARATION
								| ASTNode.PARAMETERIZED_TYPE, new ActionType[] { ActionType.MODIFY }));
			}
		}

		// add/remove field annotations, such as @Column, @OneToMany, @Id
		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration)node;
				// add new annotations to field
				JpaEntityBuilder.INSTANCE.addFieldAnnotations(ast, cu, rewriter, listRewriter, field,
						JpaEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
								new ActionType[] { ActionType.ADD }));
				// remove annotations from field
				JpaEntityBuilder.INSTANCE.removeFieldAnnotations(listRewriter, field,
						JpaEntityUtils.<AbstractAction> getActionList(entity, ASTNode.NORMAL_ANNOTATION,
								new ActionType[] { ActionType.REMOVE }));
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
						if (fullyQualifiedName.equals(Column.class.getSimpleName())) {
							JpaEntityBuilder.INSTANCE.processJpaFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, JpaEntityUtils.getActionList(entity, JpaFieldAction.class));
						} else if (fullyQualifiedName.equals(OneToMany.class.getSimpleName())) {
							JpaEntityBuilder.INSTANCE.processJpaFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, JpaEntityUtils.getActionList(entity, JpaListFieldAction.class));
						} else if (fullyQualifiedName.equals(DbColumn.class.getSimpleName())) {
							JpaEntityBuilder.INSTANCE.processJpaFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, JpaEntityUtils.getActionList(entity, JpaDbColumnAction.class));
						}
					}
				}
			}
		}
	}

}
