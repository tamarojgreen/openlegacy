package org.openlegacy.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author Ivan Bort
 * 
 */
public class EncryptedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private String keystoreLocation;
	private String keystorePassword;

	private Map<String, String> properties = new HashMap<String, String>();

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {

		if (!StringUtils.isEmpty(keystoreLocation) && !StringUtils.isEmpty(keystorePassword)) {
			try {
				fillMap(keystoreLocation, keystorePassword, properties);
			} catch (Exception e) {
			}

		}
		super.processProperties(beanFactoryToProcess, props);
	}

	private static void fillMap(String keystoreLocation, String keyStorePassword, Map<String, String> map) throws Exception {
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.clear();

		KeyStore ks = KeyStore.getInstance("JCEKS");
		ks.load(null, keyStorePassword.toCharArray());
		KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());

		InputStream fIn = EncryptedPropertyPlaceholderConfigurer.class.getResourceAsStream(keystoreLocation);
		ks.load(fIn, keyStorePassword.toCharArray());

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");

		Enumeration<String> aliases = ks.aliases();
		while (aliases.hasMoreElements()) {
			String alias = aliases.nextElement();
			try {
				KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry)ks.getEntry(alias, keyStorePP);

				PBEKeySpec keySpec = (PBEKeySpec)factory.getKeySpec(ske.getSecretKey(), PBEKeySpec.class);

				String password = new String(keySpec.getPassword());
				map.put(alias, password);
			} catch (Exception e) {
				map.put(alias, "");
			}
		}
		fIn.close();
	}

	public String getKeystoreLocation() {
		return keystoreLocation;
	}

	public void setKeystoreLocation(String keystoreLocation) {
		this.keystoreLocation = keystoreLocation;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

}
