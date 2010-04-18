package com.isnotok.sleep.view;

import java.io.File;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.navigator.CommonNavigator;

import com.isnotok.sleep.model.FileModel;

public class NavigatorView extends CommonNavigator {
	public final static String ID = "com.isnotok.sleep.view.NavigatorView";
	
	public NavigatorView() {
		// TODO Auto-generated constructor stub
	}
	
	 protected IAdaptable getInitialInput()
	 {
		 this.getCommonViewer().refresh();
		 //DirectoryObject root = new FolderModel(null, "/");
		 
		 return new FileModel("/");//root;
	 }
}
