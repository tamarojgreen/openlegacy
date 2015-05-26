package com.openlegacy.enterprise.ide.eclipse.editors.utils;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.openlegacy.utils.StringUtil;

/**
 * @author Ivan Bort
 * 
 */
public abstract class ASTUtils {

	protected abstract <T> NormalAnnotation getAnnotationForArrayLiteral(AST ast, CompilationUnit cu, ASTRewrite rewriter, T item);

	// ************************ general **********************

	@SuppressWarnings("unchecked")
	public static void addImport(AST ast, CompilationUnit cu, ASTRewrite rewriter, Class<?> clazz) {
		List<ImportDeclaration> imports = cu.imports();
		boolean exist = false;
		for (ImportDeclaration impord : imports) {
			if (impord.getName().getFullyQualifiedName().equals(clazz.getCanonicalName())) {
				exist = true;
				break;
			}
		}
		if (!exist) {
			ListRewrite lrw = rewriter.getListRewrite(cu, CompilationUnit.IMPORTS_PROPERTY);
			ImportDeclaration declaration = ast.newImportDeclaration();
			declaration.setName(ast.newName(clazz.getCanonicalName()));
			lrw.insertLast(declaration, null);
		}
	}

	public static CompilationUnit createParser(ICompilationUnit javaInput) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(javaInput);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(false);

		return (CompilationUnit)parser.createAST(null);
	}

	@SuppressWarnings("unchecked")
	public static Type cloneType(AST ast, Type type) {
		if (type.isPrimitiveType()) {
			return ast.newPrimitiveType(((PrimitiveType)type).getPrimitiveTypeCode());
		}
		if (type.isSimpleType()) {
			SimpleType simpleType = (SimpleType)type;
			return ast.newSimpleType(ast.newSimpleName(simpleType.getName().getFullyQualifiedName()));
		}
		if (type.isArrayType()) {
			ArrayType arrayType = (ArrayType)type;
			Type cloneType = cloneType(ast, arrayType.getComponentType());
			return ast.newArrayType(cloneType);
		}
		if (type.isParameterizedType()) {
			ParameterizedType parameterizedType = (ParameterizedType)type;
			List<Type> typeArguments = parameterizedType.typeArguments();
			Type cloneType = cloneType(ast, parameterizedType.getType());
			ParameterizedType newType = ast.newParameterizedType(cloneType);
			for (Type typeArg : typeArguments) {
				newType.typeArguments().add(cloneType(ast, typeArg));
			}
			return newType;
		}
		return ast.newPrimitiveType(PrimitiveType.INT);
	}

	// ****************************** literals ********************************
	public BooleanLiteral createBooleanLiteral(AST ast, boolean value) {
		return ast.newBooleanLiteral(value);
	}

	public StringLiteral createStringLiteral(AST ast, String value) {
		StringLiteral literal = ast.newStringLiteral();
		literal.setLiteralValue(value);
		return literal;
	}

	public NumberLiteral createNumberLiteral(AST ast, Integer value) {
		return ast.newNumberLiteral(value.toString());
	}

	public TypeLiteral createTypeLiteral(AST ast, Class<?> clazz) {
		TypeLiteral literal = ast.newTypeLiteral();
		literal.setType(ast.newSimpleType(ast.newName(clazz.getSimpleName())));
		return literal;
	}

	public QualifiedName createEnumLiteral(AST ast, Enum<?> value) {
		return ast.newQualifiedName(ast.newSimpleName(value.getClass().getSimpleName()), ast.newSimpleName(value.toString()));
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayInitializer createArrayLiteral(AST ast, CompilationUnit cu, ASTRewrite rewriter, List<T> obj) {
		ArrayInitializer expr = ast.newArrayInitializer();
		for (T item : obj) {
			NormalAnnotation annotation = getAnnotationForArrayLiteral(ast, cu, rewriter, item);
			if (annotation != null) {
				expr.expressions().add(annotation);
			}
		}
		return expr;
	}

	public NumberLiteral createDoubleLiteral(AST ast, Double value) {
		return ast.newNumberLiteral(value.toString());
	}

	@SuppressWarnings("unchecked")
	public ArrayInitializer createStringArrayLiteral(AST ast, List<String> value) {
		ArrayInitializer arrayInitializer = ast.newArrayInitializer();
		for (String str : value) {
			if (!StringUtil.isEmpty(str)) {
				arrayInitializer.expressions().add(createStringLiteral(ast, str));
			}
		}
		return arrayInitializer;
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayInitializer createArrayLiteral(AST ast, T[] obj) {
		ArrayInitializer expr = ast.newArrayInitializer();
		for (Object object : obj) {
			expr.expressions().add(createEnumLiteral(ast, (Enum<?>)object));
		}
		return expr;
	}

	// ****************************** pairs ********************************
	public MemberValuePair createBooleanPair(AST ast, String key, boolean value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(key));
		pair.setValue(createBooleanLiteral(ast, value));
		return pair;
	}

	public MemberValuePair createStringPair(AST ast, String name, String value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(name));
		pair.setValue(createStringLiteral(ast, value));
		return pair;
	}

	public MemberValuePair createNumberPair(AST ast, String name, Integer value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(name));
		pair.setValue(createNumberLiteral(ast, value));
		return pair;
	}

	public MemberValuePair createTypePair(AST ast, String name, Class<?> value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(name));
		pair.setValue(createTypeLiteral(ast, value));
		return pair;
	}

	public MemberValuePair createEnumPair(AST ast, String name, Enum<?> value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(name));
		pair.setValue(createEnumLiteral(ast, value));
		return pair;
	}

	public <T> MemberValuePair createArrayPair(AST ast, CompilationUnit cu, ASTRewrite rewriter, String key, List<T> obj) {
		ArrayInitializer literal = createArrayLiteral(ast, cu, rewriter, obj);
		if (literal.expressions().isEmpty()) {
			return null;
		}
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(key));
		pair.setValue(literal);
		return pair;
	}

	public MemberValuePair createDoublePair(AST ast, String name, Double value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(name));
		pair.setValue(createDoubleLiteral(ast, value));
		return pair;
	}

	public MemberValuePair createStringArrayPair(AST ast, String name, List<String> value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(name));
		pair.setValue(createStringArrayLiteral(ast, value));
		return pair;
	}

	public <T> MemberValuePair createArrayPair(AST ast, String name, T[] value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(ast.newSimpleName(name));
		pair.setValue(createArrayLiteral(ast, value));
		return pair;
	}
}
