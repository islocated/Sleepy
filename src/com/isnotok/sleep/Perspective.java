package com.isnotok.sleep;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.isnotok.sleep.view.NavigatorView;
import com.isnotok.sleep.view.PakView;
import com.isnotok.sleep.view.ResourceView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		
		layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		//layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.3f, layout.getEditorArea());
		
		right.addView(ResourceView.ID);
		
		layout.addView(PakView.ID, IPageLayout.RIGHT, 0.4f, layout.getEditorArea());
	}
}
