package org.openlegacy.designtime.generators;

import org.openlegacy.utils.JavaParserUtil;
import org.openlegacy.utils.PropertyUtil;
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
public class ScreenPojoCodeModelImpl implements ScreenPojoCodeModel {

	private String className;
	private String packageName;
	private ClassOrInterfaceDeclaration mainType;
	private Map<String, Field> fields = new TreeMap<String, Field>();

	private boolean enabled;
	private boolean supportTerminalData;

	public ScreenPojoCodeModelImpl(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration type, String className) {

		mainType = type;
		this.packageName = compilationUnit.getPackage().getName().toString();
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
							if (annotationExpr.getName().getName().equals(AnnotationConstants.FIELD_MAPPING_ANNOTATION)) {
								handleFieldMappingAnnotation(fieldDeclaration, annotationExpr, field);
							}
						}
					}
					fields.put(fieldName, field);
				}
			}
		}

		checkHasGetterAndSetter(members);
	}

	private static void handleFieldMappingAnnotation(FieldDeclaration fieldDeclaration, AnnotationExpr annotationExpr, Field field) {
		String annotationValue = JavaParserUtil.findAnnotationAttribute(annotationExpr, AnnotationConstants.EDITABLE);
		if (AnnotationConstants.TRUE.equals(annotationValue)) {
			field.setEditable(true);
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
					|| annotationName.equals(AnnotationConstants.SCREEN_PART_ANNOTATION)
					|| annotationName.equals(AnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
				enabled = true;
				checkSupportTerminalData(annotationExpr);
			}
		}
	}

	private void checkSupportTerminalData(AnnotationExpr annotationExpr) {
		if (annotationExpr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;
			String supportTerminalDataString = findAnnotationAttribute(AnnotationConstants.SUPPORT_TERMINAL_DATA,
					normalAnnotationExpr.getPairs());
			supportTerminalData = supportTerminalDataString != null && supportTerminalDataString.equals(AnnotationConstants.TRUE);
		}
	}

	private static String findAnnotationAttribute(String annotationName, List<MemberValuePair> pairs) {
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

	public String getFormattedClassName() {
		return className.replace(".", "");
	}

	public String getPackageName() {
		return packageName;
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
		private boolean hasGetter;
		private boolean hasSetter;
		private boolean hasGetterField;
		private String type;
		private boolean editable;
		private boolean primitiveType;

		public Field(String name, String type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
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

	}
}
