package com.isnotok.sleep;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.isnotok.sleep.view.NavigatorView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		
		layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		//layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.7f, layout.getEditorArea());
		
	}
}
