package org.openlegacy.demo.db.model.services;

import org.openlegacy.db.DbEntity;

import org.openlegacy.db.DbSession;
import org.openlegacy.modules.login.Login;
import javax.inject.Inject;

import javax.jws.WebService;

import org.openlegacy.demo.db.model.UploadedFile;
import org.openlegacy.demo.db.model.StockItemNote;
import org.openlegacy.demo.db.model.StockItemImage;
import org.openlegacy.demo.db.model.StockItem;

import org.openlegacy.demo.db.model.ProductItem; 

import org.openlegacy.db.actions.DbActions;
import org.openlegacy.terminal.wait_conditions.WaitCondition;

/**
 *  A service implementation which invokes OpenLegacy API, and returns a service output.
 *  The code below should be customize to perform a working scenario which goes through the relevant screens.
 *  Can be tested by invoking the test TestService1ServiceTest.
 *  The interface TestService1Service can be customized to enabling passing parameters to the service, and this class can consume the parameters within the relavant screens.    
 */
@WebService(endpointInterface = "org.openlegacy.demo.db.model.services.TestService1Service")
public class TestService1ServiceImpl implements TestService1Service {

	@Inject
	private DbSession dbSession;

	@Override
	public TestService1Out getTestService1(TestService1In testService1In) {

		try{
			dbSession.getModule(Login.class).login("USER","PASSWORD");
			
			
			UploadedFile uploadedFile = dbSession.getEntity(UploadedFile.class);
			StockItemNote stockItemNote = dbSession.getEntity(StockItemNote.class);
			stockItemNote.setStockItem(testService1In.getStockItem());
			stockItemNote.setText(testService1In.getText());
			StockItemImage stockItemImage = dbSession.getEntity(StockItemImage.class);
			stockItemImage.setImage(testService1In.getImage());
			StockItem stockItem = dbSession.getEntity(StockItem.class);
			stockItem.setImages(testService1In.getImages());
			
			dbSession.doAction(DbActions.READ(), (DbEntity)uploadedFile);
			dbSession.doAction(DbActions.READ(), (DbEntity)stockItemNote);
			dbSession.doAction(DbActions.READ(), (DbEntity)stockItemImage);
			dbSession.doAction(DbActions.READ(), (DbEntity)stockItem);
			
			TestService1Out testService1Out = new TestService1Out();
			ProductItem productItem = dbSession.getEntity(ProductItem.class);
			testService1Out.setChilds(productItem.getChilds());
			testService1Out.setParent(productItem.getParent());
			testService1Out.setStockItem(stockItem);
			testService1Out.setImage(stockItemImage.getImage());
			testService1Out.setFileName(uploadedFile.getFileName());

			return testService1Out;
		} finally {
			dbSession.disconnect();
		}
	}

}
