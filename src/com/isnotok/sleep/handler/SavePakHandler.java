package com.isnotok.sleep.handler;

import java.io.File;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.model.PakRecord;
import com.isnotok.sleep.view.InventoryView;
import com.isnotok.sleep.view.PackagingView;

public class SavePakHandler implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage page = window.getActivePage();
		
		FileDialog fd = new FileDialog(window.getShell(), SWT.SAVE);
        fd.setText("Save");
        fd.setFilterPath("/");
        String[] filterExt = { "*.pak" };
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        if(selected != null){
        	if(!selected.toLowerCase().endsWith(".pak")){
        		selected += ".pak";
        	}
        	
        	File file = new File(selected);
        	if(file.exists()){
        		boolean overwrite = MessageDialog.openConfirm(window.getShell(), "Confirm Overwrite", "Are you sure you want to overwrite file '" + file.getName() + "'?");
        		if(!overwrite)
        		{
        			return null;
        		}
        	}
        		
        	System.out.println(selected);
        	
        	PackagingView packaging = (PackagingView) page.findView(PackagingView.ID);
    		PakManager pakManager = packaging.getPakManager();
    		
    		//pakManager.save(selected);
    		
    		//pakfile.save();
    		
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
