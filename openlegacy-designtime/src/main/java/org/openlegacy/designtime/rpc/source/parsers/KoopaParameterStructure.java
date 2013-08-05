package org.openlegacy.designtime.rpc.source.parsers;

/**
 * 
 * Extracts information from koopa CommonTree nodes of type dataDescriptionEntry_format1 into generic ParameterStructure.
 * 
 */

import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import koopa.trees.antlr.jaxen.Jaxen;

public class KoopaParameterStructure implements ParameterStructure {

	private static final String PICTURE_QUERY = "//picture//pictureString";
	private static final String OCCURS_QUERY = "//occurs//integer";
	private static final String COBOL_WORD_QUERY = "//dataName//cobolWord";
	private static final String LEVEL_NUMBER_QUERY = "//levelNumber";
	private static final char OPEN_BRACKET_SMBOL = '(';

	private int fieldLevel;
	private String fieldName;
	private int occurs;
	private String variableDeclartion;
	private List<ParameterStructure> subFieldsList;

	private static String genarteFlatPic(CommonTree picNodes) {

		StringBuilder x = new StringBuilder();
		x.append(picNodes.getChild(0).getText());
		Integer fieldLenght = x.length();
		for (int idx = 1; idx < picNodes.getChildCount(); idx++) {
			String line = picNodes.getChild(idx).getText();
			if (line.charAt(0) == OPEN_BRACKET_SMBOL) {
				int times = Integer.parseInt(picNodes.getChild(idx + 1).getText());
				char repetingChar = x.charAt(fieldLenght - 1);
				idx += 2;
				for (int i = 1; i < times; i++) {
					x.append(repetingChar);
				}
			} else {
				x.append(line);
				fieldLenght += line.length();
			}

		}
		return x.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.ParameterStructure#getvariableDeclartion()
	 */
	public String getVariableDeclaration() {
		return variableDeclartion;
	}

	@SuppressWarnings("unchecked")
	public KoopaParameterStructure(CommonTree parameterNode) {

		parameterNode.setParent(null);

		CommonTree tempNode = (CommonTree)Jaxen.evaluate(parameterNode, LEVEL_NUMBER_QUERY).get(0);
		fieldLevel = Integer.parseInt(tempNode.getChild(0).getText());
		tempNode = (CommonTree)Jaxen.evaluate(parameterNode, COBOL_WORD_QUERY).get(0);
		fieldName = tempNode.getChild(0).getText();

		List<CommonTree> tempNodeList = (List<CommonTree>)Jaxen.evaluate(parameterNode, OCCURS_QUERY);
		if (tempNodeList.isEmpty()) {
			occurs = 1;
		} else {
			occurs = Integer.parseInt(tempNodeList.get(0).getChild(0).getText());
		}

		tempNodeList = (List<CommonTree>)Jaxen.evaluate(parameterNode, PICTURE_QUERY);
		if (tempNodeList.isEmpty()) {
			subFieldsList = new ArrayList<ParameterStructure>();
		} else {
			variableDeclartion = genarteFlatPic(tempNodeList.get(0));

		}
	}

	public Map<String, String> collectSubFields(List<ParameterStructure> paramtersNodes, int startIdx, String topLevelName) {
		Map<String, String> fieldNameMap = new HashMap<String, String>();

		int idx = startIdx;
		int handleLevel = paramtersNodes.get(idx).getLevel();
		while (idx < paramtersNodes.size()) {
			KoopaParameterStructure cobolField = (KoopaParameterStructure)paramtersNodes.get(idx);
			if (cobolField.getLevel() == handleLevel) {
				fieldNameMap.put(cobolField.getFieldName(), topLevelName);
				subFieldsList.add(cobolField);
				paramtersNodes.remove(idx);
				if (!cobolField.isSimple()) {
					fieldNameMap.putAll(cobolField.collectSubFields(paramtersNodes, startIdx, topLevelName));
				}
			} else {
				return fieldNameMap;
			}

		}
		return fieldNameMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.ParameterStructure#getLevel()
	 */
	public int getLevel() {
		return fieldLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.ParameterStructure#getFieldName()
	 */
	public String getFieldName() {
		return fieldName;
	}

	public int getOccurs() {
		return occurs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.ParameterStructure#getSubFieldsList()
	 */
	public List<ParameterStructure> getSubFields() {
		return subFieldsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.ParameterStructure#isSimple()
	 */
	public boolean isSimple() {
		return (subFieldsList == null);
	}

}
