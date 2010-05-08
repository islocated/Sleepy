package com.isnotok.sleep.filter;

import java.io.File;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.isnotok.sleep.model.ModelObject;

public class HiddenFilter extends ViewerFilter {

	public HiddenFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof File){
			File file = (File) element;
			
			return !file.isHidden();
		}
		
		// TODO Auto-generated method stub
		return true;
	}

}
