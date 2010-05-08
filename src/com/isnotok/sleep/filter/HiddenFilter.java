package com.isnotok.sleep.filter;

import java.io.File;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class HiddenFilter extends ViewerFilter {

	public HiddenFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof File){
			File file = (File) element;
			
			//C:\ on windows is hidden
			if(file.getParent() == null && file.getPath().equals("C:\\")){
				return true;
			}
			
			return !file.isHidden();
		}
		
		// TODO Auto-generated method stub
		return true;
	}

}
