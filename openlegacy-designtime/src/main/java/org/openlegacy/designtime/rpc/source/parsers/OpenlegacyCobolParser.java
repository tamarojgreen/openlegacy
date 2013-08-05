package org.openlegacy.designtime.rpc.source.parsers;

import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.rpc.source.CodeParser;
import org.openlegacy.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	private String copybookExtension = ".cpy";
	private String cobolExtension = ".cbl";
	private final static Log logger = LogFactory.getLog(OpenlegacyCobolParser.class);

	private String copyBookPath;

	private CobolParser cobolParser;

	public String getCopyBookPath() {
		return copyBookPath;
	}

	private static String writeToTempFile(String source, String extension) throws IOException {

		File tempFile = File.createTempFile("temp" + System.currentTimeMillis(), extension);

		tempFile.deleteOnExit();

		BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
		try {
			out.write(source);
		} finally {
			out.close();

		}
		return tempFile.getPath();
	}

	@SuppressWarnings({ "unchecked" })
	private boolean handlePreProcess(CommonTree rootNode) throws IOException {

		boolean result = false;

		if (!Jaxen.evaluate(rootNode, USE_COPYBOOK_QUERY).isEmpty()) {
			result = true;
			List<File> koppaSerachPath = new ArrayList<File>();
			koppaSerachPath.add(new File(copyBookPath));
			cobolParser.setCopybookPath(koppaSerachPath);
			cobolParser.setPreprocessing(true);

			List<CommonTree> copybookNodes = (List<CommonTree>)Jaxen.evaluate(rootNode, COPY_ROOT_QUERY);
			if (!copybookNodes.isEmpty()) {

				for (CommonTree copybookNode : copybookNodes) {
					copybookNode.setParent(null);
					List<CommonTree> fileNode = (List<CommonTree>)Jaxen.evaluate(copybookNode, COPY_FILE_QUERY);
					String fileName = fileNode.get(0).getText() + ".cpy";
					List<CommonTree> replaceNodes = (List<CommonTree>)Jaxen.evaluate(copybookNode, COPYBOOK_REPLACE_QUERY);
					if (!replaceNodes.isEmpty()) {

						CommonTree replaceNode = replaceNodes.get(0);
						replaceNode.setParent(null);
						List<CommonTree> operandNodes = (List<CommonTree>)Jaxen.evaluate(replaceNode, OPERAND_QUERY);
						String oldString = removeDelimiter(operandNodes.get(0).getText());
						String newString = removeDelimiter(operandNodes.get(1).getText());
						CobolParserUtils.replaceStringInFile(oldString, newString, copyBookPath, fileName);
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

	public ParseResults parse(String source, Map<String, InputStream> streamMap) throws IOException {
		copyBookPath = CobolParserUtils.createTmpDir("CopyBookDir");
		CobolParserUtils.copyStreamsToFile(copyBookPath, streamMap);
		return parse(source, cobolExtension);

	}

	public ParseResults parse(String source, String fileName) {

		String tempFileName = "";
		String extension = FileUtils.fileExtension(fileName);
		boolean isCopyBook = extension.equals(copybookExtension);
		if (isCopyBook) {
			source = source.replaceAll(":.*:", "");
		}

		try {
			tempFileName = writeToTempFile(source, extension);
		} catch (Exception e) {// Catch exception if any
			logger.fatal(e);
			return null;
		}
		try {
			koopa.parsers.ParseResults parseResults = cobolParser.parse(new File(tempFileName));
			if (handlePreProcess(parseResults.getTree())) {
				parseResults = cobolParser.parse(new File(tempFileName));
			}

			CobolParseResults olParseResults = new CobolParseResults(parseResults, isCopyBook);

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
}
