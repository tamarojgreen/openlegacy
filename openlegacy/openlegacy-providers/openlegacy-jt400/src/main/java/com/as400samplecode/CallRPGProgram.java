/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package com.as400samplecode;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.Trace;

import java.io.IOException;

public class CallRPGProgram {

	public static void main(String[] args) throws AS400SecurityException, IOException {

		if (args.length < 3) {
			System.out.println("Usage:" + CallCobolProgramPcml.class.getSimpleName() + " host user password");
			return;
		}

		String host = args[0];
		String user = args[1];
		String password = args[2];

		String firstName = "John";
		String lastName = "Doe";
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

		AS400 as400System = new AS400(host, user, password);
		as400System.authenticate(user, password);
		// The ProgramCall class allows a user to call an iSeries server program,
		// pass parameters to it (input and output), and access data returned in the
		// output parameters after the program runs. Use ProgramCall to call programs.
		ProgramCall program = new ProgramCall(as400System);

		try {
			// Initialize the name of the program to run.
			String programName = "/QSYS.LIB/RMR2L1.LIB/RPGROICH.PGM";
			// Set up the 3 parameters.
			ProgramParameter[] parameterList = new ProgramParameter[3];

			// Parameter 1 is the First Name
			AS400Text textData = new AS400Text(20, as400System);
			parameterList[0] = new ProgramParameter(textData.toBytes(firstName));

			// Parameter 2 is the Last Name
			textData = new AS400Text(20, as400System);
			parameterList[1] = new ProgramParameter(textData.toBytes(lastName));

			// Parameter 3 to get the answer, up to 100 bytes long.
			parameterList[2] = new ProgramParameter(30);

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
				textData = new AS400Text(30, as400System);
				message = (String)textData.toObject(parameterList[2].getOutputData());

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