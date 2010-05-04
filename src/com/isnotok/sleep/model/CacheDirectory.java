package com.isnotok.sleep.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

public class CacheDirectory extends ModelObject{
	private File root;
	
	public CacheDirectory(File root){
		this.root = root;
	}

	@Override
	public Object[] getChildren() {
		File [] files = root.listFiles();
		List<ResourceDirectory> list = new ArrayList<ResourceDirectory>(files.length);
		for(File file : files){
			if(file.isDirectory()){
				list.add(new ResourceDirectory(this, file));
			}
		}
		
		Object [] children = list.toArray();
		
		// TODO Auto-generated method stub
		return children.length == 0 ? new Object[0] : children;
	}
	
	@Override
	public Object getParent() {
		return null;
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return root.listFiles().length > 0;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return root.getName();
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return root.getName();
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return root;
	}
}
