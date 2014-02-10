package org.openlegacy.utils.jt400;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.rpc.SourceFetcher;
import org.openlegacy.utils.FileUtils;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Map;

public class Jt400SourceFetcher implements SourceFetcher {

	private Map<String, String> namesMappings;

	public void main(String[] args) throws OpenLegacyException {

		if (args.length < 4) {
			System.out.println("Usage:" + Jt400SourceFetcher.class.getSimpleName() + " host user password legacy_file");
			return;
		}

		String host = args[0];
		String user = args[1];
		String password = args[2];
		String legacyFile = args[3];
		System.out.println(fetch(host, user, password, legacyFile));
	}

	public String convertExtension(String legacyFileName) {
		String oldExtension = FilenameUtils.getExtension(legacyFileName);
		String fileName = FilenameUtils.getName(legacyFileName);
		if (namesMappings.containsKey(oldExtension)) {
			fileName = FileUtils.fileWithoutExtension(fileName) + "." + namesMappings.get(oldExtension);
		}
		return fileName;
	}

	public void setNamesMappings(Map<String, String> namesMappings) {
		this.namesMappings = namesMappings;
	}

	public byte[] fetch(String host, String user, String password, String legacyFile) throws OpenLegacyException {

		byte[] result = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String seperatoe = System.lineSeparator();
		try {
			SequentialFile file = getFile(host, user, password, legacyFile);
			Record data = file.readNext();
			// Loop while there are records in the file (while we have not
			// reached end-of-file).

			while (data != null) {
				output.write(data.toString().getBytes());
				output.write(seperatoe.getBytes());
				data = file.readNext();
			}
			result = new byte[output.size()];
			System.arraycopy(output.toByteArray(), 0, result, 0, output.size());
			output.close();
		}

		catch (Exception de) {
			throw new OpenLegacyException("Failed to get legacy file", de);

		}

		return result;
	}

	private static SequentialFile getFile(String host, String user, String password, String legacyFile)
			throws AS400SecurityException, IOException, AS400Exception, InterruptedException, PropertyVetoException {
		AS400 as400 = new AS400(host, user, password);
		as400.connectService(AS400.RECORDACCESS);
		QSYSObjectPathName filename = new QSYSObjectPathName(legacyFile);
		SequentialFile file = new SequentialFile(as400, filename.getPath());
		// Retrieve the record format for the file
		AS400FileRecordDescription recordDescription = new AS400FileRecordDescription(as400, filename.getPath());
		RecordFormat[] format = recordDescription.retrieveRecordFormat();
		file.setRecordFormat(format[0]);
		file.open(AS400File.READ_ONLY, 100, AS400File.COMMIT_LOCK_LEVEL_NONE);
		return file;
	}

}
