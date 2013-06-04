package com.as400samplecode;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.Trace;

public class CallRPGProgramInt {

	public static void main(String[] args) {
		String firstName = args[0].trim();
		String lastName = args[1].trim();
		Integer age = Integer.parseInt(args[2]);
		String message = "";

		// Connect to the iSeries using hostname, userid and password

		Trace.setTraceOn(true);
		Trace.setTraceDiagnosticOn(true);
		Trace.setTraceInformationOn(true);
		Trace.setTraceWarningOn(true);
		Trace.setTraceErrorOn(true);
		Trace.setTraceDatastreamOn(true);
		Trace.setTraceThreadOn(true);
		Trace.setTraceJDBCOn(true);

		AS400 as400System = new AS400("as400.openlegacy.org", "RMR20924", "roi045");

		// The ProgramCall class allows a user to call an iSeries server program,
		// pass parameters to it (input and output), and access data returned in the
		// output parameters after the program runs. Use ProgramCall to call programs.
		ProgramCall program = new ProgramCall(as400System);

		try {
			// Initialize the name of the program to run.
			String programName = "/QSYS.LIB/RMR2L1.LIB/RPGROI.PGM";
			// Set up the 3 parameters.
			ProgramParameter[] parameterList = new ProgramParameter[4];

			// Parameter 1 is the First Name
			AS400Text textData = new AS400Text(20, as400System);
			parameterList[0] = new ProgramParameter(textData.toBytes(firstName));

			// Parameter 2 is the Last Name
			textData = new AS400Text(20, as400System);
			parameterList[1] = new ProgramParameter(textData.toBytes(lastName));

			// Parameter 3 is the Age
			AS400ZonedDecimal as400ZonedDecimal = new AS400ZonedDecimal(3, 0);
			double myAge = Double.valueOf(age);
			parameterList[2] = new ProgramParameter(as400ZonedDecimal.toBytes(myAge));

			// Parameter 3 to get the answer, up to 100 bytes long.
			parameterList[3] = new ProgramParameter(100);

			// Set the program name and parameter list.
			program.setProgram(programName, parameterList);

			// Run the program.
			if (program.run() != true) {
				// Report failure.
				System.out.println("Program failed!");
				// Show the messages.
				AS400Message[] messageList = program.getMessageList();
				for (int i = 0; i < messageList.length; ++i) {
					// Show each message.
					System.out.println(messageList[i].getText());
					// Load additional message information.
					messageList[i].load();
					// Show help text.
					System.out.println(messageList[i].getHelp());
				}
			}

			// Else no error, get output data.
			else {
				textData = new AS400Text(100, as400System);
				message = (String)textData.toObject(parameterList[3].getOutputData());

			}
		} catch (Exception e) {
			System.out.println("Program " + program.getProgram() + " issued an exception!");
			e.printStackTrace();
		}

		// Done with the server.
		as400System.disconnectAllServices();

		// Print the output from the RPGLE called program
		System.out.println("Message is: " + message);

	}
}