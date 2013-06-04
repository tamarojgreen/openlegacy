package org.openlegacy.rpc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.rpc.definitions.RpcRegistryTest;
import org.openlegacy.rpc.modules.trail.RpcTrailSerializeTest;

@RunWith(Suite.class)
@SuiteClasses({ RpcRegistryTest.class, RpcMockConnectionTest.class, RpcMockSessionTest.class, RpcTrailSerializeTest.class })
public class OpenLegacyRpcRuntimeSuite {

}
