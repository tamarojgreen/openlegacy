package org.openlegacy.designtime.db.generators;

import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.generators.EntityPageGenerator;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.layout.PageDefinition;

import java.io.OutputStream;

public interface DbEntityPageGenerator extends EntityPageGenerator {

	public final static String LIST_MODE = "list";
	public final static String EDIT_MODE = "edit";

	public void generateView(GenerateViewRequest generateViewRequest, EntityDefinition<?> entityDefinition, String fileName,
			String mode);

	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix, String mode);
}