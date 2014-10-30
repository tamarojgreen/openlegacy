package com.openlegacy.enterprise.ide.eclipse;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;
import org.openlegacy.annotations.rpc.RpcEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntityDescriber implements ITextContentDescriber {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.content.IContentDescriber#describe(java.io.InputStream,
	 * org.eclipse.core.runtime.content.IContentDescription)
	 */
	public int describe(InputStream contents, IContentDescription description) throws IOException {
		return describe(new InputStreamReader(contents), description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.content.IContentDescriber#getSupportedOptions()
	 */
	public QualifiedName[] getSupportedOptions() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.content.ITextContentDescriber#describe(java.io.Reader,
	 * org.eclipse.core.runtime.content.IContentDescription)
	 */
	public int describe(Reader contents, IContentDescription description) throws IOException {
		return doDescribe(contents) == null ? INVALID : VALID;
	}

	public static String doDescribe(Reader contents) {
		BufferedReader reader = new BufferedReader(contents);
		String line = null;
		try {
			boolean commentStarted = false;
			boolean commentEnded = true;
			while ((line = reader.readLine()) != null) {
				String trimmed = line.trim();
				if (trimmed.startsWith("//") || trimmed.startsWith("/*")) {
					commentStarted = true;
					commentEnded = false;
				}
				if (!commentStarted && commentEnded
						&& line.contains(MessageFormat.format("@{0}", RpcEntity.class.getSimpleName()))) {
					if (line.matches("\\s*@" + RpcEntity.class.getSimpleName() + ".*")) {
						return line;
					}
				}
				if (trimmed.startsWith("//") || trimmed.endsWith("*/")) {
					commentEnded = true;
					commentStarted = false;
				}
			}
		} catch (IOException e) {
		}

		return line;
	}

}
