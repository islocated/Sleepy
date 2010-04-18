package com.isnotok.sleep.model;

import java.util.ArrayList;
import java.util.List;

public class FolderModel extends DirectoryObject{
	protected FolderModel parent;
	protected List<DirectoryObject> children;
	
	public FolderModel(FolderModel parent, String name) {
		this.parent = parent;
		this.name = name;
		
		children = new ArrayList<DirectoryObject>();
	}

	public Object[] getChildren(){
		return children.toArray();
	}
	
	public FolderModel getParent(){
		return parent;
	}

	public void addChild(DirectoryObject child) {
		// TODO Auto-generated method stub
		children.add(child);
	}
	
}
