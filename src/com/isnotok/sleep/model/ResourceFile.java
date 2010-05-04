package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ResourceFile extends ModelObject {
	private File root;
	private ResourceDirectory parent;
	private Resource resource;
	
	public ResourceFile(ResourceDirectory resourceDirectory, File root){
		this.parent = resourceDirectory;
		this.root = root;
		
		//resource can be null because we can't suppport all objects yet
		resource = CacheManager.getInstance().getResource(root);
	}

	@Override
	public Object[] getChildren() {
		return new Object[0];
	}

	@Override
	public Object getParent() {
		return parent;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public String getDescription() {
		if(resource == null){
			return "";
		}
		// TODO Auto-generated method stub
		return resource.getType() + "-" + resource.getResourceName();
	}

	@Override
	public String getText() {
		if(resource == null){
			return "";
		}
		// TODO Auto-generated method stub
		return resource.getResourceName();
	}

	@Override
	public Image getImage() {
		if(resource == null){
			return null;
		}
		// TODO Auto-generated method stub
		return new Image(Display.getCurrent(), resource.getImageData());//resource.getImageData();
	}

	@Override
	public Object getFile() {
		// TODO Auto-generated method stub
		return root;
	}
}
