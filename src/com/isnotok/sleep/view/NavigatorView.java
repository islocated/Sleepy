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
		return File.listRoots()[0];//File("/");
	}
	
	public void refresh(){
		this.getCommonViewer().refresh();
	}
	
	/*
	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		if(anEvent.getSelection() instanceof StructuredSelection){
			StructuredSelection selection = (StructuredSelection) anEvent.getSelection();
			File file = (File) selection.getFirstElement();
			
			IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
			try {
				handlerService.executeCommand("com.isnotok.sleep.command.OpenPak", null);
			} catch (Exception ex) {
				throw new RuntimeException("add.command not found");
			}

		}
		else{
			// TODO Auto-generated method stub
			super.handleDoubleClick(anEvent);
		}
	}
	*/
}
