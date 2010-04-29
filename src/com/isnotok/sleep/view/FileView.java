package com.isnotok.sleep.view;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.isnotok.sleep.model.Resource;
import com.isnotok.sleep.model.RoomResource;
import com.isnotok.sleep.model.SpriteResource;
import com.isnotok.sleep.model.TileResource;
import com.isnotok.sleep.model.TimbreResource;

//Implement ISelectionProvider if we want this view to return the zoom
public class FileView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.FileView";
	private Gallery gallery;
	
	private Resource resource;

	public FileView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(final Composite parent) {
		//Sets up the ability for this view to get selection events from all views
		getSite().getPage().addSelectionListener(this);

		gallery = new Gallery(parent, SWT.V_SCROLL | SWT.VIRTUAL);
		
		final DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(128);
		gr.setItemWidth(128);
		gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);
		gallery.setAntialias(SWT.OFF);

		
		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		ir.setShowLabels(true);
		ir.setDropShadows(true);
		ir.setDropShadowsSize(2);
		gallery.setItemRenderer(ir);
		
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				if(resource == null){
					return;
				}
				
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if (index == 0) {
						// This is group 1
						item.setText("tile");
						item.setItemCount(1);
						item.setExpanded(true);
					}
					else {
						// Should never be used
						item.setItemCount(0);
					}
				} else {
					// It's an item
					GalleryItem parentItem = item.getParentItem();

					// Get item index
					int index = parentItem.indexOf(item);
					
					item.setText("default");
					
					if(resource.getImageData() != null){
						Image img = new Image(parent.getDisplay(), resource.getImageData());
						item.setImage(img);
						item.setText(resource.getResourceName());
					}
				}
			}
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		gallery.setFocus();
	}
	
	//ISelectionListner interface
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if(selection instanceof IStructuredSelection){
			IStructuredSelection sel = (IStructuredSelection) selection;
			Object element = sel.getFirstElement();
			if(element instanceof File){
				File file = (File) element;
				if(!file.getName().endsWith(".pak")){
					
					if(file.getParentFile().getName().equals("tile")){
						resource = new TileResource(file);
					}
					else if(file.getParentFile().getName().equals("timbre")){
						resource = new TimbreResource(file);
					}
					else if(file.getParentFile().getName().equals("sprite")){
						resource = new SpriteResource(file);
					}
					else if(file.getParentFile().getName().equals("room")){
						resource = new RoomResource(file);
					}
					
					if(resource != null){
						resource.load();
					}
					
					gallery.clearAll();
					gallery.setItemCount(1);
				}
			}
		}
	}
}
