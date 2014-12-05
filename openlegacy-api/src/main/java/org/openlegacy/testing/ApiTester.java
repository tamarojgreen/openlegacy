package org.openlegacy.testing;

import org.openlegacy.Session;

public interface ApiTester<S extends Session> {

	ApiReport test(S session);
}
