package com.isnotok.sleep.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.isnotok.sleep.model.ModelObject;

public class CacheContentProvider implements ITreeContentProvider {

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof ModelObject){
			ModelObject directory = (ModelObject) parentElement;
			return directory.getChildren();
		}
		
		// TODO Auto-generated method stub
		return new Object[0];
	}

	public Object getParent(Object element) {
		if(element instanceof ModelObject){
			ModelObject directory = (ModelObject) element;
			return directory.getParent();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof ModelObject){
			ModelObject directory = (ModelObject) element;
			return directory.hasChildren();
		}
		
		// TODO Auto-generated method stub
		return false;
	}

	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return getChildren(inputElement);
	}

}
