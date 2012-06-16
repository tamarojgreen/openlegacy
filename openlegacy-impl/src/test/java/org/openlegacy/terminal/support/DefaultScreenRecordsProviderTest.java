package org.openlegacy.terminal.support;

import apps.inventory.screens.WarehouseDetails;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

@ContextConfiguration("DefaultScreenRecordsProviderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultScreenRecordsProviderTest extends AbstractTest {

	@Test
	public void testFieldRecords() {
		TerminalSession terminalSession = newTerminalSession();

		WarehouseDetails warehouseDetails = terminalSession.getEntity(WarehouseDetails.class);
		Assert.assertNotNull(warehouseDetails);
		Map<Object, Object> values = warehouseDetails.getWarehouseTypeValues();
		Assert.assertEquals(4, values.size());
		Set<Object> entrySet = values.keySet();
		Object firstRecord = entrySet.iterator().next();
		Assert.assertEquals("DR", firstRecord);

	}

}
