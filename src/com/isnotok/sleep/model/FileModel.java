package com.isnotok.sleep.model;

public class FileModel extends DirectoryObject{
	protected FolderModel parent;
	
	public FileModel(FolderModel parent, String name){
		this.parent = parent;
		this.name = name;
	}
	
	public FolderModel getParent(){
		return parent;
	}
}
