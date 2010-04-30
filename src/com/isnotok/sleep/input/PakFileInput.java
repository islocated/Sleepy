package com.isnotok.sleep.input;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class PakFileInput implements IEditorInput {
	private File file;
	
	public PakFileInput(File file){
		this.file = file;
	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return file.getName();
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		// TODO Auto-generated method stub
		return file.getName();
	}

	public Object getAdapter(Class adapter) {
		if(adapter == File.class){
		// TODO Auto-generated method stub
			return file;
		}
		return null;
	}

}
