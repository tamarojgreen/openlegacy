package org.openlegacy.designtime.rpc.generators;

import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;

@RpcEntity
public class DetailsDummyEntity {

	@RpcField(length = 5)
	private String id;

	@RpcField(length = 10)
	private String name;

	@RpcField(length = 20)
	private String address;
}
