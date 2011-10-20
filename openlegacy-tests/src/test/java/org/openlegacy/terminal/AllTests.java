package org.openlegacy.terminal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.applinx.ApxSuite;
import org.openlegacy.recognizers.RecognizersSuite;

@RunWith(Suite.class)
@SuiteClasses({ SimpleScreenEntityBinderTest.class, RecognizersSuite.class,
		ApxSuite.class })
public class AllTests {

}
