package org.openlegacy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.db.OpenLegacyDbRuntimeSuite;
import org.openlegacy.rpc.OpenLegacyRpcRuntimeSuite;
import org.openlegacy.terminal.OpenLegacyScreensRuntimeSuite;
import org.openlegacy.ws.WebServiceRegistryTest;

@RunWith(Suite.class)
@SuiteClasses({ OpenLegacyScreensRuntimeSuite.class, OpenLegacyRpcRuntimeSuite.class, OpenLegacyDbRuntimeSuite.class,
		WebServiceRegistryTest.class })
public class OpenLegacyRuntimeSuite {

}
