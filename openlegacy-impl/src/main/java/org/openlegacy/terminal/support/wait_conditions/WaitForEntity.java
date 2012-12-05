package org.openlegacy.terminal.support.wait_conditions;

import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.wait_conditions.WaitCoditionAdapter;
import org.openlegacy.utils.ProxyUtil;

public class WaitForEntity extends WaitCoditionAdapter {

	private Class<? extends ScreenEntity> expectedEntityClass;

	public WaitForEntity(Class<? extends ScreenEntity> expectedEntityClass) {
		this.expectedEntityClass = expectedEntityClass;
	}

	public boolean continueWait(TerminalSession terminalSession) {

		ScreenEntity currentEntity = terminalSession.getEntity();
		return !(ProxyUtil.isClassesMatch(currentEntity.getClass(), expectedEntityClass));
	}
}
