package com.isnotok.sleep.sorter;

import org.eclipse.jface.viewers.ViewerSorter;

import com.isnotok.sleep.model.ModelObject;

public class ResourceSorter extends ViewerSorter {
	@Override
	public int category(Object element) {
		if(element instanceof ModelObject){
			ModelObject model = (ModelObject) element;
			return model.getFile().isDirectory() ? 0 : 1;
		}
		// TODO Auto-generated method stub
		return super.category(element);
	}
}
