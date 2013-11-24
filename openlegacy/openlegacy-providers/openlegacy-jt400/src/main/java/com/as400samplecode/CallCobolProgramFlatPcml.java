package com.as400samplecode;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CallCobolProgramFlatPcml {

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

		ProgramCallDocument newPcml = new ProgramCallDocument(as400System, "com.as400samplecode.cobol_flat");
		// newPcml.serialize(new FileOutputStream("cobol_structure.ser"));
		// newPcml.serialize();

		// newPcml.setValue("cobol_flat.child1", 10);
		// newPcml.setValue("cobol_flat.child2", 0);
		boolean result = newPcml.callProgram("cobol_flat");

		if (result == false) {
			AS400Message[] msgs = newPcml.getMessageList("cobol_flat");

			// Iterate through messages and write them to standard output
			for (AS400Message msg : msgs) {
				msgId = msg.getID();
				msgText = msg.getText();
				System.out.println("    " + msgId + " - " + msgText);
			}
			System.out.println("** Call to FLAT failed. See messages above **");
			System.exit(0);
		}

		System.out.println("child2=" + newPcml.getValue("cobol_flat.child2"));
		// The ProgramCall class allows a user to call an iSeries server program,
		// pass parameters to it (input and output), and access data returned in the
		// output parameters after the program runs. Use ProgramCall to call programs.
		// Done with the server.
		as400System.disconnectAllServices();
	}
}
