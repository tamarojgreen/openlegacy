package tests;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.xml.transform.stream.StreamResult;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.demo.db.model.StockItem;
import org.openlegacy.demo.db.model.StockItemImage;
import org.openlegacy.demo.db.model.services.TestService1Service;
import org.openlegacy.demo.db.model.services.TestService1Service.TestService1In;
import org.openlegacy.demo.db.model.services.TestService1Service.TestService1Out;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 *  A test which invokes TestService1 web service via an http client.
 *  The application should be app and running (via run-application.launch)
 *  To run the test, select Run As -> JUnit test.
 *  If the service has parameters, they should be set via the test. 
 */
@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestService1ServiceTest {

	@Inject
	@Qualifier("testService1Client")
	private TestService1Service testService1Client;

	@Inject
	private Marshaller marshaller;
	
	@Test
	public void testTestService1Service() throws ParseException, XmlMappingException, IOException {


		long before = System.currentTimeMillis();

		TestService1In testService1In = new TestService1In();
		testService1In.setStockItem(new StockItem());
		testService1In.setText("");
		testService1In.setImage(new byte[]{});
		testService1In.setImages(new ArrayList<StockItemImage>());

		TestService1Out testService1Out = testService1Client.getTestService1(testService1In);
		Assert.assertNotNull(testService1Out);

        long after = System.currentTimeMillis();
  		System.out.println("Execution time:" + (after-before));
  				
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		marshaller.marshal(testService1Out, result);
		System.out.println(stringWriter.toString());
		
	}

}
