package org.openlegacy.providers.wsrpc.example;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class NewWebService {

	@WebMethod
	public String getHelloWorld() {
		return "Hello World";
	}
}
