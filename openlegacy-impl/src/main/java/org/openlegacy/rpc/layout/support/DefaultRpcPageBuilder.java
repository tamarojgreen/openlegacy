package org.openlegacy.rpc.layout.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.definitions.page.support.SimplePagePartDefinition;
import org.openlegacy.definitions.page.support.SimplePagePartRowDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.layout.PagePartDefinition;
import org.openlegacy.layout.PagePartRowDefinition;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcTableDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcTableDefinition.SimpleRpcColumnDefinition;
import org.openlegacy.rpc.layout.RpcPageBuilder;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultRpcPageBuilder implements RpcPageBuilder {

	private final static Log logger = LogFactory.getLog(DefaultRpcPageBuilder.class);

	/**
	 * Page builder entry point. Builds a page definition from a screen entity definition
	 */
	@Override
	public PageDefinition build(RpcEntityDefinition entityDefinition) {
		PageDefinition pageDefinition = new SimplePageDefinition(entityDefinition);

		// copy all actions to the page definition to allow separate control in page actions
		pageDefinition.getActions().addAll(entityDefinition.getActions());

		for (PartEntityDefinition<RpcFieldDefinition> rpcPartEntityDefinition : entityDefinition.getPartsDefinitions().values()) {
			List<PagePartDefinition> pageParts = buildPagePartFromRpcPart(pageDefinition,
					(RpcPartEntityDefinition)rpcPartEntityDefinition, entityDefinition, rpcPartEntityDefinition.getPartName());
			pageDefinition.getPageParts().addAll(pageParts);
		}

		return pageDefinition;
	}

	protected void sortFields(List<ScreenFieldDefinition> sortedFields) {
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
	}

	private List<PagePartDefinition> buildPagePartFromRpcPart(PageDefinition pageDefinition,
			RpcPartEntityDefinition rpcPartEntityDefinition, RpcEntityDefinition entityDefinition, String parentEntityName) {
		if (rpcPartEntityDefinition.getCount() > 1) {
			PagePartDefinition pagePart = buildTablePagePart(pageDefinition, rpcPartEntityDefinition, parentEntityName);
			List<PagePartDefinition> pageParts = new ArrayList<PagePartDefinition>();
			pageParts.add(pagePart);
			return pageParts;
		} else {
			Collection<RpcFieldDefinition> fields = rpcPartEntityDefinition.getFieldsDefinitions().values();
			List<RpcFieldDefinition> sortedFields = new ArrayList<RpcFieldDefinition>(fields);

			SimplePagePartDefinition pagePart = (SimplePagePartDefinition)buildPagePart(sortedFields, entityDefinition,
					parentEntityName);
			pagePart.setDisplayName(rpcPartEntityDefinition.getDisplayName());

			List<PagePartDefinition> pageParts = new ArrayList<PagePartDefinition>();
			pageParts.add(pagePart);
			for (RpcPartEntityDefinition rpcPartEntityDefinitionInner : rpcPartEntityDefinition.getInnerPartsDefinitions().values()) {
				String parentName = MessageFormat.format("{0}.{1}", parentEntityName, rpcPartEntityDefinitionInner.getPartName());
				List<PagePartDefinition> innerPageParts = buildPagePartFromRpcPart(pageDefinition, rpcPartEntityDefinitionInner,
						entityDefinition, parentName);
				pageParts.addAll(innerPageParts);
			}

			// if screen part has position (loaded from @PartPosition), override the calculated position
			return pageParts;

		}

	}

	private static PagePartDefinition buildTablePagePart(PageDefinition pageDefinition,
			RpcPartEntityDefinition rpcPartEntityDefinition, String parentEntityName) {
		SimpleRpcTableDefinition rpcTable = new SimpleRpcTableDefinition(rpcPartEntityDefinition.getPartName());
		rpcTable.setTableEntityName(parentEntityName);
		Collection<RpcFieldDefinition> fields = rpcPartEntityDefinition.getFieldsDefinitions().values();
		for (RpcFieldDefinition rpcFieldDefinition : fields) {
			SimpleRpcColumnDefinition rpcColumn = new SimpleRpcColumnDefinition();
			String[] nameParts = rpcFieldDefinition.getName().split("\\.");
			String columnName = nameParts.length > 0 ? nameParts[nameParts.length - 1] : rpcFieldDefinition.getName();
			rpcColumn.setName(columnName);
			rpcColumn.setDisplayName(rpcFieldDefinition.getDisplayName());
			rpcColumn.setSampleValue(rpcFieldDefinition.getSampleValue());
			rpcTable.getColumnDefinitions().add(rpcColumn);
		}
		SimplePagePartDefinition pagePart = new SimplePagePartDefinition();
		pagePart.setTableDefinition(rpcTable);
		pagePart.setTableFieldName(rpcTable.getClass().getSimpleName());

		return pagePart;
	}

	private static PagePartDefinition buildPagePart(List<RpcFieldDefinition> fields, RpcEntityDefinition entityDefinition,
			String parentName) {
		SimplePagePartDefinition pagePart = new SimplePagePartDefinition();

		PagePartRowDefinition currentPagePartRow = null;
		Set<Integer> columnValues = new HashSet<Integer>();
		if (fields.size() == 0) {
			logger.warn("A rpc/rpc part with 0 fields found. Page part not created. Class:"
					+ entityDefinition.getEntityClassName());
			return pagePart;
		}
		// iterate through all the neighbor fields, and build row part rows upon row change, and find the end column
		for (RpcFieldDefinition rpcFieldDefinition : fields) {
			currentPagePartRow = new SimplePagePartRowDefinition();
			if (rpcFieldDefinition.getClass().isAssignableFrom(SimpleRpcFieldDefinition.class)) { // QUICK FIX
				SimpleRpcFieldDefinition test = (SimpleRpcFieldDefinition)rpcFieldDefinition;
				test.setShortName(String.format("%s.%s", parentName, StringUtil.toJavaFieldName(test.getDisplayName())));
			}
			currentPagePartRow.getFields().add(rpcFieldDefinition);
			pagePart.getPartRows().add(currentPagePartRow);
		}

		pagePart.setColumns(columnValues.size());

		return pagePart;
	}

}
