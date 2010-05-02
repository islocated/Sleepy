package com.isnotok.sleep.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.isnotok.sleep.view.NavigatorView;

public class RefreshNavigator implements IHandler {

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
		
		view.getCommonViewer().refresh();
		
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
