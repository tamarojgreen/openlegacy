package org.openlegacy.recognizers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.recognizers.pattern.PatternBasedScreensRecognizerTest;

@RunWith(Suite.class)
@SuiteClasses({ RegistryBasedScreensRecognizerTest.class,
		PatternBasedScreensRecognizerTest.class,
		CompositeScreensRecognizerTest.class })
public class RecognizersSuite {

}
