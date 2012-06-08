package org.openlegacy.terminal.samples.mvc.controllers;

import org.openlegacy.demo.db.model.StockItem;
import org.openlegacy.demo.db.model.StockItemImage;
import org.openlegacy.demo.db.services.StockItemsService;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.WorkWithItemMaster1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/StockItemImages")
public class StockItemImagesController {

	@Inject
	private StockItemsService stockItemsService;

	@Inject
	private TerminalSession terminalSession;

	// handle page initial display
	@RequestMapping(method = RequestMethod.GET)
	public String show(Model uiModel) {
		// get the item number from the host session
		Integer itemNumber = terminalSession.getEntity(WorkWithItemMaster1.class).getItemNumber();

		// fetch relevant notes from the DB and pass the page
		StockItem stockItem = stockItemsService.getOrCreateStockItem(itemNumber);
		uiModel.addAttribute(stockItem);

		return "StockItemImages";
	}

	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public void uploadImage(@RequestParam("itemNumber") Integer itemNumber, @RequestParam("file") MultipartFile file,
			HttpServletResponse response) throws IOException {

		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			stockItemsService.addImage(itemNumber, bytes);
		}
	}

	@RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
	public void showImage(HttpServletResponse response, @PathVariable("id") Long imageId) throws IOException {

		StockItemImage stockItemImage = stockItemsService.getImage(imageId);
		response.getOutputStream().write(stockItemImage.getImage());
	}
}
