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

public class PakView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.PakView";
	private PakFile pakfile;
	private Gallery gallery;

	public PakView() {
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
				if(pakfile == null){
					return;
				}
				
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if (index == 0) {
						// This is group 1
						item.setText("tile");
						item
								.setItemCount(pakfile.getResourceType("tile").length);
						//gr.setItemWidth(64);
						//gr.setItemHeight(64);
						item.setExpanded(true);
					} else if (index == 1) {
						// This is group 1
						item.setText("sprite");
						item
								.setItemCount(pakfile.getResourceType("sprite").length);
						//gr.setItemWidth(64);
						//gr.setItemHeight(64);
						item.setExpanded(true);
					} else if (index == 2) {
						// This is group 1
						item.setText("scale");
						item
								.setItemCount(pakfile.getResourceType("scale").length);
						//gr.setItemWidth(256);
						//gr.setItemHeight(256);
						item.setExpanded(true);
					} else if (index == 3) {
						// This is group 1
						item.setText("object");
						item
								.setItemCount(pakfile.getResourceType("object").length);
						item.setExpanded(true);
					} else if (index == 4) {
						// This is group 1
						item.setText("room");
						item
								.setItemCount(pakfile.getResourceType("room").length);
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
					
					
					PakRecord pakrecord = (PakRecord) pakfile
							.getResourceType(parentItem.getText())[index];
					
					item.setText(pakrecord.getWordString());

					Image img = new Image(parent.getDisplay(), pakrecord
							.getImageData());
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
				if(file.getName().endsWith(".pak")){
					pakfile = ResourceManager.getInstance().getPakFile(file);
					gallery.clearAll();
					gallery.setItemCount(3);
				}
			}
		}
	}

}
