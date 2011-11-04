package org.openlegacy.applinx;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.HostActionMapper;

import java.text.MessageFormat;

public class ApxHostActionMapper implements HostActionMapper {

	public String getCommand(Class<? extends HostAction> hostAction) {
		String simpleName = hostAction.getSimpleName().toLowerCase();
		return MessageFormat.format("[{0}]", simpleName);
	}

}
