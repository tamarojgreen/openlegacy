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

package org.openlegacy.mvc.remoting.backend.services;

import org.openlegacy.mvc.remoting.backend.db.services.StockItemsService;
import org.openlegacy.mvc.remoting.db.model.StockItem;
import org.openlegacy.mvc.remoting.db.model.StockItemImage;
import org.openlegacy.mvc.remoting.services.OLStockItems;

/**
 * @author Imivan
 * 
 */
public class OLStockItemsWebServiceImpl implements OLStockItems {

	private StockItemsService stockItemsService;

	public OLStockItemsWebServiceImpl(StockItemsService stockItemsDbService) {
		this.stockItemsService = stockItemsDbService;
	}

	@Override
	public StockItem getOrCreateStockItem(Integer itemNumber) {
		return stockItemsService.getOrCreateStockItem(itemNumber);
	}

	@Override
	public void addOrUpdateNote(Integer itemNumber, String noteId, String text) {
		stockItemsService.addOrUpdateNote(itemNumber, noteId, text);
	}

	@Override
	public void updateStockItem(StockItem stockItem) {
		stockItemsService.updateStockItem(stockItem);
	}

	@Override
	public void addImage(Integer itemNumber, byte[] bytes) {
		stockItemsService.addImage(itemNumber, bytes);
	}

	@Override
	public StockItemImage getImage(Long imageId) {
		return stockItemsService.getImage(imageId);
	}

}
