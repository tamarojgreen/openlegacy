package org.openlegacy.modules.navigation;

import org.openlegacy.modules.SessionModule;
import org.openlegacy.terminal.ScreenEntityDescriptor;

import java.util.List;

public interface Navigation extends SessionModule {

	List<ScreenEntityDescriptor> getPathFromRoot();

	void navigate(Class<?> targetScreenEntityClass);
}
