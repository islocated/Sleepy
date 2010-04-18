package com.isnotok.sleep.model;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

public class FileModel extends File implements IAdaptable{
	
	/*
	public Object getAdapter(Class adapter) {
		if(adapter == File.class){
			FileModel fileModel = new 
		}
			
		// TODO Auto-generated method stub
		return null;
	}
	*/
	
	public FileModel(File parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}
	
	

	public FileModel(String parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}



	public FileModel(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}



	public FileModel(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}
	
	public FileModel[] listFileModels(){
		String [] strings = list();
		
		if(strings == null){
			return new FileModel[0];
		}
		
		FileModel [] fileModels = new FileModel[strings.length];
		for(int i = 0; i < strings.length; i++){
			fileModels[i] = new FileModel(this, strings[i]);
		}
		
		return fileModels;
	}

	
	public Object getAdapter(Class adapter) {
		IAdapterManager manager = Platform.getAdapterManager();
		//...;//lookup the IAdapterManager service         
		return manager.getAdapter(this, adapter);
	}

/*
	public Object getAdapter(Class adapter) {
		return AdapterManager.getDefault().getAdapter(this, adapter);
	}*/
}
