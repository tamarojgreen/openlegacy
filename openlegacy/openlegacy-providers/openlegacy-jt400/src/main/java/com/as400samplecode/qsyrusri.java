package com.as400samplecode;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.Trace;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;

// Example program to call "Retrieve User Information" (QSYRUSRI) API
public class qsyrusri {

	public qsyrusri() {}

	public static void main(String[] argv) {

		Trace.setTraceOn(true);
		Trace.setTraceDiagnosticOn(true);
		Trace.setTraceInformationOn(true);
		Trace.setTraceWarningOn(true);
		Trace.setTraceErrorOn(true);
		Trace.setTraceDatastreamOn(true);
		Trace.setTraceThreadOn(true);
		Trace.setTraceJDBCOn(true);

		AS400 as400System; // com.ibm.as400.access.AS400
		ProgramCallDocument pcml; // com.ibm.as400.data.ProgramCallDocument
		boolean rc = false; // Return code from ProgramCallDocument.callProgram()
		String msgId, msgText; // Messages returned from the server
		Object value; // Return value from ProgramCallDocument.getValue()

		System.setErr(System.out);

		// Construct AS400 without parameters, user will be prompted
		as400System = new AS400("as400.openlegacy.org", "RMR20924", "roi045");

		try {
			// Uncomment the following to get debugging information
			// com.ibm.as400.data.PcmlMessageLog.setTraceEnabled(true);

			System.out.println("Beginning PCML Example..");
			System.out.println("    Constructing ProgramCallDocument for QSYRUSRI API...");

			// Construct ProgramCallDocument
			// First parameter is system to connect to
			// Second parameter is pcml resource name. In this example,
			// serialized PCML file "qsyrusri.pcml.ser" or
			// PCML source file "qsyrusri.pcml" must be found in the classpath.
			pcml = new ProgramCallDocument(as400System, "com.as400samplecode.qsyrusri");

			// Set input parameters. Several parameters have default values
			// specified in the PCML source. Do not need to set them using Java code.
			System.out.println("    Setting input parameters...");
			pcml.setValue("qsyrusri.receiverLength", new Integer((pcml.getOutputsize("qsyrusri.receiver"))));

			// Request to call the API
			// User will be prompted to sign on to the system
			System.out.println("    Calling QSYRUSRI API requesting information for the sign-on user.");
			rc = pcml.callProgram("qsyrusri");

			// If return code is false, we received messages from the server
			if (rc == false) {
				// Retrieve list of server messages
				AS400Message[] msgs = pcml.getMessageList("qsyrusri");

				// Iterate through messages and write them to standard output
				for (AS400Message msg : msgs) {
					msgId = msg.getID();
					msgText = msg.getText();
					System.out.println("    " + msgId + " - " + msgText);
				}
				System.out.println("** Call to QSYRUSRI failed. See messages above **");
				System.exit(0);
			}
			// Return code was true, call to QSYRUSRI succeeded
			// Write some of the results to standard output
			else {
				value = pcml.getValue("qsyrusri.receiver.bytesReturned");
				System.out.println("        Bytes returned:      " + value);
				value = pcml.getValue("qsyrusri.receiver.bytesAvailable");
				System.out.println("        Bytes available:     " + value);
				value = pcml.getValue("qsyrusri.receiver.userProfile");
				System.out.println("        Profile name:        " + value);
				value = pcml.getValue("qsyrusri.receiver.previousSignonDate");
				System.out.println("        Previous signon date:" + value);
				value = pcml.getValue("qsyrusri.receiver.previousSignonTime");
				System.out.println("        Previous signon time:" + value);
			}
		} catch (PcmlException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			System.out.println("*** Call to QSYRUSRI failed. ***");
			System.exit(0);
		}

		System.exit(0);
	} // End main()

}