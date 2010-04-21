package com.isnotok.sleep.view;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;

import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;

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
		
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(56);
		gr.setItemWidth(72);
		gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);

		
		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		ir.setShowLabels(true);
		gallery.setItemRenderer(ir);

		
			
		// Open Directory
		//final File f = new File("C:\\Images");
		//final File[] contents = f.listFiles();

		//File file = new File("/Users/Mint/Documents/RCP/com.isnotok.sleep",
		//		"input/4.pak");
		//final PakFile pakFile = new PakFile(file);

		/*try {
			pakFile.load();
			// PakRecord record = pakFile.getResource("tile",
			// "table nm").get(0);
			// imageData = record.getImageData();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

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
						item.setExpanded(true);
					} else if (index == 1) {
						// This is group 1
						item.setText("room");
						item
								.setItemCount(pakfile.getResourceType("room").length);
						item.setExpanded(true);
					} else if (index == 2) {
						// This is group 1
						item.setText("scene");
						item
								.setItemCount(pakfile.getResourceType("scene").length);
						item.setExpanded(true);
					} else if (index == 3) {
						// This is group 1
						item.setText("object");
						item
								.setItemCount(pakfile.getResourceType("object").length);
						item.setExpanded(true);
					} else if (index == 4) {
						// This is group 1
						item.setText("sprite");
						item
								.setItemCount(pakfile.getResourceType("sprite").length);
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
				pakfile = new PakFile(file);
				try {
					pakfile.load();
					gallery.setItemCount(5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DataFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
