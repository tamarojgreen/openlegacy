package org.openlegacy.designtime.rpc.source.parsers;

import org.junit.Assert;
import org.junit.Test;

public class CobolNumberInformationTest {

	@Test
	public void testInt() {
		CobolNumberInformation intTemplat = new CobolNumberInformation("999");
		Assert.assertEquals(0, intTemplat.getScale());
		Assert.assertEquals(3, intTemplat.getDigitBeforeDot());
		Assert.assertEquals(0, intTemplat.getDigitAfterDot());
		Assert.assertEquals(false, intTemplat.isSigned());
	}

	@Test
	public void testFloat() {
		CobolNumberInformation intTemplat = new CobolNumberInformation("S99V999");
		Assert.assertEquals(2, intTemplat.getDigitBeforeDot());
		Assert.assertEquals(3, intTemplat.getDigitAfterDot());
		Assert.assertEquals(true, intTemplat.isSigned());
	}

	@Test
	public void testScale() {
		CobolNumberInformation intTemplat = new CobolNumberInformation("PP99V999");
		Assert.assertEquals(2, intTemplat.getScale());
		Assert.assertEquals(2, intTemplat.getDigitBeforeDot());
		Assert.assertEquals(3, intTemplat.getDigitAfterDot());
	}

	@Test
	public void testScale2() {
		CobolNumberInformation intTemplat = new CobolNumberInformation("99V999PP");
		Assert.assertEquals(-2, intTemplat.getScale());
		Assert.assertEquals(2, intTemplat.getDigitBeforeDot());
		Assert.assertEquals(3, intTemplat.getDigitAfterDot());
	}

	@Test
	public void testEexp() {
		CobolNumberInformation intTemplat = new CobolNumberInformation("99V999E9");
		Assert.assertEquals(0, intTemplat.getScale());
		Assert.assertEquals(2, intTemplat.getDigitBeforeDot());
		Assert.assertEquals(3, intTemplat.getDigitAfterDot());
		Assert.assertEquals(1, intTemplat.getExponentDigits());
	}

}
