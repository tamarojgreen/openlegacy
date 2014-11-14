/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.rpc.source.parsers;

import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.rpc.source.CodeParser;
import org.openlegacy.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import koopa.parsers.cobol.CobolParser;
import koopa.trees.antlr.jaxen.Jaxen;

/**
 * A wrapper of koopa COBOL parser. Fetch COBOL program interface from antlr tree and generate RpcEntityDefinition.
 * 
 */

public class OpenlegacyCobolParser implements CodeParser {

	private final static String USE_COPYBOOK_QUERY = "//linkageSection//copyStatement";
	private final static String COPY_ROOT_QUERY = "//copyStatement";
	private final static String COPY_FILE_QUERY = "//textName//cobolWord//text()";
	private final static String OPERAND_QUERY = "//copyOperandName//pseudoLiteral//text()";
	private final static String COPYBOOK_REPLACE_QUERY = "//copyReplacementInstruction";
	private final static String newLine = System.getProperty("line.separator");
	private final static String linePrefix = "      ";
	private final static String CBL_HEADER = linePrefix + "IDENTIFICATION DIVISION." + newLine + linePrefix
			+ "PROGRAM-ID.                     STM." + newLine + linePrefix + "DATA DIVISION." + newLine + linePrefix;

	private final static Pattern linkagePattern = Pattern.compile("LINKAGE\\s*SECTION");
	private final static Pattern procedurePattern = Pattern.compile("PROCEDURE\\s*DIVISION");

	private String copybookExtension = ".cpy";
	private String cobolExtension = ".cbl";
	private final static Log logger = LogFactory.getLog(OpenlegacyCobolParser.class);

	private RpcEntityDefinitionBuilder rpcEntityDefinitionBuilder;

	private String copyBookPath;

	private CobolParser cobolParser;

	public String getCopyBookPath() {
		return copyBookPath;
	}

	@SuppressWarnings({ "unchecked" })
	private List<String> handlePreProcess(CommonTree rootNode) throws IOException {

		List<String> result = new ArrayList<String>();

		if (!Jaxen.evaluate(rootNode, USE_COPYBOOK_QUERY).isEmpty()) {
			List<File> koppaSerachPath = new ArrayList<File>();
			koppaSerachPath.add(new File(copyBookPath));
			cobolParser.setCopybookPath(koppaSerachPath);
			cobolParser.setPreprocessing(true);

			List<CommonTree> copybookNodes = (List<CommonTree>)Jaxen.evaluate(rootNode, COPY_ROOT_QUERY);
			if (!copybookNodes.isEmpty()) {

				for (CommonTree copybookNode : copybookNodes) {
					copybookNode.setParent(null);
					List<CommonTree> fileNode = (List<CommonTree>)Jaxen.evaluate(copybookNode, COPY_FILE_QUERY);
					String copyBookName = fileNode.get(0).getText();
					result.add(copyBookName);
					List<CommonTree> replaceNodes = (List<CommonTree>)Jaxen.evaluate(copybookNode, COPYBOOK_REPLACE_QUERY);
					if (!replaceNodes.isEmpty()) {

						CommonTree replaceNode = replaceNodes.get(0);
						replaceNode.setParent(null);
						List<CommonTree> operandNodes = (List<CommonTree>)Jaxen.evaluate(replaceNode, OPERAND_QUERY);
						String oldString = removeDelimiter(operandNodes.get(0).getText());
						String newString = removeDelimiter(operandNodes.get(1).getText());
						CobolParserUtils.replaceStringInFile(oldString, newString, copyBookPath, copyBookName + copybookExtension);
					}
				}

			}
		}
		return result;
	}

	@SuppressWarnings("static-method")
	private String removeDelimiter(String name) {
		int length = name.length();
		return name.substring(2, length - 2);
	}

	@Override
	public ParseResults parse(String source, Map<String, InputStream> streamMap) throws IOException {
		copyBookPath = CobolParserUtils.createTmpDir("CopyBookDir");
		CobolParserUtils.copyStreamsToFile(copyBookPath, streamMap);
		return parse(source, cobolExtension);

	}

	private static String getParsePart(String source) {

		String result = CBL_HEADER;
		Matcher matcherStart = linkagePattern.matcher(source);
		Matcher matchEnd = procedurePattern.matcher(source);
		if (matcherStart.find() == true && matchEnd.find() == true) {
			String remain = source.substring(matchEnd.end());
			int eol = remain.indexOf('.');
			if (eol > 1) {
				result = result + source.substring(matcherStart.start(), matchEnd.end()) + remain.substring(0, eol + 1) + newLine;
			} else {
				result = result + source.substring(matcherStart.start());
			}
		}
		return result;
	}

	@Override
	public ParseResults parse(String source, String fileName) {

		String tempFileName = "";
		String extension = FileUtils.fileExtension(fileName);

		boolean isCopyBook = extension.equals(copybookExtension);
		if (isCopyBook) {

			source = source.replaceAll(":.*:", "");
		} else {
			source = getParsePart(source);
		}

		try {
			source = CobolParserUtils.removeCommentsAndLabels(source);
			tempFileName = CobolParserUtils.writeToTempFile(source, extension);
		} catch (Exception e) {// Catch exception if any
			logger.fatal(e);
			return null;
		}
		try {
			koopa.parsers.ParseResults parseResults = cobolParser.parse(new File(tempFileName));
			List<String> externalParts = handlePreProcess(parseResults.getTree());
			if (externalParts.size() > 0) {
				parseResults = cobolParser.parse(new File(tempFileName));
			}

			CobolParseResults olParseResults = new CobolParseResults(parseResults, isCopyBook);
			olParseResults.setRpcEntityDefinitionBuilder(rpcEntityDefinitionBuilder);

			if (!parseResults.isValidInput()) {
				throw (new OpenLegacyParseException("Koopa input is invalid", olParseResults));
			}

			return olParseResults;

		} catch (Exception e) {
			throw (new OpenLegacyParseException("Koopa input is invalid", e));
		}

	}

	public CobolParser getCobolParser() {
		return cobolParser;
	}

	public void setCobolParser(CobolParser cobolParser) {
		this.cobolParser = cobolParser;
	}

	public void setCobolExtension(String cobolExtension) {
		this.cobolExtension = cobolExtension;
	}

	public void setCopybookExtension(String copybookExtension) {
		this.copybookExtension = copybookExtension;
	}

	public void setRpcEntityDefinitionBuilder(RpcEntityDefinitionBuilder rpcEntityDefinitionBuilder) {
		this.rpcEntityDefinitionBuilder = rpcEntityDefinitionBuilder;
	}
}
