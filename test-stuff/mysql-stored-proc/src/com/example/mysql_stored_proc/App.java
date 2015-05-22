package com.example.mysql_stored_proc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

	static String jdbc2javaTypes(String jdbcType) {

		String javaType = "Object";
		if (jdbcType.equalsIgnoreCase("CHAR")) {
			javaType = "String";
		} else if (jdbcType.equalsIgnoreCase("VARCHAR")) {
			javaType = "String";
		} else if (jdbcType.equalsIgnoreCase("LONGVARCHAR")) {
			javaType = "String";
		} else if (jdbcType.equalsIgnoreCase("NUMERIC")
				|| jdbcType.equalsIgnoreCase("DECIMAL")) {
			javaType = "java.math.BigDecimal";
		} else if (jdbcType.equalsIgnoreCase("BIT")) {
			javaType = "boolean";
		} else if (jdbcType.equalsIgnoreCase("BOOLEAN")) {
			javaType = "boolean";
		} else if (jdbcType.equalsIgnoreCase("TINYINT")) {
			javaType = "byte";
		} else if (jdbcType.equalsIgnoreCase("SMALLINT")) {
			javaType = "short";
		} else if (jdbcType.equalsIgnoreCase("INTEGER")
				|| jdbcType.equalsIgnoreCase("INT")) {
			javaType = "int";
		} else if (jdbcType.equalsIgnoreCase("BIGINT")) {
			javaType = "long";
		} else if (jdbcType.equalsIgnoreCase("REAL")) {
			javaType = "float";
		} else if (jdbcType.equalsIgnoreCase("FLOAT")) {
			javaType = "double";
		} else if (jdbcType.equalsIgnoreCase("DOUBLE")) {
			javaType = "double";
		} else if (jdbcType.equalsIgnoreCase("BINARY")) {
			javaType = "byte[]";
		} else if (jdbcType.equalsIgnoreCase("VARBINARY")) {
			javaType = "byte[]";
		} else if (jdbcType.equalsIgnoreCase("LONGVARBINARY")) {
			javaType = "byte[]";
		} else if (jdbcType.equalsIgnoreCase("DATE")) {
			javaType = "java.sql.Date";
		} else if (jdbcType.equalsIgnoreCase("TIME")) {
			javaType = "java.sql.Time";
		} else if (jdbcType.equalsIgnoreCase("TIMESTAMP")) {
			javaType = "java.sql.Timestamp";
		} else if (jdbcType.equalsIgnoreCase("CLOB")) {
			javaType = "Clob";
		} else if (jdbcType.equalsIgnoreCase("BLOB")) {
			javaType = "Blob";
		} else if (jdbcType.equalsIgnoreCase("ARRAY")) {
			javaType = "Array";
		} else if (jdbcType.equalsIgnoreCase("STRUCT")) {
			javaType = "Struct";
		} else if (jdbcType.equalsIgnoreCase("REF")) {
			javaType = "Ref";
		} else if (jdbcType.equalsIgnoreCase("DATALINK")) {
			javaType = "java.net.URL";
		}

		return javaType;
	}

	public static void main(String[] args) {
		// buildClassFromStoredProcSource();

		DoStuffWithTwoNumbers e = new DoStuffWithTwoNumbers();
		e.setN1(10);
		e.setN2(20);

		e = DoStuffWithTwoNumbers.invoke(e);

		System.out.println("summ is " + e.getSumm());
		System.out.println("sub is  " + e.getSub());
		System.out.println("mul is  " + e.getMul());
	}

	private static void buildClassFromStoredProcSource() {
		ClassBuilder cb = new ClassBuilder();

		Path path = Paths.get("sql", "proc.sql").toAbsolutePath();
		try {
			String procBody = new String(Files.readAllBytes(path));

			procBody = procBody.replace("\n", " ").replaceAll("\\s+", " ")
					.replaceAll("\\s*,\\s*", ", ")
					.replaceAll("\\s*\\(\\s+", "(")
					.replaceAll("\\s+\\)\\s*", ")");

			System.out.println(procBody);

			parseProcSource(cb, procBody);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			CallableStatement cs = DbUtil.getConnection().prepareCall(
					"{call doStuffWithTwoNumbers(?, ?)}");

			cs.setInt(1, 1);
			cs.setInt(2, 2);

			ResultSet resultSet = cs.executeQuery();

			ResultSetMetaData meta = resultSet.getMetaData();

			while (resultSet.next()) {
				System.out.println("total columns - " + meta.getColumnCount());

				for (int col = 1; col <= meta.getColumnCount(); ++col) {
					cb.addParam(jdbc2javaTypes(meta.getColumnTypeName(col)),
							meta.getColumnName(col));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// cb.print("com.example");
	}

	private static void parseProcSource(ClassBuilder cb, String procBody) {
		String paramRegex = "(in|out|inout) (\\w+|`\\w+`) (\\w+)(\\(\\d+(\\s*,\\s*\\d+)?\\))?";
		String headerRegex = "(`(\\w+)`|(\\w+))\\s*\\(((" + paramRegex + ")(, "
				+ paramRegex + ")*)\\)";

		Pattern pattern = Pattern.compile(headerRegex);
		Matcher matcher = pattern.matcher(procBody);

		if (matcher.find()) {
			if (matcher.group(2) != null) {
				cb.setName(matcher.group(2));
			} else {
				cb.setName(matcher.group(3));
			}

			String[] params = matcher.group(4).split("\\s*,\\s*");

			pattern = Pattern.compile(paramRegex);

			for (String param : params) {
				matcher = pattern.matcher(param);
				if (matcher.find()) {
					String paramName = matcher.group(2).replace("`", "");
					String paramType = jdbc2javaTypes(matcher.group(3));

					cb.addParam(paramType, paramName);
				}
			}
		}
	}
}
