package com.isnotok.sleep;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.isnotok.sleep.view.MagnifyView;
import com.isnotok.sleep.view.NavigatorView;
import com.isnotok.sleep.view.PakView;
import com.isnotok.sleep.view.ResourceView;
import com.isnotok.sleep.view.RoomView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		
		IFolderLayout lbottom = layout.createFolder("lbottom", IPageLayout.BOTTOM, 0.5f, "left");
		
		left.addView(NavigatorView.ID);
		lbottom.addView(ResourceView.ID);
		
		//layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		//layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		
		IFolderLayout middle = layout.createFolder("middle", IPageLayout.LEFT, 0.7f, layout.getEditorArea());
		
		//right.addView(ResourceView.ID);
		
		middle.addView(PakView.ID);
		
		IFolderLayout bottommid = layout.createFolder("bottommid", IPageLayout.BOTTOM, 0.6f, "middle");
		
		bottommid.addView(RoomView.ID);
		
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.6f, "middle");
		
		right.addView(MagnifyView.ID);
		
		//layout.addView(PakView.ID, IPageLayout.RIGHT, 0.4f, layout.getEditorArea());
	}
}
