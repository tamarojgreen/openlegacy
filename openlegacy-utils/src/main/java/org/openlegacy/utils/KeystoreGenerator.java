package org.openlegacy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author Ivan Bort
 * 
 */
public class KeystoreGenerator {

	public static void main(String[] args) throws Exception {
		String entry = null;
		String entryPassword = null;
		String keystore = null;
		String keystorePassword = null;
		if (args == null || args.length != 4) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Enter entry name (e.g. database.password):   ");
			entry = br.readLine();
			System.out.print("Enter entry password (can be empty):   ");
			entryPassword = br.readLine();
			if (entryPassword == null) {
				entryPassword = "";
			}
			System.out.print("Enter keystore name (can not be empty):   ");
			keystore = br.readLine();
			System.out.print("Enter keystore password (can not be empty):   ");
			keystorePassword = br.readLine();

			br.close();
		} else {
			entry = args[0];
			entryPassword = args[1];
			keystore = args[2];
			keystorePassword = args[3];
		}

		if (entry == null || entryPassword == null || keystore == null || keystorePassword == null) {
			throw new RuntimeException("Missing required parameters.");
		}

		File keystoreFile = new File(keystore);

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
		SecretKey generatedSecret = factory.generateSecret(new PBEKeySpec(entryPassword.toCharArray()));

		KeyStore ks = KeyStore.getInstance("JCEKS");
		if (keystoreFile.exists()) {
			ks.load(new FileInputStream(keystoreFile), keystorePassword.toCharArray());
		} else {
			ks.load(null, keystorePassword.toCharArray());
		}
		KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(keystorePassword.toCharArray());

		ks.setEntry(entry, new KeyStore.SecretKeyEntry(generatedSecret), keyStorePP);

		FileOutputStream fos = new FileOutputStream(keystoreFile);
		ks.store(fos, keystorePassword.toCharArray());

		System.out.println();
		System.out.println("Generated file location: " + keystoreFile.getAbsolutePath());
	}

}
