package org.openlegacy.modules.navigation;

import org.openlegacy.modules.HostSessionModule;
import org.openlegacy.terminal.ScreenEntityDescriptor;

import java.util.List;

public interface Navigation extends HostSessionModule {

	List<ScreenEntityDescriptor> getPathFromRoot();

	void navigate(Class<?> targetScreenEntityClass);
}
