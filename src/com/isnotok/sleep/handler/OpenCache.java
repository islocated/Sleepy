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

import com.isnotok.sleep.editor.CacheEditor;
import com.isnotok.sleep.input.PakFileInput;
import com.isnotok.sleep.view.NavigatorView;

public class OpenCache implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		IViewPart view = page.findView(NavigatorView.ID);
		// Get the selection
		ISelection selection = view.getSite().getSelectionProvider().getSelection();

		if(selection != null){
			if (selection != null && selection instanceof IStructuredSelection) {
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				// If we had a selection lets open the editor
				if (obj != null) {
					File file = (File) obj;
					if(file.getName().endsWith(".pak"));
					try {
						PakFileInput pakFile = new PakFileInput(file);
						page.openEditor(pakFile, CacheEditor.ID);
						
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
