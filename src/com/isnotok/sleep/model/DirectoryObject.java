package com.isnotok.sleep.model;

import org.eclipse.core.runtime.PlatformObject;

public class DirectoryObject extends PlatformObject{
	protected String name;
	
	public DirectoryObject(){
		
	}
	
	public DirectoryObject(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public Object [] getChildren(){
		return new Object[0];
	}
	
	public Object getParent(){
		return null;
	}
}
