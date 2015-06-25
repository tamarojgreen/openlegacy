package com.example.mysql_stored_proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassBuilder {

	String mName;

	private static class ParamDef {
		public String type;
		public String name;
	}

	private static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	private static String fillTemplate(String template, ParamDef paramDef) {

		Pattern pattern = Pattern.compile("#\\{(\\|?[a-z]+)\\}");
		Matcher matcher = pattern.matcher(template);

		Map<String, String> m = new HashMap<>();

		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); ++i) {

				String fieldName = "";
				boolean capitalize = false;

				String key = matcher.group(0);

				String s = matcher.group(i);
				if (s.charAt(0) == '|') {
					fieldName = s.substring(1);
					capitalize = true;
				} else {
					fieldName = s;
				}

				if (!m.containsKey(key)) {
					try {
						String value = String.valueOf(ParamDef.class.getField(
								fieldName).get(paramDef));
						if (capitalize) {
							value = capitalize(value);
						}
						m.put(key, value);
					} catch (IllegalArgumentException | IllegalAccessException
							| NoSuchFieldException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}

		for (String key : m.keySet()) {
			template = template.replace(key, m.get(key));
		}
		return template;
	}

	List<ParamDef> mParams = new ArrayList<>();

	public void setName(String name) {
		mName = name;
	}

	public void addParam(String type, String name) {
		ParamDef paramDef = new ParamDef();
		paramDef.type = type;
		paramDef.name = name;
		mParams.add(paramDef);
	}

	public void print(String packageName) {

		String fieldTemplate = "    private #{type} #{name};";
		String getterTemplate = "    public #{type} get#{|name}() {\n       return this.#{name};\n    }";
		String setterTemplate = "    public void set#{|name}(#{type} #{name}) {\n        this.#{name} = #{name};\n    }";

		System.out.println("package " + packageName + ";");
		System.out.println("");
		System.out.println("public class " + capitalize(mName) + " {");
		for (ParamDef paramDef : mParams) {
			System.out.println(fillTemplate(fieldTemplate, paramDef));
		}

		System.out.println("");
		for (ParamDef paramDef : mParams) {
			System.out.println(fillTemplate(getterTemplate, paramDef));
			System.out.println(fillTemplate(setterTemplate, paramDef));
		}
		System.out.println("}");

	}

}
