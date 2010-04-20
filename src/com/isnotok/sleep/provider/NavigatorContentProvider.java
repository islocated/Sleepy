package com.isnotok.sleep.provider;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class NavigatorContentProvider implements ITreeContentProvider {
	private static FileFilter fileFilter = new FileFilter(){
		Pattern p = Pattern.compile(".*jpg|.*gif|.*png|.*bmp|.*pak", Pattern.CASE_INSENSITIVE);
		
		public boolean accept(File pathname) {
			return (pathname.isDirectory() 
				|| p.matcher(pathname.getName()).matches()) 
				&& !pathname.getName().equalsIgnoreCase(".")	//Ignore current directory
				&& !pathname.isHidden();						//Ignore hidden files
		}
	};
	
	public Object[] getChildren(Object parentElement) {
		File file = (File) parentElement;
		
		Object [] files = file.listFiles(fileFilter);
		
		return files == null ? new Object[0] : files;
	}

	public Object getParent(Object element) {
		return ((File)element).getParent();
	}

	public boolean hasChildren(Object element) {
		return ((File) element).isDirectory() && getChildren(element).length > 0;
	}

	public Object[] getElements(Object inputElement) {
		//This is called when root is set as the input
		//TODO:What about when we select a new element?
		return getChildren(inputElement);
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		//Fire some event?
	}

}
