package com.openlegacy.enterprise.ide.eclipse.editors.pages;

import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public interface IOpenLegacyPage {

	public void refresh();

	public void revalidatePage(String key);

	public List<IOpenLegacyPage> getSubscribers(String key);

	public void addSubscriber(String key, IOpenLegacyPage page);
}
