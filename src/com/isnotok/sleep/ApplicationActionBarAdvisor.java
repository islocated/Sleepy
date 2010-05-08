package com.isnotok.sleep;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    //private IWorkbenchAction exitAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	//exitAction = ActionFactory.QUIT.create(window);
        //register(exitAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	//MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
    	//menuBar.add(fileMenu);
    	
    	//fileMenu.add(exitAction);
    }
    
}
