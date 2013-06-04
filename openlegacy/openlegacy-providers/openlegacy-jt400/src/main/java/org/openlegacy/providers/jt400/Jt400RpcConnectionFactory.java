package org.openlegacy.providers.jt400;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Trace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public class Jt400RpcConnectionFactory implements RpcConnectionFactory, InitializingBean {

	private final static Log logger = LogFactory.getLog(Jt400RpcConnectionFactory.class);

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private FieldFormatter fieldFormatter;

	public RpcConnection getConnection() {
		AS400 as400 = applicationContext.getBean(AS400.class);
		Jt400RpcConnection rpcConnection = new Jt400RpcConnection(as400);
		rpcConnection.setFieldFormatter(fieldFormatter);
		return rpcConnection;
	}

	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();

	}

	public void afterPropertiesSet() throws Exception {
		if (logger.isTraceEnabled()) {
			Trace.setTraceOn(true);
			Trace.setTraceDiagnosticOn(true);
			Trace.setTraceInformationOn(true);
			Trace.setTraceWarningOn(true);
			Trace.setTraceErrorOn(true);
			Trace.setTraceDatastreamOn(true);
			Trace.setTraceThreadOn(true);
			Trace.setTraceJDBCOn(true);
		}
	}

}
