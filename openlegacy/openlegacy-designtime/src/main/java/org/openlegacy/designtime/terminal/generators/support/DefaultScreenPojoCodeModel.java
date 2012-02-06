package org.openlegacy.designtime.terminal.generators.support;

import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.TypesUtil;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * 
 */
public class DefaultScreenPojoCodeModel implements ScreenPojoCodeModel {

	private String className;
	private String packageName;
	private ClassOrInterfaceDeclaration mainType;
	private Map<String, Field> fields = new TreeMap<String, Field>();

	private boolean enabled;
	private boolean supportTerminalData;
	private boolean superClass = false;
	private String displayName;
	private String entityName;
	private int startRow;
	private int endRow;

	public DefaultScreenPojoCodeModel(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration type, String className) {

		mainType = type;
		if (compilationUnit.getPackage() != null) {
			this.packageName = compilationUnit.getPackage().getName().toString();
		}

		this.className = className;
		calculateClassProperties();

		calculateFieldsProperties();

	}

	private void calculateFieldsProperties() {
		List<BodyDeclaration> members = mainType.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof FieldDeclaration) {
				FieldDeclaration fieldDeclaration = (FieldDeclaration)bodyDeclaration;
				List<VariableDeclarator> variables = fieldDeclaration.getVariables();
				if (variables.size() > 0) {
					List<AnnotationExpr> fieldAnnotations = fieldDeclaration.getAnnotations();
					String fieldName = variables.get(0).getId().getName();
					Field field = new Field(fieldName, fieldDeclaration.getType().toString());
					field.setPrimitiveType(TypesUtil.isPrimitive(fieldDeclaration.getType().toString()));
					if (fieldAnnotations != null && fieldAnnotations.size() > 0) {
						for (AnnotationExpr annotationExpr : fieldAnnotations) {
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									AnnotationConstants.SCREEN_FIELD_ANNOTATION, AnnotationConstants.SCREEN_COLUMN_ANNOTATION)) {
								handleScreenFieldOrColumnAnnotation(fieldDeclaration, annotationExpr, field);
							}
						}
					}
					fields.put(fieldName, field);
				}
			}
		}

		checkHasGetterAndSetter(members);
	}

	private static void handleScreenFieldOrColumnAnnotation(FieldDeclaration fieldDeclaration, AnnotationExpr annotationExpr,
			Field field) {
		String editableValue = JavaParserUtil.getAnnotationValue(annotationExpr, AnnotationConstants.EDITABLE);
		String rowValue = JavaParserUtil.getAnnotationValue(annotationExpr, AnnotationConstants.ROW);
		String columnValue = JavaParserUtil.getAnnotationValue(annotationExpr, AnnotationConstants.COLUMN);
		String displayNameValue = JavaParserUtil.getAnnotationValue(annotationExpr, AnnotationConstants.DISPLAY_NAME);
		String startColumnValue = JavaParserUtil.getAnnotationValue(annotationExpr, AnnotationConstants.START_COLUMN);
		String endColumnValue = JavaParserUtil.getAnnotationValue(annotationExpr, AnnotationConstants.END_COLUMN);

		if (AnnotationConstants.TRUE.equals(editableValue)) {
			field.setEditable(true);
		}
		if (rowValue != null) {
			field.setRow(Integer.valueOf(rowValue));
		}
		if (columnValue != null) {
			field.setColumn(Integer.valueOf(columnValue));
		}
		String displayName = !StringUtil.isEmpty(displayNameValue) ? displayNameValue : StringUtil.toDisplayName(field.getName());
		field.setDisplayName(displayName);

		if (startColumnValue != null) {
			field.setColumn(Integer.valueOf(startColumnValue));
		}
		if (endColumnValue != null) {
			field.setEndColumn(Integer.valueOf(endColumnValue));
		}
	}

	private void checkHasGetterAndSetter(List<BodyDeclaration> members) {
		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration)bodyDeclaration;
				String methodName = methodDeclaration.getName();

				String propertyNameIfGetter = PropertyUtil.getPropertyNameIfGetter(methodName);
				if (propertyNameIfGetter != null) {
					Field field = fields.get(propertyNameIfGetter);
					if (field != null) {
						field.setHasGetter(true);
					}
				}

				String propertyNameIfSetter = PropertyUtil.getPropertyNameIfSetter(methodName);
				if (propertyNameIfSetter != null) {
					Field field = fields.get(propertyNameIfSetter);
					if (field != null) {
						field.setHasSetter(true);
					}
				}

				String propertyFieldNameIfGetter = PropertyUtil.getPropertyNameIfGetter(methodName);
				if (propertyFieldNameIfGetter != null) {
					if (propertyFieldNameIfGetter.endsWith(AnnotationConstants.FIELD_SUFFIX)) {
						String simplePropertyName = propertyFieldNameIfGetter.substring(0, propertyFieldNameIfGetter.length()
								- AnnotationConstants.FIELD_SUFFIX.length());
						Field field = fields.get(simplePropertyName);
						if (field != null) {
							field.setHasGetterField(true);
						}
					}
				}

			}
		}
	}

	private void calculateClassProperties() {
		List<AnnotationExpr> annotations = mainType.getAnnotations();
		if (annotations == null) {
			return;
		}
		for (AnnotationExpr annotationExpr : annotations) {
			String annotationName = annotationExpr.getName().getName();
			if (annotationName.equals(AnnotationConstants.SCREEN_ENTITY_ANNOTATION)
					|| annotationName.equals(AnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)
					|| annotationName.equals(AnnotationConstants.SCREEN_PART_ANNOTATION)
					|| annotationName.equals(AnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
				enabled = true;
				populateEntityAttributes(annotationExpr);
				if (annotationName.equals(AnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)) {
					superClass = true;
				}
			}
		}
	}

	private void populateEntityAttributes(AnnotationExpr annotationExpr) {
		String displayNameFromAnnotation = null;
		String entityNameFromAnnotation = null;
		String startRowFromTableAnnotation = null;
		String endRowFromTableAnnotation = null;

		if (annotationExpr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;
			String supportTerminalDataString = findAnnotationAttribute(AnnotationConstants.SUPPORT_TERMINAL_DATA,
					normalAnnotationExpr.getPairs());
			supportTerminalData = supportTerminalDataString != null && supportTerminalDataString.equals(AnnotationConstants.TRUE);

			displayNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.DISPLAY_NAME, normalAnnotationExpr.getPairs());
			entityNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.NAME, normalAnnotationExpr.getPairs());
			startRowFromTableAnnotation = findAnnotationAttribute(AnnotationConstants.START_ROW, normalAnnotationExpr.getPairs());
			endRowFromTableAnnotation = findAnnotationAttribute(AnnotationConstants.END_ROW, normalAnnotationExpr.getPairs());
		}
		displayName = displayNameFromAnnotation != null ? displayNameFromAnnotation : StringUtil.toDisplayName(getClassName());
		entityName = entityNameFromAnnotation != null ? entityNameFromAnnotation : StringUtil.toJavaFieldName(getClassName());

		if (startRowFromTableAnnotation != null) {
			startRow = Integer.parseInt(startRowFromTableAnnotation);
		}
		if (endRowFromTableAnnotation != null) {
			endRow = Integer.parseInt(endRowFromTableAnnotation);
		}
	}

	private static String findAnnotationAttribute(String annotationName, List<MemberValuePair> pairs) {
		if (pairs == null) {
			return null;
		}
		for (MemberValuePair memberValuePair : pairs) {
			if (memberValuePair.getName().equals(annotationName)) {
				return memberValuePair.getValue().toString();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#isRelevant()
	 */
	public boolean isRelevant() {
		return enabled;
	}

	public boolean isSuperClass() {
		return superClass;
	}

	public boolean isSupportTerminalData() {
		return supportTerminalData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#getClassName()
	 */
	public String getClassName() {
		return className;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getFormattedClassName() {
		return className.replace(".", "");
	}

	public String getPackageName() {
		return packageName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#getFields()
	 */
	public Collection<Field> getFields() {
		return fields.values();
	}

	public static class Field {

		private String name;
		private String displayName;
		private boolean hasGetter;
		private boolean hasSetter;
		private boolean hasGetterField;
		private String type;
		private boolean editable;
		private boolean primitiveType;
		private Integer row;
		private Integer column;
		private Integer endColumn;

		public Field(String name, String type) {
			this.name = name;
			this.type = type;
		}

		public boolean isScreenField() {
			return row != null;
		}

		public String getName() {
			return name;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public boolean isHasGetter() {
			return hasGetter;
		}

		public boolean isHasSetter() {
			return hasSetter;
		}

		public void setHasGetter(boolean hasGetter) {
			this.hasGetter = hasGetter;
		}

		public void setHasSetter(boolean hasSetter) {
			this.hasSetter = hasSetter;
		}

		public boolean isHasGetterField() {
			return hasGetterField;
		}

		public void setHasGetterField(boolean hasGetterField) {
			this.hasGetterField = hasGetterField;
		}

		public String getType() {
			return type;
		}

		public boolean isEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}

		public boolean isPrimitiveType() {
			return primitiveType;
		}

		public void setPrimitiveType(boolean primitiveType) {
			this.primitiveType = primitiveType;
		}

		public Integer getRow() {
			return row;
		}

		public void setRow(Integer row) {
			this.row = row;
		}

		public Integer getColumn() {
			return column;
		}

		public void setColumn(Integer column) {
			this.column = column;
		}

		public Integer getEndColumn() {
			return endColumn;
		}

		public void setEndColumn(Integer endColumn) {
			this.endColumn = endColumn;
		}
	}

}
