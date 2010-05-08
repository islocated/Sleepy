package com.isnotok.sleep.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class NonResourceFilter extends ViewerFilter {

	Pattern p = Pattern.compile(".*jpg|.*gif|.*png|.*bmp|.*pak|[A-F0-9]*", Pattern.CASE_INSENSITIVE);
	
	public boolean accept(File pathname) {
		return (p.matcher(pathname.getName()).matches());
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof File){
			File file = (File) element;
			
			if(file.isDirectory()){
				return true;
			}
			
			return accept(file);
		}
		
		// TODO Auto-generated method stub
		return true;
	}

}
