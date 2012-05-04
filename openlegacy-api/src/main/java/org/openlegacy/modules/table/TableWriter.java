package org.openlegacy.modules.table;

import java.io.OutputStream;
import java.util.List;

public interface TableWriter {

	void writeTable(List<? extends Object> records, OutputStream outputStream);

}
