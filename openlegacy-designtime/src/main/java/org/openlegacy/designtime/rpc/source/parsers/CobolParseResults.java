package org.openlegacy.designtime.rpc.source.parsers;

import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.rpc.model.support.SimpleRpcEntityDesigntimeDefinition;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import koopa.parsers.ParseResults;
import koopa.tokens.Token;
import koopa.trees.antlr.jaxen.Jaxen;
import koopa.util.Tuple;

public class CobolParseResults implements org.openlegacy.designtime.rpc.source.parsers.ParseResults {

	private final static String PARAMETER_DEFINITION_QUERY = "//linkageSection//dataDescriptionEntry_format1";
	private final static String PARAMETER_COPYBOOK_DEFINITION_QUERY = "//dataDescriptionEntry_format1";
	private final static String PARAGRAPH_QUERY = "//paragraph";
	private final static String PROCEDURE_QUERY = "//procedureDivision";
	// private final static String ROOT_PROGRAM_QUERY_TEMPLATE = "//paragraph//cobolWord[text()='%s']";
	private final static String USE_PARAMETER_QUERY = "//usingPhrase//cobolWord//text()";
	private final static String PARAMETER_USED_QUERY = "//identifier_format2//cobolWord//text()";

	private RpcEntityDefinitionBuilder rpcEntityDefinitionBuilder = new RpcEntityDefinitionBuilderImp(
			new CobolFieldInformationFactory());

	private koopa.parsers.ParseResults parseResults;
	boolean isCopyBook;

	private final static Log logger = LogFactory.getLog(CobolParseResults.class);

	public CobolParseResults(ParseResults parseResults, boolean isCopyBook) {
		this.parseResults = parseResults;
		this.isCopyBook = isCopyBook;
	}

	public RpcEntityDefinition getEntityDefinition() {
		List<ParameterStructure> parameters = organize();
		RpcEntityDefinition rpcEntityDefinition = new SimpleRpcEntityDesigntimeDefinition();
		rpcEntityDefinitionBuilder.build(parameters, rpcEntityDefinition);
		return rpcEntityDefinition;
	}

	private List<ParameterStructure> organize() {
		CommonTree rootNode = parseResults.getTree();

		String queryString = PARAMETER_DEFINITION_QUERY;
		if (isCopyBook) {
			queryString = PARAMETER_COPYBOOK_DEFINITION_QUERY;
		}

		List<ParameterStructure> allParamtersNodes = new ArrayList<ParameterStructure>();

		try {

			@SuppressWarnings("unchecked")
			List<CommonTree> jaxsonParamtersNodes = (List<CommonTree>)Jaxen.evaluate(rootNode, queryString);
			Map<String, String> fieldToTopLevelName = arrangeInLevels(allParamtersNodes, jaxsonParamtersNodes);
			if (isCopyBook) {
				return allParamtersNodes;

			} else {
				return filterUsedParameter(parseResults, allParamtersNodes, fieldToTopLevelName);
			}

		} catch (Exception e) {
			logger.debug("failed at query stage");
			throw new OpenLegacyProviderException("Koopa input is invalid");
		}
	}

	private static Map<String, String> arrangeInLevels(List<ParameterStructure> allParamtersNodes,
			List<CommonTree> jaxsonParamtersNodes) {
		for (int parameterIdx = 0; parameterIdx < jaxsonParamtersNodes.size(); parameterIdx++) {

			allParamtersNodes.add(new KoopaParameterStructure(jaxsonParamtersNodes.get(parameterIdx)));
		}

		Map<String, String> fieldToTopLevelName = new HashMap<String, String>();
		for (int parameterIdx = 0; parameterIdx < allParamtersNodes.size(); parameterIdx++) {
			KoopaParameterStructure cobolParmeter = (KoopaParameterStructure)allParamtersNodes.get(parameterIdx);
			fieldToTopLevelName.put(cobolParmeter.getFieldName(), cobolParmeter.getFieldName());
			if (!cobolParmeter.isSimple()) {
				fieldToTopLevelName.putAll(cobolParmeter.collectSubFields(allParamtersNodes, parameterIdx + 1,
						cobolParmeter.getFieldName()));
			}
		}
		return fieldToTopLevelName;
	}

