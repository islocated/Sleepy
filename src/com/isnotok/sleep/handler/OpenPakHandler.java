package com.isnotok.sleep.handler;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.view.NavigatorView;
import com.isnotok.sleep.view.ResourceView;

public class OpenPakHandler implements IHandler {

	private int count = 0;
	
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		IViewPart view = page.findView(NavigatorView.ID);
		// Get the selection
		ISelection selection = view.getSite().getSelectionProvider()
				.getSelection();

		if(selection != null){
			if (selection != null && selection instanceof IStructuredSelection) {
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				// If we had a selection lets open the editor
				if (obj != null) {
					File file = (File) obj;
					if(file.getName().endsWith(".pak"));
					//PakFile pakfile = new PakFile(file);
					
					//event.g
					try {
						ResourceView rview = (ResourceView) page.showView(ResourceView.ID, file.getName(), IWorkbenchPage.VIEW_ACTIVATE | IWorkbenchPage.VIEW_CREATE);
						rview.setFile(file);
						
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*
					try {
						page.openEditor(input, MyPersonEditor.ID);

					} catch (PartInitException e) {
						System.out.println(e.getStackTrace());
					}
					*/
					
				}
			}
			return null;

		}
		
		return null;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		
		
		return true;
	}

	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
