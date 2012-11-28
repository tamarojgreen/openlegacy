package org.openlegacy.mvc.remoting.backend.db.services;

import org.openlegacy.mvc.remoting.db.model.StockItem;
import org.openlegacy.mvc.remoting.db.model.StockItemImage;
import org.openlegacy.mvc.remoting.db.model.StockItemNote;
import org.openlegacy.mvc.remoting.services.StockItemsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service(value = "stockItemsDbService")
@Transactional
public class StockItemsServiceImpl implements StockItemsService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public StockItem updateStockItem(StockItem stockItem) {
		stockItem = entityManager.merge(stockItem);
		entityManager.flush();
		return stockItem;
	}

	@Override
	public StockItem getStockItem(Integer itemId) {
		return entityManager.find(StockItem.class, itemId);
	}

	@Override
	public StockItem getOrCreateStockItem(Integer itemId) {
		StockItem stockItem = entityManager.find(StockItem.class, itemId);
		if (stockItem == null) {
			stockItem = new StockItem();
			stockItem.setItemId(itemId);
			stockItem = updateStockItem(stockItem);
		}
		return stockItem;
	}

	@Override
	public void addOrUpdateNote(Integer itemId, String noteId, String text) {
		StockItem stockItem = getOrCreateStockItem(itemId);
		StockItemNote stockItemNote = new StockItemNote();
		stockItemNote.setNoteId(noteId);
		stockItemNote.setText(text);
		stockItem.getNotes().put(noteId, stockItemNote);
		entityManager.merge(stockItem);

	}

	@Override
	public void addImage(Integer itemId, byte[] bytes) {
		StockItem stockItem = getOrCreateStockItem(itemId);
		StockItemImage stockItemImage = new StockItemImage();
		stockItemImage.setImage(bytes);
		stockItem.getImages().add(stockItemImage);
		entityManager.merge(stockItem);
	}

	@Override
	public StockItemImage getImage(Long imageId) {
		return entityManager.find(StockItemImage.class, imageId);
	}
}