	private List<ParameterStructure> filterUsedParameter(ParseResults parseResults, List<ParameterStructure> allParamtersNodes,
			Map<String, String> fieldToTopLevelName) {
		List<String> usedParamtersNames = getParameterNames(parseResults);
		List<ParameterStructure> interfaceParamtersNodes = new ArrayList<ParameterStructure>();
		List<String> interfaceParamtersNames = new ArrayList<String>();

		for (String prameterName : usedParamtersNames) {
			if (fieldToTopLevelName.containsKey(prameterName) && !interfaceParamtersNames.contains(prameterName)) {
				interfaceParamtersNames.add(fieldToTopLevelName.get(prameterName));
			}
		}
		for (ParameterStructure parameterNode : allParamtersNodes) {
			if (interfaceParamtersNames.contains(parameterNode.getFieldName())) {
				interfaceParamtersNodes.add(parameterNode);
			}
		}
		return interfaceParamtersNodes;
	}

	@SuppressWarnings("static-method")
	private List<String> getParameterNames(ParseResults parseResults) {

		CommonTree rootNode = parseResults.getTree();
		List<String> paramtersNames = new ArrayList<String>();
		CommonTree programRoot = null;

		@SuppressWarnings("unchecked")
		List<CommonTree> programRootNode = (List<CommonTree>)Jaxen.evaluate(rootNode, PARAGRAPH_QUERY);

		if (programRootNode.size() == 1) {
			programRoot = (CommonTree)programRootNode.get(0).getParent();
		}/*
		 * else if (programRootNode.size() > 1) { String rootProgramQueryString = String.format(ROOT_PROGRAM_QUERY_TEMPLATE,
		 * mainProcedureName);
		 * 
		 * @SuppressWarnings("unchecked") List<CommonTree> retryProgramRootNode = (List<CommonTree>)Jaxen.evaluate(rootNode,
		 * rootProgramQueryString); // Assume there is only one main programRoot =
		 * (CommonTree)retryProgramRootNode.get(0).getParent().getParent().getParent().getParent(); }
		 */else {
			@SuppressWarnings("unchecked")
			List<CommonTree> retryProgramRootNode = (List<CommonTree>)Jaxen.evaluate(rootNode, PROCEDURE_QUERY);
			programRoot = retryProgramRootNode.get(0);
		}

		if (programRoot == null) {
			return paramtersNames;
		}

		programRoot.setParent(null);

		@SuppressWarnings("unchecked")
		List<CommonTree> procedureParamtersNodes = (List<CommonTree>)Jaxen.evaluate(programRoot, USE_PARAMETER_QUERY);

		for (CommonTree procedureParamtersNode : procedureParamtersNodes) {
			paramtersNames.add(procedureParamtersNode.getText());
		}

		if (paramtersNames.isEmpty()) {
			@SuppressWarnings("unchecked")
			List<CommonTree> usedParameters = (List<CommonTree>)Jaxen.evaluate(programRoot, PARAMETER_USED_QUERY);
			for (CommonTree parameter : usedParameters) {
				paramtersNames.add(parameter.getText());
			}
		}

		return paramtersNames;
	}

	public List<String> getErrors() {
		List<String> errors = new ArrayList<String>();
		for (int i = 0; i < parseResults.getErrorCount(); i++) {
			Tuple<Token, String> error = parseResults.getError(i);
			errors.add(error.getFirst() + " " + error.getSecond());
		}
		return errors;
	}

	public List<String> getWarnings() {
		List<String> warnings = new ArrayList<String>();
		for (int i = 0; i < parseResults.getWarningCount(); i++) {
			Tuple<Token, String> warning = parseResults.getWarning(i);
			warnings.add(warning.getFirst() + " " + warning.getSecond());
		}
		return warnings;
	}

}
