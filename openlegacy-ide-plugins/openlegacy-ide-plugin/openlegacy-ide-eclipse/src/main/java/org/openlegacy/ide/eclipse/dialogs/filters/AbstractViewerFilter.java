package org.openlegacy.ide.eclipse.dialogs.filters;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractViewerFilter extends ViewerFilter {

	private String interfaceNameForComparison = "";

	public AbstractViewerFilter(String interfaceNameForComparison) {
		this.interfaceNameForComparison = interfaceNameForComparison;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof TypeNameMatch) {
			TypeNameMatch match = (TypeNameMatch)element;
			IType type = match.getType();
			try {
				String[] superInterfaceNames = type.getSuperInterfaceNames();
				for (String name : superInterfaceNames) {
					if (name.equals(interfaceNameForComparison)) {
						return true;
					}
				}
				String superclassName = type.getSuperclassName();
				if ((superclassName != null) && (!superclassName.equals(Object.class.getName()))) {
					ITypeHierarchy typeHierarchy = type.newSupertypeHierarchy(null);
					IType[] allInterfaces = typeHierarchy.getAllSuperInterfaces(type);
					for (IType item : allInterfaces) {
						String fullyQualifiedName = item.getFullyQualifiedName('.');
						if (fullyQualifiedName.equals(interfaceNameForComparison)) {
							return true;
						}
						superInterfaceNames = item.getSuperInterfaceNames();
						for (String name : superInterfaceNames) {
							if (name.equals(interfaceNameForComparison)) {
								return true;
							}
						}
					}
				}
			} catch (JavaModelException e) {
			}
		}
		return false;
	}

}
