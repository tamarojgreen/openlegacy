package org.openlegacy.terminal.support.obfuscator;

import java.util.Random;

public class EnglishLowerCaseObfuscator implements CharObfuscator {

	private Random random = new Random(SEED);

	@Override
	public char obfuscate(char ch) {
		int digit = random.nextInt(26);
		return (char)(digit + 97);
	}

	@Override
	public boolean isInRange(char ch) {
		return (ch >= 97 && ch <= 122);
	}

}
