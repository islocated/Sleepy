package com.isnotok.sleep.view;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkbenchColors;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;

import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;
import com.isnotok.sleep.model.ResourceManager;
import com.isnotok.sleep.model.TileResource;

public class CacheView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.CacheView";
	private File resourceCache;
	private Gallery gallery;

	public CacheView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(final Composite parent) {
		// TODO Auto-generated method stub
		
		//Sets up the ability for this view to get selection events from all views
		getSite().getPage().addSelectionListener(this);

		gallery = new Gallery(parent, SWT.V_SCROLL | SWT.VIRTUAL);
		
		final DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(64);
		gr.setItemWidth(64);
		gr.setAutoMargin(true);
		//gr.setTitleBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		///gr.setTitleBackground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
		gallery.setGroupRenderer(gr);
		gallery.setAntialias(SWT.OFF);

		
		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		ir.setShowLabels(true);
		ir.setDropShadows(true);
		ir.setDropShadowsSize(2);
		gallery.setItemRenderer(ir);

		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				if(resourceCache == null){
					return;
				}
				
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if (index == 0) {
						item.setText("tile");
						item.setItemCount(new File(resourceCache, "tile").listFiles().length);
						item.setExpanded(true);
					} else if (index == 1) {
						item.setText("sprite");
						item.setItemCount(new File(resourceCache, "sprite").listFiles().length);
						item.setExpanded(true);
					} else if (index == 2) {
						item.setText("room");
						item.setItemCount(new File(resourceCache, "room").listFiles().length);
						item.setExpanded(true);
					} else if (index == 3) {
						item.setText("music");
						item.setItemCount(new File(resourceCache, "music").listFiles().length);
						item.setExpanded(true);
					} else if (index == 4) {
						item.setText("music");
						item.setItemCount(new File(resourceCache, "music").listFiles().length);
						item.setExpanded(true);
					} else if (index == 5) {
						item.setText("music");
						item.setItemCount(new File(resourceCache, "music").listFiles().length);
						item.setExpanded(true);
					} else {
						// Should never be used
						item.setItemCount(0);
					}
				} else {
					// It's an item
					GalleryItem parentItem = item.getParentItem();

					// Get item index
					int index = parentItem.indexOf(item);

					// Load corresponding items
					//Object[] objs = pakFile.getResourceType(parentItem
					//		.getText());
					
					File p = new File(resourceCache, parentItem.getText());
					File tile = p.listFiles()[index];
					TileResource tr = new TileResource(tile);
					tr.load();
					
					item.setText(tile.getName());

					Image img = new Image(parent.getDisplay(), tr.getImageData());
					item.setImage(img);
				}
			}
		});

		// Create one group (will call SetData)
		//gallery.setItemCount(5);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	//ISelectionListner interface
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if(selection instanceof IStructuredSelection){
			IStructuredSelection sel = (IStructuredSelection) selection;
			Object element = sel.getFirstElement();
			if(element instanceof File){
				File file = (File) element;
				if(file.getName().equals("resourceCache")){
					resourceCache = file;
					gallery.clearAll();
					gallery.setItemCount(2);
				}
			}
		}
	}

}
