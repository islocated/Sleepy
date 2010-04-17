package com.isnotok.sleep;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		
		layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
	}
}
