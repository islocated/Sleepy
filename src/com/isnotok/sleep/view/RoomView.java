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

import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;
import com.isnotok.sleep.model.ResourceManager;

public class RoomView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.RoomView";
	private static final String[] TYPES = {"room", "scale", "music"};
	
	private PakFile pakfile;
	private Gallery gallery;

	public RoomView() {
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
					if (index >= 0 && index < TYPES.length) {
						item.setText(TYPES[index]);
						item.setItemCount(pakfile.getResourceType(TYPES[index]).length);
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
					PakRecord pakrecord = (PakRecord) pakfile.getResourceType(parentItem.getText())[index];
					
					item.setText(pakrecord.getWordString());

					Image img = new Image(parent.getDisplay(), pakrecord
							.getImageData());
					item.setImage(img);
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
				if(file.getName().endsWith(".pak")){
					pakfile = ResourceManager.getInstance().getPakFile(file);
					gallery.clearAll();
					gallery.setItemCount(3);
				}
			}
		}
	}

}
