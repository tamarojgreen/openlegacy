package org.openlegacy.terminal.support.obfuscator;

public interface CharObfuscator {

	public static final int SEED = 1234;

	public boolean isInRange(char ch);

	public char obfuscate(char ch);
}
