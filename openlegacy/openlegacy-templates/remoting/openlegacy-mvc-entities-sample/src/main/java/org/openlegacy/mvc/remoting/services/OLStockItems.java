/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.mvc.remoting.services;

import org.openlegacy.mvc.remoting.db.model.StockItem;
import org.openlegacy.mvc.remoting.db.model.StockItemImage;

/**
 * @author Imivan
 * 
 */
public interface OLStockItems {

	public StockItem getOrCreateStockItem(Integer itemNumber);

	public void addOrUpdateNote(Integer itemNumber, String noteId, String text);

	public void updateStockItem(StockItem stockItem);

	public void addImage(Integer itemNumber, byte[] bytes);

	public StockItemImage getImage(Long imageId);

}
