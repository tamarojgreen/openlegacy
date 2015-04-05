package tests;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
 
import javax.inject.Inject;
import javax.xml.transform.stream.StreamResult;
 
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.openlegacy.ws.openlegacy.services.GetItemsService;
import com.openlegacy.ws.openlegacy.services.GetItemsService.*;
/**
 *  A test which invokes GetItems web service via an http client.
 *  The application should be app and running (via run-application.launch)
 *  To run the test, select Run As -> JUnit test.
 *  If the service has parameters, they should be set via the test. 
 */
@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class GetItemsServiceTest {

	@Inject
	@Qualifier("getItemsClient")
	private GetItemsService getItemsClient;

	@Inject
	private Marshaller marshaller;
	
	@Test
	public void testGetItemsService() throws ParseException, XmlMappingException, IOException {


		long before = System.currentTimeMillis();

		//GetItemsIn getItemsIn = new GetItemsIn();

		//GetItemsOut getItemsOut = getItemsClient.getGetItems(getItemsIn);
		GetItemsOut getItemsOut = getItemsClient.getGetItems();
		Assert.assertNotNull(getItemsOut);

        long after = System.currentTimeMillis();
  		System.out.println("Execution time:" + (after-before));
  				
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		marshaller.marshal(getItemsOut, result);
		System.out.println(stringWriter.toString());
		
	}

}
