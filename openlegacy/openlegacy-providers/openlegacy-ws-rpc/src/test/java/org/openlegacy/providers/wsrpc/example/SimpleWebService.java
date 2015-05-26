package org.openlegacy.providers.wsrpc.example;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(name = "SimpleWebService", targetNamespace = "http://SimpleWebService/")
@SOAPBinding(style = Style.RPC)
public class SimpleWebService {

	@WebMethod
	@WebResult(partName = "callBackResult")
	public String callBackString(@WebParam(name = "callBackValue") String callBackValue) {
		return callBackValue;
	}

	@WebMethod
	@WebResult(partName = "callBackResult")
	public int callBackInteger(@WebParam(name = "callBackValue") int callBackValue) {
		return callBackValue;
	}
}
