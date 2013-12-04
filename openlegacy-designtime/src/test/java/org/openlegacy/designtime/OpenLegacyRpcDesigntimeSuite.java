package org.openlegacy.designtime;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.designtime.rpc.generators.RpcEntityCodeGeneratorTest;
import org.openlegacy.designtime.rpc.generators.RpcEntityPageGeneratorTest;
import org.openlegacy.designtime.rpc.source.parsers.CobolNumberInformationTest;
import org.openlegacy.designtime.rpc.source.parsers.CobolParserTest;
import org.openlegacy.designtime.rpc.source.parsers.CobolParserUtilsTest;
import org.openlegacy.designtime.rpc.source.parsers.PcmlParserTest;

@RunWith(Suite.class)
@SuiteClasses({ CobolParserTest.class, PcmlParserTest.class, CobolParserUtilsTest.class, CobolNumberInformationTest.class,
		RpcEntityCodeGeneratorTest.class, RpcEntityPageGeneratorTest.class })
public class OpenLegacyRpcDesigntimeSuite {

}
