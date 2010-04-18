package com.isnotok.sleep.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.isnotok.sleep.model.DirectoryObject;
import com.isnotok.sleep.model.FileModel;
import com.isnotok.sleep.model.FolderModel;

public class NavigatorContentProvider implements ITreeContentProvider {
	public Object[] getChildren(Object parentElement) {
		DirectoryObject directoryObject = (DirectoryObject) parentElement;
		
		if(directoryObject.getName().equals("/")){
			FolderModel folderModel = (FolderModel) directoryObject;
			folderModel.addChild(new FileModel(folderModel, "foo.bar"));
		}
		
		//Get children polymorphically
		return ((DirectoryObject) parentElement).getChildren();
	}

	public Object getParent(Object element) {
		return ((DirectoryObject)element).getParent();
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
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
