package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.Image;

public class RootDirectory extends ModelObject{
	
	public RootDirectory(){
	}

	@Override
	public Object[] getChildren() {
		File [] files = File.listRoots();
			
		return files.length == 0 ? new Object[0] : files;
	}
	
	@Override
	public Object getParent() {
		return null;
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return getChildren().length > 0;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return null;
	}
}
