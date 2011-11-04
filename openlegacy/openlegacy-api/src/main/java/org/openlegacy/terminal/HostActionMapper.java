package org.openlegacy.terminal;

import org.openlegacy.HostAction;

public interface HostActionMapper {

	Object getCommand(Class<? extends HostAction> hostAction);
}
