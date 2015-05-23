package com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.enums.IEnumFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDbColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaDbEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaJoinColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaListFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa.JpaManyToOneAction;
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
import org.eclipse.jdt.core.dom.EnumDeclaration;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
		processEntityTopLevelAnnotations(ast, cu, rewriter, root, (JpaEntity) entity);
		// process annotations that located inside root:
		// @Column, @OneToMany, @Id, @DbColumn, @ManyToOne, @JoinColumn
		processEntityInnerAnnotations(ast, cu, rewriter, root, (JpaEntity) entity);

		// add serialVersionUID
		// processSerializableDeclaration(ast, cu, rewriter, root);
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
				Annotation annotation = (Annotation) node;
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
							(NormalAnnotation) annotation, JpaEntityUtils.getActionList(entity, JpaActionsAction.class));
				}
			}
		}
	}

	private static void processEntityInnerAnnotations(AST ast, CompilationUnit cu, ASTRewrite rewriter,
			AbstractTypeDeclaration root, JpaEntity entity) {

		ListRewrite listRewriter = rewriter.getListRewrite(root, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

		// before handling fields we should add enum declarations for enum field
		JpaEntityBuilder.INSTANCE.createNewEntityInnerDeclarations(ast, cu, rewriter, listRewriter,
				JpaEntityUtils.<AbstractAction> getActionList(entity, ASTNode.ENUM_DECLARATION,
						new ActionType[] { ActionType.ADD }));

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
		// try to remove enum declaration
		JpaEntityBuilder.INSTANCE.removeEnumDeclaration(listRewriter, root.getName().getFullyQualifiedName(),
				JpaEntityUtils.<AbstractAction> getActionList(entity, ASTNode.ENUM_DECLARATION,
						new ActionType[] { ActionType.REMOVE }));
		// add new fields
		JpaEntityBuilder.INSTANCE.createNewFields(ast, cu, rewriter, listRewriter, rootName,
				JpaEntityUtils.<AbstractAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
						new ActionType[] { ActionType.ADD }));

		// before modifying jpa field name we should change field type if appropriate actions are exist
		List<ASTNode> nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration) node;
				// handle field parameterized type
				JpaEntityBuilder.INSTANCE.processJpaFieldType(
						ast,
						cu,
						rewriter,
						listRewriter,
						field,
						rootName,
						JpaEntityUtils.<JpaFieldAction> getActionList(entity, ASTNode.FIELD_DECLARATION
								| ASTNode.PARAMETERIZED_TYPE, new ActionType[] { ActionType.MODIFY }));
				// handle field type (relates to ManyToOne)
				JpaEntityBuilder.INSTANCE.processJpaFieldType(ast, cu, rewriter, listRewriter, field, rootName,
						JpaEntityUtils.<JpaFieldAction> getActionList(entity, ASTNode.TYPE_DECLARATION,
								new ActionType[] { ActionType.MODIFY }));
			}
		}
		// before modifying jpa field annotation properties we should change field name if appropriate actions are exist
		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration) node;
				// handle field declarations (in our case it is fieldName)
				JpaEntityBuilder.INSTANCE.processJpaFieldDeclaration(ast, cu, rewriter, listRewriter, field, rootName,
						JpaEntityUtils.<JpaFieldAction> getActionList(entity, ASTNode.FIELD_DECLARATION,
								new ActionType[] { ActionType.MODIFY }));
			}
		}

		// add/remove field annotations, such as @Column, @OneToMany, @Id, @ManyToOne, @JoinColumn, @GeneratedValue
		nodeList = listRewriter.getRewrittenList();
		for (ASTNode node : nodeList) {
			if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration) node;
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
				FieldDeclaration field = (FieldDeclaration) node;
				// handle field annotations
				ListRewrite fieldListRewrite = rewriter.getListRewrite(field, FieldDeclaration.MODIFIERS2_PROPERTY);
				List<ASTNode> fieldNodeList = fieldListRewrite.getRewrittenList();
				for (ASTNode astNode : fieldNodeList) {
					if (astNode.getNodeType() == ASTNode.NORMAL_ANNOTATION) {
						NormalAnnotation fieldAnnotation = (NormalAnnotation) astNode;
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
						} else if (fullyQualifiedName.equals(ManyToOne.class.getSimpleName())) {
							JpaEntityBuilder.INSTANCE.processJpaFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, JpaEntityUtils.getActionList(entity, JpaManyToOneAction.class));
						} else if (fullyQualifiedName.equals(JoinColumn.class.getSimpleName())) {
							JpaEntityBuilder.INSTANCE.processJpaFieldAnnotation(ast, cu, rewriter, fieldListRewrite, field,
									fieldAnnotation, rootName, JpaEntityUtils.getActionList(entity, JpaJoinColumnAction.class));
						}
					}
				}
			} else if (node.getNodeType() == ASTNode.ENUM_DECLARATION) {
				EnumDeclaration enumDeclaration = (EnumDeclaration) node;
				JpaEntityBuilder.INSTANCE.processEnumDeclaration(ast, rewriter, enumDeclaration,
						JpaEntityUtils.getActionList(entity, IEnumFieldAction.class));
			}
		}
	}

	// @SuppressWarnings("unchecked")
	// private static void processSerializableDeclaration(AST ast, CompilationUnit cu, ASTRewrite rewriter,
	// AbstractTypeDeclaration root) {
	// boolean serializableInterfaceExist = false;
	// boolean serializableIdExist = false;
	//
	// ListRewrite listRewrite = rewriter.getListRewrite(root, TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY);
	// List<ASTNode> nodeList = listRewrite.getRewrittenList();
	// for (ASTNode node : nodeList) {
	// if (node.getNodeType() == ASTNode.SIMPLE_TYPE) {
	// SimpleType simpleType = (SimpleType)node;
	// if (StringUtils.equals(simpleType.getName().getFullyQualifiedName(), Serializable.class.getSimpleName())) {
	// serializableInterfaceExist = true;
	// break;
	// }
	// }
	// }
	//
	// if (!serializableInterfaceExist) {
	// // add "implements Serializable"
	// SimpleType simpleType = ast.newSimpleType(ast.newSimpleName(Serializable.class.getSimpleName()));
	// listRewrite.insertLast(simpleType, null);
	// ASTUtils.addImport(ast, cu, rewriter, Serializable.class);
	// }
	//
	// listRewrite = rewriter.getListRewrite(root, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
	// nodeList = listRewrite.getRewrittenList();
	// for (ASTNode node : nodeList) {
	// if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
	// FieldDeclaration field = (FieldDeclaration)node;
	// // get field name
	// String fieldName = "";
	// List<VariableDeclarationFragment> fragments = field.fragments();
	// if (fragments.size() > 0) {
	// fieldName = fragments.get(0).getName().getFullyQualifiedName();
	// }
	// // compare
	// if (StringUtils.equals(fieldName, "serialVersionUID")) {
	// serializableIdExist = true;
	// break;
	// }
	// }
	// }
	// if (!serializableIdExist) {
	// // add "private static final long serialVersionUID = 1L;"
	// VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
	// fragment.setName(ast.newSimpleName("serialVersionUID"));
	//
	// NumberLiteral numberLiteral = ast.newNumberLiteral("1L");
	// fragment.setInitializer(numberLiteral);
	//
	// FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
	// fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.LONG));
	// fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
	// fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
	// fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.FINAL_KEYWORD));
	//
	// listRewrite.insertFirst(fieldDeclaration, null);
	// }
	// }

}
