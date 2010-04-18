package com.isnotok.sleep.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.navigator.CommonNavigator;

import com.isnotok.sleep.model.DirectoryObject;
import com.isnotok.sleep.model.FolderModel;

public class NavigatorView extends CommonNavigator {
	public final static String ID = "com.isnotok.sleep.view.NavigatorView";
	
	public NavigatorView() {
		// TODO Auto-generated constructor stub
	}
	
	 protected IAdaptable getInitialInput()
	 {
		 this.getCommonViewer().refresh();
		 DirectoryObject root = new FolderModel(null, "/");
		 
		 
		 return root;
	 }
}
