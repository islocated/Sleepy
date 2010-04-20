package com.isnotok.sleep.view;

import java.io.File;

import org.eclipse.ui.navigator.CommonNavigator;

public class NavigatorView extends CommonNavigator {
	public final static String ID = "com.isnotok.sleep.view.NavigatorView";
	
	public NavigatorView() {
		// TODO Auto-generated constructor stub
		
	}
	
	//We don't need IAdaptable!!!!  Don't need to wrap File anymore
	protected Object getInitialInput()
	{
		this.getCommonViewer().refresh();
		return new File("/Users/Mint/Documents/RCP/com.isnotok.sleep");//"/");
	}
}
