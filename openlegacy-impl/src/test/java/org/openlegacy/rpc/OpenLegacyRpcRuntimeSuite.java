package org.openlegacy.rpc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.rpc.definitions.RpcRegistryTest;
import org.openlegacy.rpc.mock.RpcSendValidationTest;
import org.openlegacy.rpc.modules.trail.RpcTrailSerializeTest;
import org.openlegacy.rpc.render.RpcImageRendererTest;
import org.openlegacy.rpc.support.binders.RpcBooleanFieldsBinderTest;
import org.openlegacy.rpc.support.binders.RpcDateFieldsBinderTest;

@RunWith(Suite.class)
@SuiteClasses({ RpcRegistryTest.class, RpcMockConnectionTest.class, RpcMockSessionTest.class, RpcTrailSerializeTest.class,
		RpcImageRendererTest.class, RpcMockItemsConnectionTest.class, RpcMockDemoSessionTest.class,
		RpcMockItemDetailsConnectionTest.class, RpcMockSessionWithNoActionTest.class, RpcBooleanFieldsBinderTest.class,
		RpcDateFieldsBinderTest.class, RpcSendValidationTest.class })
public class OpenLegacyRpcRuntimeSuite {

}
