package com.isnotok.sleep.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.isnotok.sleep.model.FileModel;

public class NavigatorContentProvider implements ITreeContentProvider {
	public Object[] getChildren(Object parentElement) {
		FileModel fileModel = (FileModel) parentElement;
		
		Object [] files = fileModel.listFileModels();
		
		return files;
	}

	public Object getParent(Object element) {
		return ((FileModel)element).getParent();
	}

	public boolean hasChildren(Object element) {
		return ((FileModel) element).isDirectory();
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
