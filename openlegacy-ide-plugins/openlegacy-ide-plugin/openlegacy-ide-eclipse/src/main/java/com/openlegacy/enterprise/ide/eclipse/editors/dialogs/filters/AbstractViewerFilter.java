package com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jdt.internal.core.BinaryType;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractViewerFilter extends ViewerFilter {

	private Class<?> comparisonClass = null;

	public AbstractViewerFilter(Class<?> comparisonClass) {
		Assert.isNotNull(comparisonClass, "Cannot instantiate filter.");
		this.comparisonClass = comparisonClass;
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
					if (type instanceof BinaryType && name.equals(comparisonClass.getName())) {
						return true;
					} else if (type instanceof SourceType && name.equals(comparisonClass.getSimpleName())) {
						return true;
					}
				}
				String superclassName = type.getSuperclassName();
				if ((superclassName != null) && (!superclassName.equals(Object.class.getName()))) {
					if (superclassName.equals(comparisonClass.getName())) {
						return true;
					}
					ITypeHierarchy typeHierarchy = type.newSupertypeHierarchy(null);
					IType[] allInterfaces = typeHierarchy.getAllSuperInterfaces(type);
					for (IType item : allInterfaces) {
						String fullyQualifiedName = item.getFullyQualifiedName('.');
						if (fullyQualifiedName.equals(comparisonClass.getName())) {
							return true;
						}
						superInterfaceNames = item.getSuperInterfaceNames();
						for (String name : superInterfaceNames) {
							if (name.equals(comparisonClass.getName())) {
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
