package com.openlegacy.enterprise.ide.eclipse.editors.utils.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenPartRemoveAspectAction;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class FileRemover {

	private static final String ASPECTJ_SUFFIX = "Aspect.aj";

	public static void removeAspectJFiles(CompilationUnit cu, List<AbstractAction> actionList) {
		if (actionList.isEmpty()) {
			return;
		}
		IJavaElement javaElement = cu.getJavaElement();
		IJavaElement parent = javaElement.getParent();
		File directory = parent.getResource().getLocation().toFile();
		for (AbstractAction abstractAction : actionList) {
			if (abstractAction instanceof ScreenPartRemoveAspectAction) {
				ScreenPartRemoveAspectAction action = (ScreenPartRemoveAspectAction)abstractAction;
				if (action.getNamedObject() instanceof ScreenPartModel) {
					ScreenPartModel model = (ScreenPartModel)action.getNamedObject();
					String filename = MessageFormat.format("{0}{1}_{2}", action.getEntityClassName(),
							model.getPreviousClassName(), ASPECTJ_SUFFIX);

					File aspectFile = new File(directory, filename);
					if (aspectFile.exists()) {
						aspectFile.delete();
					}
				}
			}
		}
	}

}
