package com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcNumericFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.ActionsComparator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.IEntityActionsSorter;

import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntityActionsSorter implements IEntityActionsSorter {

	public static final RpcEntityActionsSorter INSTANCE = new RpcEntityActionsSorter();

	private RpcEntityActionsSorter() {}

	private static final List<String> RPC_ENTITY_ORDER = Arrays.asList(AnnotationConstants.NAME,
			AnnotationConstants.DISPLAY_NAME, RpcAnnotationConstants.LANGUAGE);

	private static final List<String> RPC_FIELD_ORDER = Arrays.asList(RpcAnnotationConstants.ORIGINAL_NAME,
			AnnotationConstants.KEY, RpcAnnotationConstants.DIRECTION, AnnotationConstants.LENGTH,
			AnnotationConstants.FIELD_TYPE, AnnotationConstants.DISPLAY_NAME, AnnotationConstants.SAMPLE_VALUE,
			AnnotationConstants.HELP_TEXT, AnnotationConstants.EDITABLE, RpcAnnotationConstants.DEFAULT_VALUE,
			RpcAnnotationConstants.EXPRESSION);

	private static final List<String> RPC_BOOLEAN_FIELD_ORDER = Arrays.asList(AnnotationConstants.TRUE_VALUE,
			AnnotationConstants.FALSE_VALUE, AnnotationConstants.TREAT_EMPTY_AS_NULL);

	private static final List<String> RPC_NUMERIC_FIELD_ORDER = Arrays.asList(RpcAnnotationConstants.MINIMUM_VALUE,
			RpcAnnotationConstants.MAXIMUM_VALUE, RpcAnnotationConstants.DECIMAL_PLACES);

	private static final List<String> RPC_PART_ORDER = Arrays.asList(AnnotationConstants.DISPLAY_NAME, AnnotationConstants.NAME);

	public List<AbstractAction> sort(List<AbstractAction> actions) {
		if ((actions == null) || actions.isEmpty()) {
			return actions;
		}
		AbstractAction abstractAction = actions.get(0);
		if (abstractAction instanceof RpcEntityAction) {
			Collections.sort(actions, ActionsComparator.getInstance(RPC_ENTITY_ORDER));
		} else if (abstractAction instanceof RpcFieldAction) {
			Collections.sort(actions, ActionsComparator.getInstance(RPC_FIELD_ORDER));
		} else if (abstractAction instanceof RpcBooleanFieldAction) {
			Collections.sort(actions, ActionsComparator.getInstance(RPC_BOOLEAN_FIELD_ORDER));
		} else if (abstractAction instanceof RpcNumericFieldAction) {
			Collections.sort(actions, ActionsComparator.getInstance(RPC_NUMERIC_FIELD_ORDER));
		} else if (abstractAction instanceof RpcPartAction) {
			Collections.sort(actions, ActionsComparator.getInstance(RPC_PART_ORDER));
		}
		return actions;
	}

	@Override
	public int getOrderIndex(AbstractAction action, int defaultIndex) {
		if (action instanceof RpcEntityAction) {
			return RPC_ENTITY_ORDER.contains(action.getKey()) ? RPC_ENTITY_ORDER.indexOf(action.getKey()) : defaultIndex;
		} else if (action instanceof RpcFieldAction) {
			return RPC_FIELD_ORDER.contains(action.getKey()) ? RPC_FIELD_ORDER.indexOf(action.getKey()) : defaultIndex;
		} else if (action instanceof RpcBooleanFieldAction) {
			return RPC_BOOLEAN_FIELD_ORDER.contains(action.getKey()) ? RPC_BOOLEAN_FIELD_ORDER.indexOf(action.getKey())
					: defaultIndex;
		} else if (action instanceof RpcNumericFieldAction) {
			return RPC_NUMERIC_FIELD_ORDER.contains(action.getKey()) ? RPC_NUMERIC_FIELD_ORDER.indexOf(action.getKey())
					: defaultIndex;
		} else if (action instanceof RpcPartAction) {
			return RPC_PART_ORDER.contains(action.getKey()) ? RPC_PART_ORDER.indexOf(action.getKey()) : defaultIndex;
		}
		return defaultIndex;
	}

}
