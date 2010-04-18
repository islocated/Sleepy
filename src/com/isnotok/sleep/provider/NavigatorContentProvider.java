package com.isnotok.sleep.provider;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class NavigatorContentProvider implements ITreeContentProvider {
	public Object[] getChildren(Object parentElement) {
		File file = (File) parentElement;
		Object [] files = file.listFiles();
		
		return files == null ? new Object[0] : files;
	}

	public Object getParent(Object element) {
		return ((File)element).getParent();
	}

	public boolean hasChildren(Object element) {
		return ((File) element).isDirectory();
		//return getChildren(element).length > 0;
	}

	public Object[] getElements(Object inputElement) {
		//This is called when root is set as the input
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
