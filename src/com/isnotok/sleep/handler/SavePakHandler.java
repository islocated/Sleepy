package com.isnotok.sleep.handler;

import java.io.File;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;
import com.isnotok.sleep.view.InventoryView;

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
        	System.out.println(selected);
        	
        	InventoryView inventory = (InventoryView) page.findView(InventoryView.ID);
    		List<PakRecord> list = inventory.getInventory();
    		
    		PakFile pakfile = new PakFile(new File(selected));
    		for(PakRecord record : list){
    			//If record is type room then we need to get the tiles....
    			pakfile.addRecord(record);
    		}
    		
    		pakfile.save();
    		
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
