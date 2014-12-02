package com.someorg.examples.rpc;

import org.openlegacy.annotations.rpc.FieldMapping;
import org.openlegacy.annotations.rpc.RpcEntity;

@RpcEntity()
public class SignOn {

	private static String staticString = "staticString";
	private final String finalString = "finalString";

	private String user;
	private String password;

}
