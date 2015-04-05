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

import com.openlegacy.ws.openlegacy.services.GetItemDetailesService;
import com.openlegacy.ws.openlegacy.services.GetItemDetailesService.*;
/**
 *  A test which invokes GetItemDetailes web service via an http client.
 *  The application should be app and running (via run-application.launch)
 *  To run the test, select Run As -> JUnit test.
 *  If the service has parameters, they should be set via the test. 
 */
@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class GetItemDetailesServiceTest {

	@Inject
	@Qualifier("getItemDetailesClient")
	private GetItemDetailesService getItemDetailesClient;

	@Inject
	private Marshaller marshaller;
	
	@Test
	public void testGetItemDetailesService() throws ParseException, XmlMappingException, IOException {


		long before = System.currentTimeMillis();

		GetItemDetailesIn getItemDetailesIn = new GetItemDetailesIn();
		getItemDetailesIn.setItemNumber("1000");

		GetItemDetailesOut getItemDetailesOut = getItemDetailesClient.getGetItemDetailes(getItemDetailesIn);
		Assert.assertNotNull(getItemDetailesOut);

        long after = System.currentTimeMillis();
  		System.out.println("Execution time:" + (after-before));
  				
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		marshaller.marshal(getItemDetailesOut, result);
		System.out.println(stringWriter.toString());
		
	}

}
