package com.as400samplecode;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CallCobolProgramTreeArrayPcml {

	public static void main(String[] args) throws PcmlException, FileNotFoundException, IOException {

		if (args.length < 3) {
			System.out.println("Usage:" + CallCobolProgramPcml.class.getSimpleName() + " host user password");
			return;
		}

		String host = args[0];
		String user = args[1];
		String password = args[2];
		String msgId, msgText; // Messages returned from the server

		// Connect to the iSeries using hostname, userid and password

		// Trace.setTraceOn(true);
		// Trace.setTraceDiagnosticOn(true);
		// Trace.setTraceInformationOn(true);
		// Trace.setTraceWarningOn(true);
		// Trace.setTraceErrorOn(true);
		// Trace.setTraceDatastreamOn(true);
		// Trace.setTraceThreadOn(true);
		// Trace.setTraceJDBCOn(true);

		AS400 as400System = new AS400(host, user, password);

		ProgramCallDocument newPcml = new ProgramCallDocument(as400System, "com.as400samplecode.tree_array");
		// newPcml.serialize();

		boolean result = newPcml.callProgram("tree_array");

		if (result == false) {
			AS400Message[] msgs = newPcml.getMessageList("tree_array");

			// Iterate through messages and write them to standard output
			for (AS400Message msg : msgs) {
				msgId = msg.getID();
				msgText = msg.getText();
				System.out.println("    " + msgId + " - " + msgText);
			}
			System.out.println("** Call to TREEARRAYPGM failed. See messages above **");
			System.exit(0);
		}
		int[] indices = new int[1]; // Indices for access array value
		for (indices[0] = 0; indices[0] < 3; indices[0]++) {
			int i = newPcml.getIntValue("tree_array.top.record.number", indices);
			String s = (String)newPcml.getValue("tree_array.top.record.text", indices);
			System.out.println(i);
			System.out.println(s);
		}

		// The ProgramCall class allows a user to call an iSeries server program,
		// pass parameters to it (input and output), and access data returned in the
		// output parameters after the program runs. Use ProgramCall to call programs.
		// Done with the server.
		as400System.disconnectAllServices();
	}
}
