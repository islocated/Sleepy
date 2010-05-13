package com.isnotok.sleep.handler;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.isnotok.sleep.view.NavigatorView;

public class Rename implements IHandler {

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
		final NavigatorView view = (NavigatorView) page.findView(NavigatorView.ID);
		// Get the selection
		ISelection selection = view.getSite().getSelectionProvider().getSelection();
		
		IStructuredSelection sel = (IStructuredSelection) selection;
		
		Object fobj = sel.getFirstElement();
		
		final File file = (File) fobj;
		
		Tree tree = view.getCommonViewer().getTree();//.getSelection();
		
		TreeItem [] items = tree.getSelection();//;.getFirstElement();
		
		if(items == null || items.length < 1){
			return null;
		}
		
		TreeItem treeItem = items[0];
		
		final TreeEditor editor = new TreeEditor(treeItem.getParent());
		//The editor must have the same size as the cell and must
		//not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

		// Clean up any previous editor control
		Control oldEditor = editor.getEditor();
		if (oldEditor != null) oldEditor.dispose();

		
		// The control that will be the editor must be a child of the Tree
		final Text newEditor = new Text(treeItem.getParent(), SWT.BORDER);
		newEditor.setText(treeItem.getText());
		newEditor.selectAll();
		newEditor.setFocus();
		editor.setEditor(newEditor, treeItem);
		
		newEditor.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
	              switch (e.keyCode) {
	              case SWT.CR:
	                // Enter hit--set the text into the tree and drop through
	            	  Text text = (Text)editor.getEditor();
	            	  
	            	  if(renameFile(file, text.getText())){
	            		  editor.getItem().setText(text.getText());
	            		  newEditor.dispose();
	            		  view.getCommonViewer().refresh();
	            	  }
	            	  else{
	            		  newEditor.dispose();
	            		  MessageDialog.openError(view.getSite().getShell(), "Rename Failed", "Unable to rename file.");
	            	  }
	            	  
	            	  break;
	  					//fall through
	              case SWT.ESC:
	                // End editing session
	            	  newEditor.dispose();
	                break;
	              }
	            }

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		newEditor.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				Text text = (Text)editor.getEditor();
				if(renameFile(file, text.getText())){
					editor.getItem().setText(text.getText());
					newEditor.dispose();
					 view.getCommonViewer().refresh();
				}
				else{
					newEditor.dispose();
					MessageDialog.openError(view.getSite().getShell(), "Rename Failed", "Unable to rename file.");
				}
			}
			
		});
		

		return null;
	}

	protected boolean renameFile(File file, String text) {
		if(file.getName().equals(text)){
			return true;
		}
		
		File parent = file.getParentFile();
		File newFile = new File(parent, text);
		if(newFile.exists()){
			return false;
		}
		
		return file.renameTo(newFile);
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
