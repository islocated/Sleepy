package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.Image;


public abstract class ModelObject {
	public Object[] getChildren() { 
		return new Object[0];
	}
	
	public Object getParent() {
		return null;
	}

	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}
	
	abstract public String getDescription();
	
	abstract public String getText();

	abstract public Image getImage();

	abstract public File getFile();
}
