package com.isnotok.sleep.handler;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.isnotok.sleep.util.FileUtil;
import com.isnotok.sleep.view.NavigatorView;

public class DeleteFile implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		NavigatorView view = (NavigatorView) page.findView(NavigatorView.ID);
		
		ISelection sel = window.getActivePage().getSelection();
		if(sel instanceof IStructuredSelection){
			IStructuredSelection selection = (IStructuredSelection) sel;
			Object [] objects = selection.toArray();
			
			if(objects == null || objects.length == 0)
				return null;
			
			boolean delete;
			
			if(objects.length == 1){
				File file = (File) objects[0];
				delete = MessageDialog.openConfirm(window.getShell(), "Confirm Delete", "Are you sure you want to delete file '" + file.getName() + "'?");
			}
			else{
				delete = MessageDialog.openConfirm(window.getShell(), "Confirm Delete", "Are you sure you want to delete these multiple files?");
			}
			
			boolean deleted = false;
			for(Object obj : objects){
				if(obj instanceof File){
					File file = (File) obj;
					if(delete){
						if(FileUtil.deleteFile(file)){
							System.out.println("deleted");
							deleted = true;
						}
						else{
							System.out.println("error");
						}
					}
				}
			}
			
			if(deleted){
				view.getCommonViewer().refresh();
			}
		}
		
		// TODO Auto-generated method stub
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
