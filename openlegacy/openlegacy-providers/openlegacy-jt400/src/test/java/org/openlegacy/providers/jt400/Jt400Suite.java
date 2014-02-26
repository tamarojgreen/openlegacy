package org.openlegacy.providers.jt400;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Jt400RpcConnectionTest.class, Jt400RpcSessionTest.class, Jt400RpcActionToPcmlTest.class,
		Jt400RpcManipulateSessionTest.class })
public class Jt400Suite {

}
