package com.isnotok.sleep.handler;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.isnotok.sleep.editor.CachePakEditor;
import com.isnotok.sleep.input.PakFileInput;
import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.view.NavigatorView;
import com.isnotok.sleep.view.PackagingView;

public class OpenFile implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage page = window.getActivePage();
		NavigatorView view = (NavigatorView) page.findView(NavigatorView.ID);
		
		FileDialog fd = new FileDialog(window.getShell(), SWT.OPEN);
        fd.setText("Open");
        String[] filterExt = { "*.pak" };
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        if(selected != null){
        	File file = new File(selected);
        	if(!file.exists()){
        		return null;
        	}
        		
        	System.out.println(selected);
        	
        	try {
				PakFile pfile = new PakFile(file);
				pfile.load();
				pfile.unpack();
				
				view.getCommonViewer().refresh();
				
				File newfile = new File(file.getParentFile(), file.getName().replace('.', '-'));
				
				PakFileInput pakFile = new PakFileInput(newfile);
				page.openEditor(pakFile, CachePakEditor.ID);
				
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
