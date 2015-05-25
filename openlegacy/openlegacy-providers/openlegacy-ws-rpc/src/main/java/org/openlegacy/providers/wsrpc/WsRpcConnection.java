package org.openlegacy.providers.wsrpc;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import opr.openlegacy.rpc.wsrpc.WsRpcInvokeAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcResult;

public class WsRpcConnection implements RpcConnection{

	private final static Log logger = LogFactory.getLog(WsRpcConnection.class);
	
	private SOAPConnectionFactory connectionFactory;
	private MessageFactory messageFactory;
	
	public WsRpcConnection(){
		try{
			connectionFactory = SOAPConnectionFactory.newInstance();
			messageFactory = MessageFactory.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	@Override
	public RpcSnapshot getSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RpcSnapshot fetchSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAction(RpcInvokeAction sendAction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getSequence() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getDelegate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		// TODO connection
		return false;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub		
	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		SimpleRpcResult result = new SimpleRpcResult();
		try{			
			SOAPConnection connection = connectionFactory.createConnection();
			soapToRpcResult(connection.call(
								actionToSoap(rpcInvokeAction), 
								rpcInvokeAction.getRpcPath()),
							(WsRpcInvokeAction) rpcInvokeAction,result);
			connection.close();
		}catch(Exception e){
			e.printStackTrace();			
		}
		return result;
	}

	@Override
	public void login(String user, String password) {
		
	}
	
	private SOAPMessage actionToSoap(RpcInvokeAction action) throws Exception{
		WsRpcInvokeAction localAction = (WsRpcInvokeAction)action;
		SOAPMessage message = messageFactory.createMessage();
		
		String prefix = "action";		
		SOAPElement actionElement = message.getSOAPBody().addChildElement(localAction.getAction(),
																			prefix,
																			localAction.getNamespace());	
		
		
		for (RpcField field : action.getFields()){
			if (field.getDirection() == Direction.INPUT || field.getDirection() == Direction.INPUT_OUTPUT){
				/*
				 * In this case could be better constant using.
				 * For example we have
				 * 	int NONE = 0;
				 * 	int INPUT = 1;
				 * 	int OUTPUT = 2;
				 * 	int INPUT_OUTPUT = INPUT | OUTPUT;
				 * 
				 * So, we can use bit operations like
				 * 	INPUT_OTPUT AND INPUT = INPUT
				 * 	INPUT AND INPUT = INPUT
				 * 
				 * Then we can use 
				 * 	(field.getDirection() & Direction.INPUT) == Direction.INPUT 
				 * */	
				
				if (field instanceof RpcFlatField){
					actionElement.addChildElement(field.getName()).
						setValue(String.valueOf(((RpcFlatField) field).getValue()));
				}		
			}	
		} 					
		logMessage("Request message:", message);		
		return message;
	}
	
	private void soapToRpcResult(SOAPMessage response, WsRpcInvokeAction action,SimpleRpcResult result) throws Exception{
		logMessage("Response message:", response);
		QName responseQName = new QName(action.getNamespace(),action.getAction() + "Response");
		for (RpcField field : action.getFields()){
			if (field.getDirection() == Direction.OUTPUT  || field.getDirection() == Direction.INPUT_OUTPUT){				
				SOAPElement data = findSOAPElement(response.getSOAPBody().
									getChildElements(responseQName),field.getName());				
				if (data == null)
					continue;		
				
				if (field instanceof RpcFlatField){
					if (field.getType() == String.class)
						((RpcFlatField)field).setValue(String.valueOf(data.getValue()));
					else
					if (field.getType() == int.class || (field.getType() == Integer.class))
						((RpcFlatField)field).setValue(Integer.valueOf(data.getValue()));
				}
			}
		}
		result.setRpcFields(action.getFields());
	}	
	
	private void logMessage(String header, SOAPMessage message){
		try{
			if (logger.isDebugEnabled()){
				logger.debug(header);
				message.writeTo(System.out);
				System.out.println();			
			}	
		}catch(Exception e){}		
	}
	
	private SOAPElement findSOAPElement(Iterator<?> parent,String name){		
		while (parent.hasNext()){
			SOAPElement element = (SOAPElement)parent.next();
			if (element.getElementName().getLocalName().equals(name))
				return element;				
			else
				return findSOAPElement(element.getChildElements(), name);
		}
		return null;
	}
}
