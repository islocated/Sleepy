package com.isnotok.sleep.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.isnotok.sleep.model.ModelObject;

public class ResourceFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof ModelObject){
			ModelObject model = (ModelObject) element;
			boolean accept = model.getFile().isDirectory()
				|| (model.getFile().getName().length() == 12);
			
			return accept;
		}
		// TODO Auto-generated method stub
		return false;
	}

}
