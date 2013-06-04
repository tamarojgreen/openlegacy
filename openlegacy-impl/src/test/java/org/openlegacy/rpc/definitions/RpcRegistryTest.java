package org.openlegacy.rpc.definitions;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.definitions.mock.RpcDummyEntity;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/test-rpc-basic-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcRegistryTest {

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;
	
	@Test
	public void testRegistryLoad() {
		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(RpcDummyEntity.class);
		Assert.assertNotNull(rpcEntityDefinition);
	}
}
