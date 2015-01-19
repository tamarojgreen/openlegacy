package org.openlegacy.terminal.support.obfuscator;

import java.util.Random;

public class NumberObfuscator implements CharObfuscator {

	private Random random = new Random(SEED);

	@Override
	public char obfuscate(char ch) {
		int digit = random.nextInt(10);
		return (char)(digit + 48);
	}

	@Override
	public boolean isInRange(char ch) {
		return (ch >= 48 && ch <= 57);
	}

}
