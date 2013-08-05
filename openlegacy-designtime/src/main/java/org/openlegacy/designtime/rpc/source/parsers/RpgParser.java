package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
import org.openlegacy.designtime.DesigntimeException;
import org.openlegacy.designtime.rpc.source.CodeParser;
import org.openlegacy.modules.login.Login.UserField;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RpgParser implements CodeParser {

	String rpcEntityName = null;

	private static final int FORMAT_TYPE_POS = 5;
	private static final int SYMBOL_POS = 6;
	private static final int ERROR_POS = 100; // use the location out of line
	private static final int EXTERNAL_POS = 22 - SYMBOL_POS; // 22-6
	private static final char DIRRECTIVE_SYMBOL = '/';
	private static final char COMMENT_SYMBOL = '*';
	private static final char BLANK = ' ';

	private static final char DEFINITION = 'D';

	private static Map<Character, Class<?>> fieldJavaTypeMap = new HashMap<Character, Class<?>>();
	private static Map<Character, FieldTypeDefinition> fieldTypeMap = new HashMap<Character, FieldTypeDefinition>();
	private static Set<Character> specChars = new HashSet<Character>();

	RpgParser() {
		specChars.add('H'); // header
		specChars.add('F'); // file
		specChars.add('E'); // extension
		specChars.add('L'); // line counter
		specChars.add(DEFINITION); // definitions
		specChars.add('I'); // input
		specChars.add('C'); // calc
		specChars.add('O'); // output

		fieldJavaTypeMap.put(' ', String.class);
		fieldJavaTypeMap.put('A', Character.class);
		fieldJavaTypeMap.put('D', Date.class);
		fieldJavaTypeMap.put('F', Float.class);
		fieldJavaTypeMap.put('I', Integer.class);
		fieldJavaTypeMap.put('S', Integer.class);

		fieldTypeMap.put(' ', new SimpleTextFieldTypeDefinition());
		fieldTypeMap.put('A', new SimpleTextFieldTypeDefinition());
		fieldTypeMap.put('D', new SimpleDateFieldTypeDefinition());
		fieldTypeMap.put('F', new SimpleNumericFieldTypeDefinition());
		fieldTypeMap.put('I', new SimpleNumericFieldTypeDefinition());
		fieldTypeMap.put('S', new SimpleNumericFieldTypeDefinition());
	}

	private static Class<?> getJavaType(Character c) throws DesigntimeException {
		try {
			return fieldJavaTypeMap.get(c);

		} catch (Exception e) {
			throw (new DesigntimeException("Type " + c.toString() + " not supported"));
		}
	}

	private static FieldTypeDefinition getType(Character c) throws DesigntimeException {
		try {
			return fieldTypeMap.get(c);

		} catch (Exception e) {
			throw (new DesigntimeException("Type " + c.toString() + " not supported"));
		}
	}

	private static boolean isSpecType(char character) {
		return specChars.contains(character);
	}

	// Determine position of Form Type indicator for lines of the file
	private int getOffset(String line) throws DesigntimeException {
		int offset = ERROR_POS;
		// If the line starts with 5 blanks, than it's already formated.
		// '*' at the 6'th position doesn't necessarily means the line is a comment (as in directive line)
		if (line.startsWith("     ") && line.length() >= SYMBOL_POS
				&& (isSpecType(line.charAt(FORMAT_TYPE_POS)) || line.charAt(SYMBOL_POS) == DIRRECTIVE_SYMBOL)) {
			offset = 5; // No need to pad line.
		} else {
			for (int i = 0; i < line.length(); i++) {
				if (isSpecType(line.charAt(i))) {
					offset = i;
					break;
				} else if (line.charAt(i) == COMMENT_SYMBOL) {
					int end = line.indexOf('\n');
					if (end > 0) { // try next line on comment
						return (getOffset(line.substring(end + 1)));
					}
				}
			}
		}
		if (offset == ERROR_POS) {
			throw (new DesigntimeException("Fail to parse file"));
		}
		return offset;
	}

	private static String getFieldName(String rowName) {
		return StringUtil.rightTrim(rowName.substring(rowName.indexOf('$') + 1));
	}

	public RpcEntityDefinition parse(String source) throws DesigntimeException {

		SimpleRpcEntityDefinition rpcDefinition = new SimpleRpcEntityDefinition();
		rpcDefinition.setEntityName(rpcEntityName);
		// final String D_LINE_FORMAT = "(.{15})([E\\s])([A-Z\\s]{2})([SU\\s])" + "(.{7})(.{7})([A-Z\\*\\s])([\\d\\s]{2})(.*)";
		final String D_LINE_FORMAT = "(.{15})([E\\s])([\\sS])(S\\s|DS)" + "(.{7})(.{7})([A-Z\\*\\s])([\\d\\s]{2})(.*)";

		try {
			source = source.toUpperCase();

			int offset = getOffset(source);

			Pattern pattern = Pattern.compile(D_LINE_FORMAT);
			Matcher match = null;
			for (String line : source.split("\n")) {
				if (line.charAt(offset) == DEFINITION) {
					String defnitionLine = line.substring(offset);
					if (defnitionLine.charAt(EXTERNAL_POS) != BLANK) { // for now ignore externals
						continue;
					}
					match = pattern.matcher(defnitionLine);
					if (match.find() == true) {
						String origFieldName = getFieldName(match.group(1));
						String javaFieldName = StringUtil.toJavaFieldName(origFieldName);
						// RpcFieldDefinition rpcFieldDefinition = new SimpleRpcFieldDefinition(javaFieldName, null);
						SimpleRpcFieldDefinition rpcFieldDefinition = new SimpleRpcFieldDefinition(javaFieldName, null);
						rpcFieldDefinition.setOriginalName(origFieldName);

						rpcFieldDefinition.setLength(Integer.parseInt(StringUtil.leftTrim(match.group(6))));
						Character fieldType = match.group(7).charAt(0);
						rpcFieldDefinition.setJavaType(getJavaType(fieldType));
						rpcFieldDefinition.setType(UserField.class);
						rpcFieldDefinition.setFieldTypeDefinition(getType(fieldType));
						rpcDefinition.getFieldsDefinitions().put(javaFieldName, rpcFieldDefinition);

					}

				}

			}
		} catch (Exception e) {
			throw (new DesigntimeException("Error: Fail to parse file"));
		}

		return rpcDefinition;
	}

	public ParseResults parse(String source, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ParseResults parse(String source, Map<String, InputStream> streamMap) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
