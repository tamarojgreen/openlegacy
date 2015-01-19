package org.openlegacy.terminal.support.obfuscator;

import java.util.Random;

public class EnglishUpperCaseObfuscator implements CharObfuscator {

	private Random random = new Random(SEED);

	@Override
	public char obfuscate(char ch) {
		int digit = random.nextInt(26);
		return (char)(digit + 65);
	}

	@Override
	public boolean isInRange(char ch) {
		return (ch >= 65 && ch <= 90);
	}

}
